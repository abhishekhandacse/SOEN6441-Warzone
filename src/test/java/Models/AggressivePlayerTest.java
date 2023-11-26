package Models;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AggressivePlayerTest {

	GameState d_gameState = new GameState();

	ModelPlayer d_player;

	Country d_country1;

	PlayerBehaviorStrategy d_playerBehaviorStrategy;

	AggressivePlayer d_aggressivePlayer = new AggressivePlayer();

	
	@Before
	public void setup() {
		this.d_country1 = new Country(1, "India", 1);
		Country l_country2 = new Country(1, "Pakistan", 1);
		Country l_country3 = new Country(1, "Portugal", 1);

		l_country2.setD_countryId(3);
		d_country1.addNeighbours(3);

		l_country3.setD_countryId(2);
		d_country1.addNeighbours(2);

		this.d_country1.setD_armies(10);
		l_country2.setD_armies(3);
		l_country3.setD_armies(2);

		ArrayList<Country> l_list = new ArrayList<Country>();
		l_list.add(d_country1);
		l_list.add(l_country2);
		l_list.add(l_country3);

		d_playerBehaviorStrategy = new AggressivePlayer();
		d_player = new ModelPlayer("Anurag");
		d_player.setD_coutriesOwned(l_list);
		d_player.setStrategy(d_playerBehaviorStrategy);
		d_player.setD_noOfUnallocatedArmies(8);

		List<ModelPlayer> l_listOfPlayer = new ArrayList<ModelPlayer>();
		l_listOfPlayer.add(d_player);

		Map l_map = new Map();
		l_map.setD_countries(l_list);
		l_map.setD_countries(l_list);
		d_gameState.setD_map(l_map);
		d_gameState.setD_players(l_listOfPlayer);

	}

	@Test
	public void testOrderCreation() throws IOException {
		assertEquals("deploy", d_player.getPlayerOrder(d_gameState).split(" ")[0]);
	}

	@Test
	public void testStrongestCountry() {
		assertEquals("India", d_aggressivePlayer.getStrongestCountry(d_player, d_gameState).getD_countryName());
	}

}
