package Models;

import Exceptions.CommandValidationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to test functionality of Deploy order.
 */
public class DeployTest {

	/**
	 * First Player
	 */
	ModelPlayer d_player1;

	/**
	 * Second Player
	 */
	ModelPlayer d_player2;

	/**
	 * First Deploy Order.
	 */
	Deploy d_deployOrder1;

	/**
	 * Second Deploy Order.
	 */
	Deploy d_deployOrder2;

	/**
	 * Game State.
	 */
	GameState d_gameState = new GameState();

	/**
	 * The setup is called before each test case of this class is executed.
	 */
	@Before
	public void setup() {
		d_player1 = new ModelPlayer();
		d_player1.setPlayerName("Anurag");

		d_player2 = new ModelPlayer();
		d_player2.setPlayerName("Harman");

		List<ModelCountry> l_countryList = new ArrayList<ModelCountry>();
		l_countryList.add(new ModelCountry("India"));
		l_countryList.add(new ModelCountry("Canada"));
		d_player1.setD_coutriesOwned(l_countryList);
		d_player2.setD_coutriesOwned(l_countryList);

		List<ModelCountry> l_mapCountries = new ArrayList<ModelCountry>();
		ModelCountry l_country1 = new ModelCountry(1, "Canada", 1);
		ModelCountry l_country2 = new ModelCountry(2, "India", 2);
		l_country2.setD_armies(5);

		l_mapCountries.add(l_country1);
		l_mapCountries.add(l_country2);

		Map l_map = new Map();
		l_map.setD_allCountries(l_mapCountries);
		d_gameState.setD_map(l_map);

		d_deployOrder1 = new Deploy(d_player1, "India", 5);
		d_deployOrder2 = new Deploy(d_player2, "Canada", 15);
	}

	/**
	 * Used to test country name entered by player in deploy command to check if the
	 * entered country name belongs to player or not. If it does not belongs to
	 * player, order will not be executed.
	 */
	@Test
	public void testValidateDeployOrderCountry() {
		boolean l_actualBoolean = d_deployOrder1.valid(d_gameState);
		assertTrue(l_actualBoolean);
		boolean l_actualBoolean2 = d_deployOrder2.valid(d_gameState);
		assertTrue(l_actualBoolean2);
	}

	/**
	 * Used to test execution of deploy order and check if required armies are
	 * deployed at country level or not.
	 */
	@Test
	public void testDeployOrderExecution() {
		d_deployOrder1.execute(d_gameState);
		ModelCountry l_countryIndia = d_gameState.getD_map().getCountryByName("India");
		assertEquals("10", l_countryIndia.getD_armies().toString());

		d_deployOrder2.execute(d_gameState);
		ModelCountry l_countryCanada = d_gameState.getD_map().getCountryByName("Canada");
		assertEquals("15", l_countryCanada.getD_armies().toString());
	}

	/**
	 * Tests deploy order logic to see if required order is created and armies are
	 * re-calculated.
	 * Required Test #4
	 * @throws CommandValidationException if given command is invalid
	 */
	@Test
	public void testDeployOrder() throws CommandValidationException {
		ModelPlayer l_player = new ModelPlayer("Maze");
		l_player.setD_noOfUnallocatedArmies(10);
		ModelCountry l_country = new ModelCountry(1, "Japan", 1);
		l_player.setD_coutriesOwned(Arrays.asList(l_country));

		l_player.createDeployOrder("deploy Japan 4");

		assertEquals(l_player.getD_noOfUnallocatedArmies().toString(), "6");
		assertEquals(l_player.getD_ordersToExecute().size(), 1);
		Deploy l_order = (Deploy) l_player.order_list.get(0);
		assertEquals("Japan", l_order.d_nameOfTargetCountry);
		assertEquals("4", String.valueOf(l_order.d_quantityOfArmiesToPlace));
	}

}
