package Controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.CommandValidationException;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Models.Player;
import Utils.CommonUtil;


public class GamePlayerControllerTest {
	
	Player d_playerInfo;

	
	GamePlayerController d_Game_playerController;

	
	Map d_map;

	
	GameState d_gameState;

	
	MapController d_mapservice;

	
	List<Player> d_exisitingPlayerList = new ArrayList<>();

	private final ByteArrayOutputStream d_outContent = new ByteArrayOutputStream();

	
	@Before
	public void setup() {
		d_playerInfo = new Player();
		d_Game_playerController = new GamePlayerController();
		d_gameState = new GameState();
		d_exisitingPlayerList.add(new Player("Rajat"));
		d_exisitingPlayerList.add(new Player("Anurag"));

	}

	
	@Test
	public void testAddPlayers() {
		assertFalse(CommonUtil.isCollectionEmpty(d_exisitingPlayerList));
		List<Player> l_updatedPlayers = d_Game_playerController.addRemovePlayers(d_exisitingPlayerList, "add", "Jhanvi");
		assertEquals("Jhanvi", l_updatedPlayers.get(2).getPlayerName());

		System.setOut(new PrintStream(d_outContent));
		d_Game_playerController.addRemovePlayers(d_exisitingPlayerList, "add", "Rajat");
		assertEquals("Player with name : Rajat already Exists. Changes are not made.", d_outContent.toString().trim());
	}

	
	@Test
	public void testRemovePlayers() {
		List<Player> l_updatedPlayers = d_Game_playerController.addRemovePlayers(d_exisitingPlayerList, "remove", "Rajat");
		assertEquals(1, l_updatedPlayers.size());

		System.setOut(new PrintStream(d_outContent));
		d_Game_playerController.addRemovePlayers(d_exisitingPlayerList, "remove", "Bhoomi");
		assertEquals("Player with name : Bhoomi does not Exist. Changes are not made.", d_outContent.toString().trim());
	}

	
	@Test
	public void testPlayersAvailability() {
		boolean l_playersExists = d_Game_playerController.checkPlayersAvailability(d_gameState);
		assertFalse(l_playersExists);
	}

	
	@Test
	public void testPlayerCountryAssignment() {
		d_mapservice = new MapController();
		d_map = new Map();
		d_map = d_mapservice.loadMap(d_gameState, "canada.map");
		d_gameState.setD_map(d_map);
		d_gameState.setD_players(d_exisitingPlayerList);
		d_Game_playerController.assignCountries(d_gameState);

		int l_assignedCountriesSize = 0;
		for (Player l_pl : d_gameState.getD_players()) {
			assertNotNull(l_pl.getD_coutriesOwned());
			l_assignedCountriesSize = l_assignedCountriesSize + l_pl.getD_coutriesOwned().size();
		}
		assertEquals(l_assignedCountriesSize, d_gameState.getD_map().getD_countries().size());
	}

	
	@Test
	public void testCalculateArmiesForPlayer() {
		Player l_playerInfo = new Player();
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
		Integer l_actualResult = d_Game_playerController.calculateArmiesForPlayer(l_playerInfo);
		Integer l_expectedresult = 8;
		assertEquals(l_expectedresult, l_actualResult);
	}

	
	@Test
	public void testValidateDeployOrderArmies() {
		d_playerInfo.setD_noOfUnallocatedArmies(10);
		String l_noOfArmies = "4";
		boolean l_bool = d_Game_playerController.validateDeployOrderArmies(d_playerInfo, l_noOfArmies);
		assertFalse(l_bool);

	}

	
	@Test
	public void testDeployOrder() throws CommandValidationException {
		Player l_player = new Player("Maze");
		l_player.setD_noOfUnallocatedArmies(10);
		Country l_country = new Country(1, "Japan", 1);
		l_player.setD_coutriesOwned(Arrays.asList(l_country));
		d_Game_playerController.createDeployOrder("deploy Japan 4", l_player);

		assertEquals(l_player.getD_noOfUnallocatedArmies().toString(), "6");
		assertEquals(l_player.getD_ordersToExecute().size(), 1);
		assertEquals(l_player.getD_ordersToExecute().get(0).getD_targetCountryName(), "Japan");
		assertEquals(l_player.getD_ordersToExecute().get(0).getD_numberOfArmiesToPlace().toString(), "4");
	}
}