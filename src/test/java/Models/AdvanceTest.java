package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class AdvanceTest {

    GameState d_gameState = new GameState();

    @Test
    public void testInvalidAdvanceOrder() {
        ModelPlayer l_player = new ModelPlayer("Amanpreet");
        Country l_country1 = new Country("India");
        l_country1.setD_armies(12);
        Country l_country2 = new Country("Canada");
        l_country2.setD_armies(10);
        List<Country> l_countries = Arrays.asList(l_country1, l_country2);
        l_player.setD_coutriesOwned(l_countries);

        assertFalse(new ModelAdvance(l_player, "India", "France", 15).checkValid(d_gameState));
        assertFalse(new ModelAdvance(l_player, "Canada", "France", 10).checkValid(d_gameState));
        assertFalse(new ModelAdvance(l_player, "Italy", "France", 10).checkValid(d_gameState));
        assertTrue(new ModelAdvance(l_player, "India", "France", 10).checkValid(d_gameState));
    }

    @Test
    public void testAttackersWin() {
        ModelPlayer l_sourcePlayer = new ModelPlayer("Amanpreet");
        Country l_country1 = new Country("India");
        l_country1.setD_armies(7);
        List<Country> l_s1 = new ArrayList<>();
        l_s1.add(l_country1);
        l_sourcePlayer.setD_coutriesOwned(l_s1);

        ModelPlayer l_targetPlayer = new ModelPlayer("Rajat");
        Country l_country2 = new Country("Canada");
        l_country2.setD_armies(4);
        List<Country> l_s2 = new ArrayList<>();
        l_s2.add(l_country2);
        l_targetPlayer.setD_coutriesOwned(l_s2);

        ModelAdvance l_advance = new ModelAdvance(l_sourcePlayer, "India", "Canada", 5);
        l_advance.handleSurvivingArmies(5, 0, l_country1, l_country2, l_targetPlayer);

        assertEquals(l_targetPlayer.getD_coutriesOwned().size(), 0);
        assertEquals(l_sourcePlayer.getD_coutriesOwned().size(), 2);
        assertEquals(l_sourcePlayer.getD_coutriesOwned().get(1).getD_armies().toString(), "5");
    }



}
