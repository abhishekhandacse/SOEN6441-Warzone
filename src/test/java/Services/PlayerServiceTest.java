package Services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import Exceptions.MapValidationException;

import org.junit.Before;
import org.junit.Test;

import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Models.ModelPlayer;
import Utils.CommonUtil;

/**
 * This class is used to test functionality of PlayerService class functions.
 */
public class PlayerServiceTest {
	/**
	 * ModelPlayer class reference.
	 */
	ModelPlayer d_playerInfo;

	/**
	 * ModelPlayer Service reference.
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
	 * Existing ModelPlayer List.
	 */
	List<ModelPlayer> d_exisitingPlayerList = new ArrayList<>();

	/**
	 * Byte Array Output Stream Object.
	 */
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
		d_exisitingPlayerList.add(new ModelPlayer("Rajat"));

	}

	/**
	 * The testAddPlayers is used to test the add functionality of addRemovePlayers
	 * function.
	 * @throws IOException in case of failure in receiving user input
	 */
	@Test
	public void testAddPlayers() throws IOException {
		assertFalse(CommonUtil.isCollectionEmpty(d_exisitingPlayerList));

		System.setOut(new PrintStream(d_outContent));
		d_playerService.addRemovePlayers(d_exisitingPlayerList, "add", "Anurag");
		assertEquals("ModelPlayer with name : Anurag already Exists. Changes are not made.", d_outContent.toString().trim());
	}

	/**
	 * The testRemovePlayers is used to t est the remove functionality of
	 * addRemovePlayers function.
	 * @throws IOException in case of failure in receiving user input
	 */
	@Test
	public void testRemovePlayers() throws IOException {
		List<ModelPlayer> l_updatedPlayers = d_playerService.addRemovePlayers(d_exisitingPlayerList, "remove", "Anurag");
		assertEquals(1, l_updatedPlayers.size());

		System.setOut(new PrintStream(d_outContent));
		d_playerService.addRemovePlayers(d_exisitingPlayerList, "remove", "Harman");
		assertEquals("ModelPlayer with name : Harman does not Exist. Changes are not made.", d_outContent.toString().trim());
	}

	/**
	 * Used for checking whether players have been added or not
	 */
	@Test
	public void testPlayersAvailability() {
		boolean l_playersExists = d_playerService.checkPlayersAvailability(d_gameState);
		assertFalse(l_playersExists);
	}

	/**
	 * Used for checking whether players have been assigned with countries
	 * 
	 * @throws InvalidMap invalid map exception
	 */
	@Test
	public void testPlayerCountryAssignment() throws MapValidationException {
		d_mapservice = new MapService();
		d_map = new Map();
		d_map = d_mapservice.loadMap(d_gameState, "canada.map");
		d_gameState.setD_map(d_map);
		d_gameState.setD_players(d_exisitingPlayerList);
		d_playerService.assignCountries(d_gameState);

		int l_assignedCountriesSize = 0;
		for (ModelPlayer l_pl : d_gameState.getD_players()) {
			assertNotNull(l_pl.getD_coutriesOwned());
			l_assignedCountriesSize = l_assignedCountriesSize + l_pl.getD_coutriesOwned().size();
		}
		assertEquals(l_assignedCountriesSize, d_gameState.getD_map().getD_countriesList().size());
	}

	/**
	 * The testCalculateArmiesForPlayer is used to calculate number of reinforcement
	 * armies
	 */
	@Test
	public void testCalculateArmiesForPlayer() {
		ModelPlayer l_playerInfo = new ModelPlayer();
		List<Country> l_countryList = new ArrayList<Country>();
		l_countryList.add(new Country("Waadt"));
		l_countryList.add(new Country("Neuenburg"));
		l_countryList.add(new Country("Fribourg"));
		l_countryList.add(new Country("Geneve"));
		l_playerInfo.setD_coutriesOwned(l_countryList);
		List<Continent> l_continentList = new ArrayList<Continent>();
		l_continentList.add(new Continent(1, "Asia", 5));
		l_playerInfo.setD_continentsOwned(l_continentList);
		l_playerInfo.setD_noOfUnallocatedArmies(10);
		Integer l_actualResult = d_playerService.calculateArmiesForPlayer(l_playerInfo);
		Integer l_expectedresult = 18;
		assertEquals(l_expectedresult, l_actualResult);
	}

}