package Models;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the {@code BenevolentPlayer} behavior strategy.
 * It includes tests for methods that determine the weakest country and weakest neighbor.
 *
 * @version 1.0
 */
public class BenevolentPlayerTest {

    /**
     * The player under test.
     */
    ModelPlayer d_player;

    /**
     * The player behavior strategy being tested.
     */
    PlayerBehaviorStrategy d_playerBehaviorStrategy;

    /**
     * The instance of BenevolentPlayer.
     */
    BenevolentPlayer d_benevolentPlayer = new BenevolentPlayer();

    /**
     * The game state used for testing.
     */
    GameState d_gameState = new GameState();

    /**
     * The country used for testing.
     */
    Country d_country1;

    /**
     * Sets up the test environment before each test method is executed.
     */
    @Before
    public void setup() {
        this.d_country1 = new Country(1, "India", 1);
        Country l_country2 = new Country(2, "China", 1);
        Country l_country3 = new Country(3, "Pakistan", 1);

        // Set up countries, neighbors, and armies for testing
        l_country2.setD_countryId(3);
        d_country1.addNeighbours(3);
        l_country3.setD_countryId(2);
        d_country1.addNeighbours(2);
        this.d_country1.setD_armies(10);
        l_country2.setD_armies(3);
        l_country3.setD_armies(2);

        // Setting up the list of countries
        List<Country> l_list = new ArrayList<>();
        l_list.add(d_country1);
        l_list.add(l_country2);
        l_list.add(l_country3);

        // Setting up the player and its properties
        d_playerBehaviorStrategy = new BenevolentPlayer();
        d_player = new ModelPlayer("Rajat");
        d_player.setD_coutriesOwned(l_list);
        d_player.setStrategy(d_playerBehaviorStrategy);
        d_player.setD_noOfUnallocatedArmies(5);

        // Setting up the list of players
        List<ModelPlayer> l_listOfPlayer = new ArrayList<>();
        l_listOfPlayer.add(d_player);

        // Setting up the game map
        Map l_map = new Map();
        l_map.setD_countries(l_list);
        l_map.setD_countries(l_list); // Is this line necessary?
        d_gameState.setD_map(l_map);
        d_gameState.setD_players(l_listOfPlayer);
    }

    /**
     * Tests the {@code getWeakestCountry} method of the {@code BenevolentPlayer}.
     * It asserts that the weakest country is correctly identified.
     */
    @Test
    public void testWeakestCountry() {
        assertEquals("Pakistan", d_benevolentPlayer.getWeakestCountry(d_player).getD_countryName());
    }

    /**
     * Tests the {@code getWeakestNeighbor} method of the {@code BenevolentPlayer}.
     * It asserts that the weakest neighbor of a country is correctly identified.
     */
    @Test
    public void testWeakestNeighbor() {
        assertEquals("Pakistan", d_benevolentPlayer.getWeakestNeighbor(d_country1, d_gameState, d_player).getD_countryName());
    }
}
