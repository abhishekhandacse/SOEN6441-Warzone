package Models;

import Exceptions.MapValidationException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class is responsible for testing the Diplomacy order in the game.
 */
public class DiplomacyTest {

    /**
     * The first player object.
     */
    ModelPlayer d_player1;

    /**
     * The second player object
     */
    ModelPlayer d_player2;

    /**
     * The Bomb order object.
     */
    ModelBomb d_bombOrder;

    /**
     * The Diplomacy order object.
     */
    Diplomacy d_diplomacyOrder;

    /**
     *  The game state object.
     */
    GameState d_gameState;

    /**
     * Setup method to initialize the test environment and create necessary objects.
     *
     * @throws MapValidationException if there is a map validation error.
     */
    @Before
    public void setup() throws MapValidationException {
        d_gameState = new GameState();
        d_player1 = new ModelPlayer();
        d_player1.setPlayerName("Amanpreet");
        d_player2 = new ModelPlayer("Rajat");


        List<Country> l_countryList = new ArrayList<Country>();
        Country l_country = new Country(0, "France", 1);
        l_country.setD_armies(9);
        l_countryList.add(l_country);

        Country l_countryNeighbour = new Country(1, "Belgium", 1);
        l_countryNeighbour.addNeighbour(0);
        l_country.addNeighbour(1);
        l_countryNeighbour.setD_armies(10);
        l_countryList.add(l_countryNeighbour);

        List<Country> l_countryList2 = new ArrayList<Country>();
        Country l_countryNotNeighbour = new Country(2, "Spain", 1);
        l_countryNotNeighbour.setD_armies(15);
        l_countryList2.add(l_countryNotNeighbour);

        Map l_map = new Map();
        l_map.setD_allCountries(new ArrayList<Country>(){{ addAll(l_countryList); addAll(l_countryList2); }});

        d_gameState.setD_map(l_map);
        d_player1.setD_coutriesOwned(l_countryList);
        d_player2.setD_coutriesOwned(l_countryList2);
        List<ModelPlayer> l_playerList = new ArrayList<ModelPlayer>();
        l_playerList.add(d_player1);
        l_playerList.add(d_player2);
        d_gameState.setD_playersList(l_playerList);
        d_diplomacyOrder = new Diplomacy(d_player2.getPlayerName(), d_player1);
        d_bombOrder = new ModelBomb(d_player2, "France");
    }

    /**
     * Test the execution of the Diplomacy order and verify if a negotiation pact is formed.
     */
    @Test
    public void testDiplomacyExecution(){
        d_diplomacyOrder.execute(d_gameState);
        assertEquals(d_player1.d_negotiatedWith.get(0), d_player2);
    }

    /**
     * Test if Diplomacy effectively prevents the execution of a Bomb order.
     */
    @Test
    public void NegotiationWorking(){
        d_diplomacyOrder.execute(d_gameState);
        d_bombOrder.execute(d_gameState);
        assertEquals(d_gameState.getRecentLog().trim(), "Log: Bomb card order : bomb France is not executed as Rajat has negotiation pact with the target country's player!");
    }
}
