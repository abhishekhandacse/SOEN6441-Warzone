package Services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.MapValidationException;
import Models.GameState;
import Models.Map;
import Models.ModelContinent;
import Models.ModelCountry;
import Models.ModelPlayer;
import Utils.CommonUtil;

/**
 * This class is used to test functionality of PlayerService class functions.
 */
public class PlayerServiceTest {
    /**
	 * Player class reference.
	 */
	ModelPlayer d_playerInfo;

	/**
	 * Player Service reference.
	 */
	PlayerService d_playerService;

	/**
	 * Map reference to store its object.
	 */
	Map d_map;

	/**
	 * GameState reference to store its object.
	 */
	GameState d_gameState;

	/**
	 * MapService reference to store its object.
	 */
	MapService d_mapservice;

	/**
	 * Existing Player List.
	 */
	List<ModelPlayer> d_exisitingPlayerList = new ArrayList<>();

	private final ByteArrayOutputStream d_outContent = new ByteArrayOutputStream();

	/**
	 * The setup is called before each test case of this class is executed.
	 */
	@Before
	public void setup() {
		d_playerInfo = new ModelPlayer();
		d_playerService = new PlayerService();
		d_gameState = new GameState();
		d_exisitingPlayerList.add(new ModelPlayer("Anurag"));
		d_exisitingPlayerList.add(new ModelPlayer("Zalak"));

	}

	/**
	 * The testAddPlayers is used to test the add functionality of addRemovePlayers function.
	 * 
	 */
	@Test
	public void testAddPlayers() {
		assertFalse(CommonUtil.isNullOrEmptyCollection(d_exisitingPlayerList));
		List<ModelPlayer> l_updatedPlayers = d_playerService.addOrRemovePlayers(d_exisitingPlayerList, "add", "Jhanvi");
		assertEquals("Jhanvi", l_updatedPlayers.get(2).getPlayerName());

		System.setOut(new PrintStream(d_outContent));
		d_playerService.addOrRemovePlayers(d_exisitingPlayerList, "add", "Anurag");
		assertEquals("Player with name : Anurag already Exists. Changes are not made.", d_outContent.toString().trim());
	}

	/**
	 * The testRemovePlayers is used to t est the remove functionality of addRemovePlayers function.
	 * 
	 */
	@Test
	public void testRemovePlayers() {
		List<ModelPlayer> l_updatedPlayers = d_playerService.addOrRemovePlayers(d_exisitingPlayerList, "remove", "Anurag");
		assertEquals(1, l_updatedPlayers.size());

		System.setOut(new PrintStream(d_outContent));
		d_playerService.addOrRemovePlayers(d_exisitingPlayerList, "remove", "Rajat");
		assertEquals("Player with name : Rajat does not Exist. Changes are not made.", d_outContent.toString().trim());
	}

	/**
	 * Used for checking whether players have been added or not
	 */
	@Test
	public void testPlayersAvailability() {
		boolean l_playersExists = d_playerService.checkAvailability(d_gameState);
		assertFalse(l_playersExists);
	}

	/**
	 * Used for checking whether players have been assigned with countries
	 */
	@Test
	public void testPlayerCountryAssignment() throws MapValidationException {
		d_mapservice = new MapService();
		d_map = new Map();
		d_map = d_mapservice.loadMap(d_gameState, "canada.map");
		d_gameState.setD_map(d_map);
		d_gameState.setD_playersList(d_exisitingPlayerList);
		d_playerService.assignCountries(d_gameState);

		int l_assignedCountriesSize = 0;
		for (ModelPlayer l_pl : d_gameState.getD_playersList()) {
			assertNotNull(l_pl.getD_coutriesOwned());
			l_assignedCountriesSize = l_assignedCountriesSize + l_pl.getD_coutriesOwned().size();
		}
		assertEquals(l_assignedCountriesSize, d_gameState.getD_map().getD_allCountries().size());
	}

	/**
     * Required Test #4
	 * The testCalculateArmiesForPlayer is used to calculate number of reinforcement armies
	 * 
	 */
	@Test
	public void testCalculateArmiesForPlayer() {
		ModelPlayer l_playerInfo = new ModelPlayer();
		List<ModelCountry> l_countryList = new ArrayList<ModelCountry>();
		l_countryList.add(new ModelCountry("China"));
		l_countryList.add(new ModelCountry("Japan"));
		l_countryList.add(new ModelCountry("Sri Lanka"));
		l_countryList.add(new ModelCountry("India"));
		l_playerInfo.setD_coutriesOwned(l_countryList);
		List<ModelContinent> l_continentList = new ArrayList<ModelContinent>();
		l_continentList.add(new ModelContinent(1, "Asia", 5));
		l_playerInfo.setD_continentsOwned(l_continentList);
		l_playerInfo.setD_noOfUnallocatedArmies(10);
		Integer l_actualResult = d_playerService.calculatePlayerArmies(l_playerInfo);
		Integer l_expectedresult = 18;
		assertEquals(l_expectedresult, l_actualResult);
	}
}
