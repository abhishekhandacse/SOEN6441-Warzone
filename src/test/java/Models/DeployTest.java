package Models;

import Exceptions.CommandValidationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for the {@code ModelDeploy} class.
 *
 * @version 1.0
 */
public class DeployTest {

    /**
     * Player 1 for testing.
     */
    ModelPlayer d_player1;

    /**
     * Player 2 for testing.
     */
    ModelPlayer d_player2;

    /**
     * Deploy order for Player 1.
     */
    ModelDeploy d_deployOrder1;

    /**
     * Deploy order for Player 2.
     */
    ModelDeploy d_deployOrder2;

    /**
     * Game state used for testing.
     */
    GameState d_gameState = new GameState();

    /**
     * Sets up the test environment before each test method is executed.
     */
    @Before
    public void setup() {
        // Setting up players
        d_player1 = new ModelPlayer();
        d_player1.setPlayerName("Rajat");

        d_player2 = new ModelPlayer();
        d_player2.setPlayerName("Anurag");

        // Setting up countries for players
        List<Country> l_countryList = new ArrayList<>();
        l_countryList.add(new Country("India"));
        l_countryList.add(new Country("Canada"));
        d_player1.setD_coutriesOwned(l_countryList);
        d_player2.setD_coutriesOwned(l_countryList);

        // Setting up countries for the game map
        List<Country> l_mapCountries = new ArrayList<>();
        Country l_country1 = new Country(1, "Canada", 1);
        Country l_country2 = new Country(2, "India", 2);
        l_country2.setD_armies(5);

        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);

        // Setting up the game map
        Map l_map = new Map();
        l_map.setD_countries(l_mapCountries);
        d_gameState.setD_map(l_map);

        // Creating deploy orders
        d_deployOrder1 = new ModelDeploy(d_player1, "India", 5);
        d_deployOrder2 = new ModelDeploy(d_player2, "Canada", 15);
    }

    /**
     * Tests the {@code checkValid} method for deploy orders.
     * It asserts that the deployment orders are valid for the respective players.
     */
    @Test
    public void testValidateDeployOrderCountry() {
        assertTrue(d_deployOrder1.checkValid(d_gameState));
        assertTrue(d_deployOrder2.checkValid(d_gameState));
    }

    /**
     * Tests the execution of deploy orders.
     * It asserts that the armies are correctly deployed to the specified countries.
     */
    @Test
    public void testDeployOrderExecution() {
        d_deployOrder1.execute(d_gameState);
        Country l_countryIndia = d_gameState.getD_map().getCountryByName("India");
        assertEquals("10", l_countryIndia.getD_armies().toString());

        d_deployOrder2.execute(d_gameState);
        Country l_countryCanada = d_gameState.getD_map().getCountryByName("Canada");
        assertEquals("15", l_countryCanada.getD_armies().toString());
    }

    /**
     * Tests the creation of deploy orders by a player.
     *
     * @throws CommandValidationException if the deploy command is invalid
     */
    @Test
    public void testDeployOrder() throws CommandValidationException {
        ModelPlayer l_player = new ModelPlayer("Maze");
        l_player.setD_noOfUnallocatedArmies(10);
        Country l_country = new Country(1, "Japan", 1);
        l_player.setD_coutriesOwned(Arrays.asList(l_country));

        l_player.createDeployOrder("deploy Japan 4");

        assertEquals("6", l_player.getD_noOfUnallocatedArmies().toString());
        assertEquals(1, l_player.getD_ordersToExecute().size());
        ModelDeploy l_order = (ModelDeploy) l_player.d_orderList.get(0);
        assertEquals("Japan", l_order.d_targetCountryName);
        assertEquals("4", String.valueOf(l_order.d_numberOfArmiesToPlace));
    }
}
