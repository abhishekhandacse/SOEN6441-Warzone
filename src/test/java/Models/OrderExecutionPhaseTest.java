package Models;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Controllers.GameEngine;
import Exceptions.MapValidationException;

public class OrderExecutionPhaseTest {
    ModelPlayer d_player1;

	ModelPlayer d_player2;

	GameState d_gameState;

	@Before
	public void setup() throws MapValidationException {
		d_gameState = new GameState();
		d_player1 = new ModelPlayer();
		d_player1.setPlayerName("Anurag");
		d_player2 = new ModelPlayer();
		d_player2.setPlayerName("Harman");

		List<ModelCountry> l_countryList = new ArrayList<ModelCountry>();
		ModelCountry l_country = new ModelCountry(0, "China", 1);
		l_country.setD_armies(14);
		l_countryList.add(l_country);

		ModelCountry l_countryNeighbour = new ModelCountry(1, "Japan", 1);
		l_countryNeighbour.addNeighbour(0);
		l_country.addNeighbour(1);
		l_countryNeighbour.setD_armies(20);
		l_countryList.add(l_countryNeighbour);

		d_player1.setD_coutriesOwned(l_countryList);

		Map l_map = new Map();
		l_map.setD_allCountries(l_countryList);
		d_gameState.setD_map(l_map);
		d_gameState.setD_playersList(Arrays.asList(d_player1, d_player2));
	}

    @Test
	public void testEndOfTheGame() {
		OrderExecutionPhase l_orderExec = new OrderExecutionPhase(new GameEngine(), d_gameState);
		assertTrue(l_orderExec.checkEndOftheGame(d_gameState));
	}
}
