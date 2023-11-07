package Models;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

public class BombTest {
    ModelPlayer d_player1;

	ModelPlayer d_player2;

	GameState d_gameState;

	String d_targetCountry;

	Bomb d_bombOrder1;

	Bomb d_bombOrder2;

	Bomb d_bombOrder3;

	Bomb d_bombOrder4;

	Order deployOrder;

	List<Order> d_order_list;

	@Before
	public void setup() {
		d_gameState = new GameState();
		d_order_list = new ArrayList<Order>();

		d_player1 = new ModelPlayer();
		d_player1.setPlayerName("Anurag");
		d_player2 = new ModelPlayer();
		d_player2.setPlayerName("Harman");

		List<ModelCountry> l_countryList = new ArrayList<ModelCountry>();
		l_countryList.add(new ModelCountry("USA"));
		l_countryList.add(new ModelCountry("Korea"));
		d_player1.setD_coutriesOwned(l_countryList);
		d_player2.setD_coutriesOwned(l_countryList);

		List<ModelCountry> l_mapCountries = new ArrayList<ModelCountry>();
		ModelCountry l_country1 = new ModelCountry(1, "USA", 1);
		ModelCountry l_country2 = new ModelCountry(2, "Korea", 2);
		ModelCountry l_country3 = new ModelCountry(2, "Japan", 2);
		ModelCountry l_country4 = new ModelCountry(2, "India", 2);
		ModelCountry l_country5 = new ModelCountry(2, "Canada", 2);

		l_country3.setD_armies(4);
		l_country4.setD_armies(15);
		l_country5.setD_armies(1);

		l_mapCountries.add(l_country1);
		l_mapCountries.add(l_country2);
		l_mapCountries.add(l_country3);
		l_mapCountries.add(l_country4);
		l_mapCountries.add(l_country5);

		Map l_map = new Map();
		l_map.setD_allCountries(l_mapCountries);
		d_gameState.setD_map(l_map);
		d_bombOrder1 = new Bomb(d_player1, "Japan");
		d_bombOrder2 = new Bomb(d_player1, "Norway");
		d_bombOrder3 = new Bomb(d_player1, "India");
		d_bombOrder4 = new Bomb(d_player1, "Canada");

		d_order_list.add(d_bombOrder1);
		d_order_list.add(d_bombOrder2);

		d_player2.setD_ordersToExecute(d_order_list);

	}
}
