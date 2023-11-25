package Models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * This class is used to test functionality of Bomb class functions.
 */
public class ModelBombTest {


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
    ModelBomb d_Model_bombOrder1;

    /**
     * The second bomb order for testing.
     */
    ModelBomb d_Model_bombOrder2;
    /**
     * The third bomb order for testing.
     */
    ModelBomb d_Model_bombOrder3;

    /**
     * The fourth bomb order for testing.
     */
    ModelBomb d_Model_bombOrder4;


    /**
     * A deploy order for testing.
     */
    Order deployOrder;

    /**
     * The target country for bomb orders.
     */
    String d_targetCountry;

    /**
     * The list of orders used for testing bomb orders.
     */
    List<Order> d_order_list;

    /**
     * The game state used for testing bomb orders.
     */
    GameState d_gameState;


    /**
     * Set up the initial game state and objects before running the test cases.
     */
    @Before
    public void setup() {
        d_gameState = new GameState();
        d_order_list = new ArrayList<Order>();

        d_player1 = new ModelPlayer();
        d_player1.setPlayerName("Harman");
        d_player2 = new ModelPlayer();
        d_player2.setPlayerName("Abhishek");

        List<Country> l_countryList = new ArrayList<Country>();
        l_countryList.add(new Country("Finland"));
        l_countryList.add(new Country("Norway"));
        d_player1.setD_coutriesOwned(l_countryList);
        d_player2.setD_coutriesOwned(l_countryList);

        List<Country> l_mapCountries = new ArrayList<Country>();
        Country l_country1 = new Country(1, "Finland", 1);
        Country l_country2 = new Country(2, "Norway", 2);
        Country l_country3 = new Country(2, "Japan", 2);
        Country l_country4 = new Country(2, "India", 2);
        Country l_country5 = new Country(2, "Canada", 2);

        l_country3.setD_armies(4);
        l_country4.setD_armies(15);
        l_country5.setD_armies(1);

        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);
        l_mapCountries.add(l_country3);
        l_mapCountries.add(l_country4);
        l_mapCountries.add(l_country5);

        Map l_map = new Map();
        l_map.setD_countriesList(l_mapCountries);
        d_gameState.setD_map(l_map);
        d_Model_bombOrder1 = new ModelBomb(d_player1, "Japan");
        d_Model_bombOrder2 = new ModelBomb(d_player1, "Norway");
        d_Model_bombOrder3 = new ModelBomb(d_player1, "India");
        d_Model_bombOrder4 = new ModelBomb(d_player1, "Canada");

        d_order_list.add(d_Model_bombOrder1);
        d_order_list.add(d_Model_bombOrder2);

        d_player2.setD_ordersToExecute(d_order_list);

    }

    /**
     * Test the execution of bomb orders, including the calculation of armies after bombing.
     */
    @Test
    public void testBombCardExecution() {
        // Test calculation of half armies.
        d_Model_bombOrder1.execute(d_gameState);
        Country l_targetCountry = d_gameState.getD_map().getCountryByName("Japan");
        assertEquals("2", l_targetCountry.getD_armies().toString());

        // Test round down of armies calculation.
        d_Model_bombOrder3.execute(d_gameState);
        Country l_targetCountry2 = d_gameState.getD_map().getCountryByName("India");
        assertEquals("7", l_targetCountry2.getD_armies().toString());

        // Testing:- targeting a territory with 1 army will leave 0.
        d_Model_bombOrder4.execute(d_gameState);
        Country l_targetCountry3 = d_gameState.getD_map().getCountryByName("Canada");
        assertEquals("0", l_targetCountry3.getD_armies().toString());

    }

    /**
     * Test the validity of bomb orders, ensuring that they meet the required conditions.
     */
    @Test
    public void testValidBombOrder() {

        // Player cannot bomb own territory
        boolean l_actualBoolean = d_Model_bombOrder1.checkValid(d_gameState);
        assertTrue(l_actualBoolean);

        // fail if target country is owned by player
        boolean l_actualBoolean1 = d_Model_bombOrder2.checkValid(d_gameState);
        assertFalse(l_actualBoolean1);
    }

}
