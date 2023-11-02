package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


/**
 * Test Cases for Order Model.
 */
public class DeployTest {


    /**
     * The order details.
     */
    Deploy d_orderDetails;


    /**
     * The player info.
     */
    Player d_playerInfo;

    /**
     * Initialization.
     */
    @Before
    public void setup() {
        d_orderDetails = new Deploy();
        d_playerInfo = new Player();
    }


    /**
     * Test case to validate country for the deployed order.
     */
    @Test
    public void testValidateDeployOrderCountry() {
        d_orderDetails.setD_targetCountryName("India");
        List<Country> l_countryList = new ArrayList<Country>();
        l_countryList.add(new Country("India"));
        l_countryList.add(new Country("Canada"));
        d_playerInfo.setD_coutriesOwned(l_countryList);
        boolean l_actualBoolean = d_orderDetails.validateDeployOrderCountry(d_playerInfo, d_orderDetails);
        assertTrue(l_actualBoolean);
    }


    /**
     * TestCase to validate the deploy order execution.
     */
    @Test
    public void testdeployOrderExecution() {
       //Adding player and assigning countries
        Player l_player = new Player();
        List<Country> l_playersCountries = new ArrayList<Country>();
        l_playersCountries.add(new Country("India"));
        l_playersCountries.add(new Country("Canada"));
        l_player.setD_coutriesOwned(l_playersCountries);

        // Creating list of countries
        List<Country> l_mapCountries = new ArrayList<Country>();
        Country l_country1 = new Country(1, "Canada", 1);
        Country l_country2 = new Country(1, "India", 2);
        l_country2.setD_armies(10);
        Country l_country3 = new Country(1, "Japan", 2);

        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);
        l_mapCountries.add(l_country3);

        Map l_map = new Map();
        l_map.setD_countries(l_mapCountries);
        State l_gameState = new State();
        l_gameState.setD_map(l_map);

        //create deploy orders
        Deploy l_order1 = new Deploy("deploy", "India", 10);
        Deploy l_order2 = new Deploy("deploy", "Canada", 15);
        l_order1.execute(l_gameState, l_player);
        Country l_countryIndia = l_gameState.getD_map().getCountryByName("India");
        assertEquals("20", l_countryIndia.getD_armies().toString());

        l_order2.execute(l_gameState, l_player);
        Country l_countryCanada = l_gameState.getD_map().getCountryByName("Canada");
        assertEquals("15", l_countryCanada.getD_armies().toString());

    }
}
