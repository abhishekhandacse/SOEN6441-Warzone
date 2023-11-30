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
 * Test class for the {@code PlayerService} class.
 *
 * @version 1.0
 */
public class PlayerServiceTest {

    /**
     * Player information for testing.
     */
    ModelPlayer d_playerInfo;

    /**
     * Player service for testing.
     */
    PlayerService d_playerService;

    /**
     * Map for testing.
     */
    Map d_map;

    /**
     * Game state for testing.
     */
    GameState d_gameState;

    /**
     * Map service for testing.
     */
    MapService d_mapservice;

    /**
     * Existing player list for testing.
     */
    List<ModelPlayer> d_exisitingPlayerList = new ArrayList<>();

    /**
     * Output content for testing.
     */
    private final ByteArrayOutputStream d_outContent = new ByteArrayOutputStream();

    /**
     * Sets up the test environment before each test method is executed.
     */
    @Before
    public void setup() {
        d_playerInfo = new ModelPlayer();
        d_playerService = new PlayerService();
        d_gameState = new GameState();
        d_exisitingPlayerList.add(new ModelPlayer("Rajat"));
        d_exisitingPlayerList.add(new ModelPlayer("Anurag"));
    }

    /**
     * Tests the addition of players.
     */
    @Test
    public void testAddPlayers() throws IOException{
        assertFalse(CommonUtil.isCollectionEmpty(d_exisitingPlayerList));
        List<ModelPlayer> l_updatedPlayers = d_playerService.addRemovePlayers(d_exisitingPlayerList, "add", "Aman");
        assertEquals("Aman", l_updatedPlayers.get(2).getPlayerName());

        System.setOut(new PrintStream(d_outContent));
        d_playerService.addRemovePlayers(d_exisitingPlayerList, "add", "Rajat");
        assertEquals("Player with name : Rajat already Exists. Changes are not made.", d_outContent.toString().trim());
    }

    /**
     * Tests the removal of players.
     */
    @Test
    public void testRemovePlayers() throws IOException{
        List<ModelPlayer> l_updatedPlayers = d_playerService.addRemovePlayers(d_exisitingPlayerList, "remove", "Rajat");
        assertEquals(1, l_updatedPlayers.size());

        System.setOut(new PrintStream(d_outContent));
        d_playerService.addRemovePlayers(d_exisitingPlayerList, "remove", "Abhishek");
        assertEquals("Player with name : Abhishek does not Exist. Changes are not made.", d_outContent.toString().trim());
    }

    /**
     * Tests the availability of players.
     */
    @Test
    public void testPlayersAvailability() {
        boolean l_playersExists = d_playerService.checkPlayersAvailability(d_gameState);
        assertFalse(l_playersExists);
    }

    /**
     * Tests the assignment of countries to players.
     *
     * @throws MapValidationException if an invalid map is encountered
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
     * Tests the calculation of armies for a player.
     */
    @Test
    public void testCalculateArmiesForPlayer() {
        ModelPlayer l_playerInfo = new ModelPlayer();
        List<Country> l_countryList = new ArrayList<>();
        l_countryList.add(new Country("Waadt"));
        l_countryList.add(new Country("Neuenburg"));
        l_countryList.add(new Country("Fribourg"));
        l_countryList.add(new Country("Geneve"));
        l_playerInfo.setD_coutriesOwned(l_countryList);
        List<Continent> l_continentList = new ArrayList<>();
        l_continentList.add(new Continent(1, "Asia", 5));
        l_playerInfo.setD_continentsOwned(l_continentList);
        l_playerInfo.setD_noOfUnallocatedArmies(10);
        Integer l_actualResult = d_playerService.calculateArmiesForPlayer(l_playerInfo);
        Integer l_expectedresult = 18;
        assertEquals(l_expectedresult, l_actualResult);
    }
}
