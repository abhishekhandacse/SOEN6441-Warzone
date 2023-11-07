package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class ModelAdvanceTest {
	
	GameState d_gameState = new GameState();
	
	@Test
	public void testInvalidAdvanceOrder() {
		ModelPlayer l_player = new ModelPlayer("Zalak");
		ModelCountry l_country1 = new ModelCountry("India");
		l_country1.setD_armies(12);
		ModelCountry l_country2 = new ModelCountry("Canada");
		l_country2.setD_armies(10);
		List<ModelCountry> l_countries = Arrays.asList(l_country1, l_country2);
		l_player.setD_coutriesOwned(l_countries);

		assertFalse(new ModelAdvance(l_player, "India", "France", 15).valid(d_gameState));
		assertFalse(new ModelAdvance(l_player, "Canada", "France", 10).valid(d_gameState));
		assertFalse(new ModelAdvance(l_player, "Italy", "France", 10).valid(d_gameState));
		assertTrue(new ModelAdvance(l_player, "India", "France", 10).valid(d_gameState));
	}

	
	@Test
	public void testAttackersWin() {
		ModelPlayer l_sourcePlayer = new ModelPlayer("Zalak");
		ModelCountry l_country1 = new ModelCountry("India");
		l_country1.setD_armies(7);
		List<ModelCountry> l_s1 = new ArrayList<>();
		l_s1.add(l_country1);
		l_sourcePlayer.setD_coutriesOwned(l_s1);

		ModelPlayer l_targetPlayer = new ModelPlayer("Mike");
		ModelCountry l_country2 = new ModelCountry("Canada");
		l_country2.setD_armies(4);
		List<ModelCountry> l_s2 = new ArrayList<>();
		l_s2.add(l_country2);
		l_targetPlayer.setD_coutriesOwned(l_s2);

		ModelAdvance l_advance = new ModelAdvance(l_sourcePlayer, "India", "Canada", 5);
		l_advance.handleSurvivingArmies(5, 0, l_country1, l_country2, l_targetPlayer);

		assertEquals(l_targetPlayer.getD_coutriesOwned().size(), 0);
		assertEquals(l_sourcePlayer.getD_coutriesOwned().size(), 2);
		assertEquals(l_sourcePlayer.getD_coutriesOwned().get(1).getD_armies().toString(), "5");
	}

	
	@Test
	public void testDefendersWin() {
		ModelPlayer l_sourcePlayer = new ModelPlayer("Zalak");
		ModelCountry l_country1 = new ModelCountry("India");
		l_country1.setD_armies(2);
		List<ModelCountry> l_s1 = new ArrayList<>();
		l_s1.add(l_country1);
		l_sourcePlayer.setD_coutriesOwned(l_s1);

		ModelPlayer l_targetPlayer = new ModelPlayer("Mike");
		ModelCountry l_country2 = new ModelCountry("Canada");
		l_country2.setD_armies(4);
		List<ModelCountry> l_s2 = new ArrayList<>();
		l_s2.add(l_country2);
		l_targetPlayer.setD_coutriesOwned(l_s2);

		ModelAdvance l_advance = new ModelAdvance(l_sourcePlayer, "India", "Canada", 5);
		l_advance.handleSurvivingArmies(3, 2, l_country1, l_country2, l_targetPlayer);

		assertEquals(l_targetPlayer.getD_coutriesOwned().size(), 1);
		assertEquals(l_sourcePlayer.getD_coutriesOwned().size(), 1);
		assertEquals(l_sourcePlayer.getD_coutriesOwned().get(0).getD_armies().toString(), "5");
		assertEquals(l_targetPlayer.getD_coutriesOwned().get(0).getD_armies().toString(), "2");
	}
	
	@Test
	public void testDeployToTarget() {
		ModelPlayer l_sourcePlayer = new ModelPlayer("Zalak");
		List<ModelCountry> l_s1 = new ArrayList<>();
		
		ModelCountry l_country1 = new ModelCountry("India");
		l_country1.setD_armies(7);
		l_s1.add(l_country1);

		ModelCountry l_country2 = new ModelCountry("Canada");
		l_country2.setD_armies(4);
		l_s1.add(l_country2);
		l_sourcePlayer.setD_coutriesOwned(l_s1);

		ModelAdvance l_advance = new ModelAdvance(l_sourcePlayer, "India", "Canada", 3);
		l_advance.deployArmiesToTarget(l_country2);
		assertEquals(l_country2.getD_armies().toString(), "7");
	}
}