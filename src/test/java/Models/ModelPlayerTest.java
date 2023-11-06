package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class ModelPlayerTest {
	
	List<ModelPlayer> d_exisitingPlayerList = new ArrayList<>();
	
	List<Order> d_order_list = new ArrayList<Order>();

	
	ModelPlayer d_player = new ModelPlayer();

	
	GameState l_gs = new GameState();
	
	@Before
	public void setup() {
		d_exisitingPlayerList.add(new ModelPlayer("Avneet"));
		d_exisitingPlayerList.add(new ModelPlayer("Zalak"));
		
		Map l_map = new Map();
		ModelCountry l_c1 = new ModelCountry(1, "Finland", 10);
		l_c1.setD_adjacentCountryIds(Arrays.asList(2));
		ModelCountry l_c2 = new ModelCountry(2, "France", 10);
		List<ModelCountry> l_countryList = new ArrayList<>();
		l_countryList.add(l_c1);
		l_countryList.add(l_c2);
		l_map.setD_allCountries(l_countryList);
		l_gs.setD_map(l_map);
	}

	
	@Test
	public void testNextOrder() {

		Order l_deployOrder1 = new Deploy(d_exisitingPlayerList.get(0), "India", 5);
		Order l_deployOrder2 = new Deploy(d_exisitingPlayerList.get(1), "Finland", 6);

		d_order_list.add(l_deployOrder1);
		d_order_list.add(l_deployOrder2);

		d_exisitingPlayerList.get(0).setD_ordersToExecute(d_order_list);
		d_exisitingPlayerList.get(1).setD_ordersToExecute(d_order_list);

		Order l_order = d_exisitingPlayerList.get(0).next_order();
		assertEquals(l_deployOrder1, l_order);
		assertEquals(1, d_exisitingPlayerList.get(0).getD_ordersToExecute().size());
	}

	
	@Test
	public void testValidateDeployOrderArmies() {
		d_player.setD_noOfUnallocatedArmies(10);
		String l_noOfArmies = "4";
		boolean l_bool = d_player.validateDeployOrderArmies(d_player, l_noOfArmies);
		assertFalse(l_bool);
	}

	
	@Test
	public void testCreateDeployOrder() {
		ModelPlayer l_pl = new ModelPlayer("abc");
		l_pl.setD_noOfUnallocatedArmies(20);
		l_pl.createDeployOrder("Deploy India 5");
		assertEquals(l_pl.getD_noOfUnallocatedArmies().toString(), "15");
		assertEquals(l_pl.getD_ordersToExecute().size(), 1);
	}

	
	@Test
	public void testCountryExists() {		
		ModelPlayer l_player = new ModelPlayer("abc");
		assertTrue(l_player.checkAdjacency(l_gs, "Finland", "France"));
		assertFalse(l_player.checkAdjacency(l_gs, "France", "Finland"));
	}
	
	
	@Test
	public void testCreateAdvanceOrder() {
		ModelPlayer l_player = new ModelPlayer("xyz");
		l_player.createAdvanceOrder("advance Finland France 10", l_gs);
		assertEquals(l_player.getD_ordersToExecute().size(), 1);
	}
	
	@Test
	public void testCreateAdvanceOrderFailure() {
		ModelPlayer l_player = new ModelPlayer("xyz");
		l_player.createAdvanceOrder("advance Finland France 0", l_gs);
		assertEquals(l_player.getD_ordersToExecute().size(), 0);
	}
}
