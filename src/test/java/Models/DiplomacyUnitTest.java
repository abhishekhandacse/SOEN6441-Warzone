package Models;

import Exceptions.MapValidationException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Deplomacy test class.
 *
 */
public class DiplomacyUnitTest {
    /**
     * country to shift armies from.
     */
    Airlift d_invalidAirLift1;

    /**
     * Game State.
     */
    GameState d_gameState;

    /**
     * Bomb Order.
     */
    ModelBomb d_Model_bombOrder;


    /**
     * Second Player
     */
    ModelPlayer d_player2;
    /**
     * First Player.
     */
    ModelPlayer d_player1;


    /**
     * diplomacy order.
     */
    Diplomacy d_diplomacyOrder;




    /**
     * Setup before the tests.
     *
     * @throws MapValidationException Exception
     */
    @Before
    public void setup() throws MapValidationException {
        d_gameState = new GameState();
        d_player1 = new ModelPlayer();
        d_player2 = new ModelPlayer("b");
        d_player1.setPlayerName("a");


        List<Country> l_countryList = new ArrayList<Country>();
        Country l_country = new Country(0, "France", 1);
        l_country.setD_armies(9);
        l_countryList.add(l_country);

        Country l_countryNeighbour = new Country(1, "Belgium", 1);
        l_countryNeighbour.addNeighbours(0);
        l_country.addNeighbours(1);
        l_countryNeighbour.setD_armies(10);
        l_countryList.add(l_countryNeighbour);

        List<Country> l_countryList2 = new ArrayList<Country>();
        Country l_countryNotNeighbour = new Country(2, "Spain", 1);
        l_countryNotNeighbour.setD_armies(15);
        l_countryList2.add(l_countryNotNeighbour);

        Map l_map = new Map();
        l_map.setD_countries(new ArrayList<Country>(){{ addAll(l_countryList); addAll(l_countryList2); }});

        d_gameState.setD_map(l_map);
        d_player2.setD_coutriesOwned(l_countryList2);
        d_player1.setD_coutriesOwned(l_countryList);

        List<ModelPlayer> l_playerList = new ArrayList<ModelPlayer>();
        l_playerList.add(d_player2);
        l_playerList.add(d_player1);
        d_gameState.setD_players(l_playerList);
        d_diplomacyOrder = new Diplomacy(d_player2.getPlayerName(), d_player1);
        d_Model_bombOrder = new ModelBomb(d_player2, "France");
    }

    /**
     * Tests if diplomacy works.
     */
    @Test
    public void testDiplomacyExecution(){
        d_diplomacyOrder.execute(d_gameState);
        assertEquals(d_player1.d_negotiatedWith.get(0), d_player2);
    }

    /**
     * Tests the next orders after negotiation if they work.
     */
    @Test
    public void NegotiationWorking(){
        d_Model_bombOrder.execute(d_gameState);
        d_diplomacyOrder.execute(d_gameState);
        assertEquals( "Log: Negotiation with b approached by a successful!",d_gameState.getRecentLog().trim());
    }
}
