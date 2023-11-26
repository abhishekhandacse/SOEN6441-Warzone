package Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the {@code RandomPlayer} class.
 *
 * @version 1.0
 */
public class RandomPlayerTest {

    /**
     * Player for testing.
     */
    ModelPlayer d_player;

    /**
     * Player behavior strategy for testing.
     */
    PlayerBehaviorStrategy d_playerBehaviorStrategy;

    /**
     * Game state used for testing.
     */
    GameState d_gameState = new GameState();

    /**
     * Sets up the test environment before each test method is executed.
     */
    @Before
    public void setup() {
        // Setting up a continent and countries for the player
        Continent l_continent = new Continent("Asia");

        Country l_country1 = new Country(1, "India", 1);
        Country l_country2 = new Country(2, "China", 1);
        ArrayList<Country> l_list = new ArrayList<>();
        l_list.add(l_country1);
        l_list.add(l_country2);

        // Setting up player behavior strategy, player, and game state
        d_playerBehaviorStrategy = new RandomPlayer();
        d_player = new ModelPlayer();
        d_player.setD_coutriesOwned(l_list);
        d_player.setStrategy(d_playerBehaviorStrategy);
        d_player.setD_noOfUnallocatedArmies(1);
        List<ModelPlayer> l_listOfPlayer = new ArrayList<>();
        l_listOfPlayer.add(d_player);

        Map l_map = new Map();
        l_map.setD_countries(l_list);
        d_gameState.setD_map(l_map);
        d_gameState.setD_players(l_listOfPlayer);
    }

    /**
     * Tests the creation of a deploy order by a random player.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testOrderCreation() throws IOException {
        assertEquals("deploy", d_player.getPlayerOrder(d_gameState).split(" ")[0]);
    }
}
