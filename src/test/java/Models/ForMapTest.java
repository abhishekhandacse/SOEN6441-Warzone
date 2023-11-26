package Models;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.MapValidationException;
import Services.MapService;

/**
 *
 * This class is used to test functionality of Map class functions.
 *
 */
public class ForMapTest {
    Map d_map;
    MapService d_ms;
    GameState d_stateOfGame;

    /**
     * Checking Map Model Operations
     */
    @Before
    public void beforeValidateTest(){
        d_map=new Map();
        d_stateOfGame =new GameState();
        d_ms= new MapService();
    }

    /**
     * Checking {@link MapValidationException} for no continent in Map
     * @throws MapValidationException Exception
     */
    @Test (expected = MapValidationException.class)
    public void testValidateNoContinent() throws MapValidationException{
        assertEquals(d_map.Validate(), false);
    }

    /**
     * Required Test #2
     * Tests a valid and invalid Map for Validate function
     *
     * @throws MapValidationException Exception
     */
    @Test (expected = MapValidationException.class)
    public void testValidate() throws MapValidationException {
        d_map= d_ms.loadMap(d_stateOfGame, "canada.map");

        assertEquals(d_map.Validate(), true);
        d_map= d_ms.loadMap(d_stateOfGame, "swiss.map");
        d_map.Validate();
    }

    /**
     * Checking {@link MapValidationException} for no country in Map
     *
     * @throws MapValidationException Exception
     */
    @Test (expected = MapValidationException.class)
    public void testValidateNoCountry() throws MapValidationException{
        Continent l_continent = new Continent();
        List <Continent> l_continents = new ArrayList<>();

        l_continents.add(l_continent);
        d_map.setD_continents(l_continents);
        d_map.Validate();
    }

    /**
     * Required Test # 1
     * Checks Continent connectivity of an unconnected continent
     *
     * @throws MapValidationException Exception
     */
    @Test (expected = MapValidationException.class)
    public void testContinentConnectivity() throws  MapValidationException{
        d_map= d_ms.loadMap(d_stateOfGame, "continentConnectivity.map");
        d_map.Validate();
    }

    /**
     * Required Test # 1
     * Checks Country Connectivity for not connected countries
     *
     * @throws MapValidationException Exception
     */
    @Test(expected = MapValidationException.class)
    public void testCountryConnectivity() throws MapValidationException{
        d_map.addContinent("Asia", 10);
        d_map.addCountry("India", "Asia");
        d_map.addCountry("China", "Asia");
        d_map.addCountry("Maldives", "Asia");
        d_map.addCountryNeighbour("India", "China");
        d_map.addCountryNeighbour("China", "India");
        d_map.addCountry("India", "Maldives");
        d_map.checkCountryConnectivity();
    }
}
