package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
* The BombTest class contains JUnit test cases for the Bomb class, which represents bomb orders in a game.
*/
public class BombTest {

    /**
     * The game state used for testing bomb orders.
     */
    GameState d_gameState;

    /**
     * The first player for testing bomb orders.
     */
    ModelPlayer d_player1;

    /**
     * The second player for testing bomb orders.
     */
    ModelPlayer d_player2;

    /**
     * The first bomb order for testing.
     */
    ModelBomb d_bombOrder1;

    /**
     * The second bomb order for testing.
     */
    ModelBomb d_bombOrder2;

    /**
     * The third bomb order for testing.
     */
    ModelBomb d_bombOrder3;

    /**
     * The fourth bomb order for testing.
     */
    ModelBomb d_bombOrder4;

    /**
     * The list of orders used for testing bomb orders.
     */
    List<Order> d_order_list;

    /**
     * The target country for bomb orders.
     */
    String d_targetCountry;

    /**
     * A deploy order for testing.
     */
    Order deployOrder;

    /**
     * Set up the initial game state and objects before running the test cases.
     */
	@Before
	public void setup() {
        // Initialize the game state
        d_gameState = new GameState();
        d_order_list = new ArrayList<Order>();
    
        // Create two player objects and set their names
        d_player1 = new ModelPlayer();
        d_player1.setPlayerName("Anurag");
        d_player2 = new ModelPlayer();
        d_player2.setPlayerName("Harman");
    
        // Create a list of countries and assign them to both players
        List<ModelCountry> l_countryList = new ArrayList<ModelCountry>();
        l_countryList.add(new ModelCountry("USA"));
        l_countryList.add(new ModelCountry("Korea"));
        d_player1.setD_coutriesOwned(l_countryList);
        d_player2.setD_coutriesOwned(l_countryList);
    
        // Create a list of countries for the game map
        List<ModelCountry> l_mapCountries = new ArrayList<ModelCountry>();
        // Create individual country objects and set their properties
        ModelCountry l_country1 = new ModelCountry(1, "USA", 1);
        ModelCountry l_country2 = new ModelCountry(2, "Korea", 2);
        ModelCountry l_country3 = new ModelCountry(2, "Japan", 2);
        ModelCountry l_country4 = new ModelCountry(2, "India", 2);
        ModelCountry l_country5 = new ModelCountry(2, "Canada", 2);
    
        // Set the number of armies in specific countries
        l_country3.setD_armies(4);
        l_country4.setD_armies(15);
        l_country5.setD_armies(1);
    
        // Add the country objects to the map's list of countries
        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);
        l_mapCountries.add(l_country3);
        l_mapCountries.add(l_country4);
        l_mapCountries.add(l_country5);
    
        // Create a map and set its list of countries
        Map l_map = new Map();
        l_map.setD_allCountries(l_mapCountries);
        d_gameState.setD_map(l_map);
    
        // Create Bomb orders for player 1 targeting specific countries
        d_bombOrder1 = new ModelBomb(d_player1, "Japan");
        d_bombOrder2 = new ModelBomb(d_player1, "Korea");
        d_bombOrder3 = new ModelBomb(d_player1, "India");
        d_bombOrder4 = new ModelBomb(d_player1, "Canada");
    
        // Add the Bomb orders to the order list
        d_order_list.add(d_bombOrder1);
        d_order_list.add(d_bombOrder2);
    
        // Set the list of orders for player 2
        d_player2.setD_ordersToExecute(d_order_list);
    }

    /**
     * Test the execution of bomb orders, including the calculation of armies after bombing.
     */
    @Test
	public void testBombCardExecution() {
		// Test calculation of half armies.
		d_bombOrder1.execute(d_gameState);
		ModelCountry l_targetCountry = d_gameState.getD_map().getCountryByName("Japan");
		assertEquals("2", l_targetCountry.getD_armies().toString());

		// Test round down of armies calculation.
		d_bombOrder3.execute(d_gameState);
		ModelCountry l_targetCountry2 = d_gameState.getD_map().getCountryByName("India");
		assertEquals("7", l_targetCountry2.getD_armies().toString());

		// Testing:- targeting a territory with 1 army will leave 0.
		d_bombOrder4.execute(d_gameState);
		ModelCountry l_targetCountry3 = d_gameState.getD_map().getCountryByName("Canada");
		assertEquals("0", l_targetCountry3.getD_armies().toString());

	}

     /**
     * Test the validity of bomb orders, ensuring that they meet the required conditions.
     */
    @Test
	public void testValidBombOrder() {

		// Player cannot bomb own territory
		boolean l_actualBoolean = d_bombOrder1.valid(d_gameState);
		assertTrue(l_actualBoolean);

		// fail if target country is owned by player
		boolean l_actualBoolean1 = d_bombOrder2.valid(d_gameState);
		assertFalse(l_actualBoolean1);
	}
}
