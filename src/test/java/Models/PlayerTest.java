package Models;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


/**
 * The type Player test.
 */
public class PlayerTest {

    /**
     * The D exisiting player list.
     */
    List<Player> d_exisitingPlayerList = new ArrayList<>();


    /**
     * Sets .
     */
    @Before
    public void setup() {
        d_exisitingPlayerList.add(new Player("Rajat"));
        d_exisitingPlayerList.add(new Player("Anurag"));
    }

    /**
     * Test next order.
     */
    @Test
    public void testNextOrder() {
        Deploy l_order1 = new Deploy();
        l_order1.setD_orderAction("deploy");
        l_order1.setD_numberOfArmiesToPlace(5);
        l_order1.setD_sourceCountryName(null);
        l_order1.setD_targetCountryName("India");

        Deploy l_order2 = new Deploy();
        l_order1.setD_orderAction("deploy");
        l_order2.setD_numberOfArmiesToPlace(6);
        l_order2.setD_sourceCountryName("");
        l_order2.setD_targetCountryName("Finland");

        List<Deploy> l_orderlist = new ArrayList<>();
        l_orderlist.add(l_order1);
        l_orderlist.add(l_order2);

        d_exisitingPlayerList.get(0).setD_ordersToExecute(l_orderlist);
        Deploy l_order = d_exisitingPlayerList.get(0).nextOrder();
        assertEquals(l_order, l_order1);
        assertEquals(d_exisitingPlayerList.get(0).getD_ordersToExecute().size(), 1);
    }
}
