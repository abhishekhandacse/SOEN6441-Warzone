package Models;

import Exceptions.InvalidMap;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DiplomacyTest {

    ModelPlayer d_player1;

    ModelPlayer d_player2;

    Bomb d_bombOrder;

    Diplomacy d_diplomacyOrder;

    GameState d_gameState;

    @Before
    public void setup() throws InvalidMap {
        d_gameState = new GameState();
        d_player1 = new ModelPlayer();
        d_player1.setPlayerName("Amanpreet");
        d_player2 = new ModelPlayer("Rajat");


        List<ModelCountry> l_countryList = new ArrayList<ModelCountry>();
        ModelCountry l_country = new ModelCountry(0, "France", 1);
        l_country.setD_armies(9);
        l_countryList.add(l_country);

        ModelCountry l_countryNeighbour = new ModelCountry(1, "Belgium", 1);
        l_countryNeighbour.addNeighbour(0);
        l_country.addNeighbour(1);
        l_countryNeighbour.setD_armies(10);
        l_countryList.add(l_countryNeighbour);

        List<ModelCountry> l_countryList2 = new ArrayList<ModelCountry>();
        ModelCountry l_countryNotNeighbour = new ModelCountry(2, "Spain", 1);
        l_countryNotNeighbour.setD_armies(15);
        l_countryList2.add(l_countryNotNeighbour);

        Map l_map = new Map();
        l_map.setD_allCountries(new ArrayList<ModelCountry>(){{ addAll(l_countryList); addAll(l_countryList2); }});

        d_gameState.setD_map(l_map);
        d_player1.setD_coutriesOwned(l_countryList);
        d_player2.setD_coutriesOwned(l_countryList2);
        List<ModelPlayer> l_playerList = new ArrayList<ModelPlayer>();
        l_playerList.add(d_player1);
        l_playerList.add(d_player2);
        d_gameState.setD_players(l_playerList);
        d_diplomacyOrder = new Diplomacy(d_player2.getPlayerName(), d_player1);
        d_bombOrder = new Bomb(d_player2, "France");
    }

    @Test
    public void testDiplomacyExecution(){
        d_diplomacyOrder.execute(d_gameState);
        assertEquals(d_player1.d_negotiatedWith.get(0), d_player2);
    }

    @Test
    public void NegotiationWorking(){
        d_diplomacyOrder.execute(d_gameState);
        d_bombOrder.execute(d_gameState);
        assertEquals(d_gameState.getRecentLog().trim(), "Log: Bomb card order : bomb France is not executed as b has negotiation pact with the target country's player!");
    }
}
