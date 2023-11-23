package Models;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Controllers.GameEngine;
import Exceptions.MapValidationException;

/**
 * The OrderExecutionPhaseTest class contains JUnit test cases for the OrderExecutionPhase class,
 * which is responsible for the execution phase of game orders.
 */
public class OrderExecutionPhaseTest {
    /**
     * The first player for testing order execution.
     */
    ModelPlayer d_player1;

    /**
     * The second player for testing order execution.
     */
    ModelPlayer d_player2;

    /**
     * The game state used for testing order execution.
     */
    GameState d_gameState;

    /**
     * Set up the initial game state and objects before running the test cases.
     *
     * @throws MapValidationException if there is an issue with map validation.
     */
    @Before
    public void setup() throws MapValidationException {
        // Create a new game state
        d_gameState = new GameState();

        // Create the first player and set their name
        d_player1 = new ModelPlayer();
        d_player1.setPlayerName("Anurag");

        // Create the second player and set their name
        d_player2 = new ModelPlayer();
        d_player2.setPlayerName("Harman");

        // Create a list of countries owned by the first player
        List<Country> l_countryList = new ArrayList<Country>();

        // Create a country and set its properties
        Country l_country = new Country(0, "China", 1);
        l_country.setD_armies(14);
        l_countryList.add(l_country);

        // Create a neighboring country
        Country l_countryNeighbour = new Country(1, "Japan", 1);
        l_countryNeighbour.addNeighbour(0);
        l_country.addNeighbour(1);
        l_countryNeighbour.setD_armies(20);
        l_countryList.add(l_countryNeighbour);

        // Set the list of countries owned by the first player
        d_player1.setD_coutriesOwned(l_countryList);

        // Create a map and set its list of countries
        Map l_map = new Map();
        l_map.setD_allCountries(l_countryList);
        d_gameState.setD_map(l_map);

        // Set the list of players in the game state
        d_gameState.setD_playersList(Arrays.asList(d_player1, d_player2));
    }

    /**
     * Required Test #5
     * Test the checkEndOftheGame method in the OrderExecutionPhase class.
     */
    @Test
    public void testEndOfTheGame() {
        // Create an instance of the `OrderExecutionPhase` class with a new `GameEngine` and the game state
        OrderExecutionPhase l_orderExec = new OrderExecutionPhase(new GameEngine(), d_gameState);

        // Check if the end of the game is reached
        assertTrue(l_orderExec.checkEndOftheGame(d_gameState));
    }
}
