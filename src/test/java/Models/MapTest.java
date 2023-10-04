package Models;

import static org.junit.Assert.assertEquals;

import Controllers.MapController;
import Exceptions.MapValidationException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Map test.
 */
public class MapTest {

    /**
     * The D map.
     */
    Map d_map;
    /**
     * The D ms.
     */
    MapController d_ms;
    /**
     * The D game state.
     */
    State d_gameState;


    /**
     * Before validate test.
     */
    @Before
    public void beforeValidateTest() {
        d_map = new Map();
        d_gameState = new State();
        d_ms = new MapController();
    }


    /**
     * Test validate no continent.
     *
     * @throws MapValidationException the map validation exception
     */
    @Test(expected = MapValidationException.class)
    public void testValidateNoContinent() throws MapValidationException {
        assertEquals(d_map.Validate(), false);
    }


    /**
     * Test validate.
     *
     * @throws MapValidationException the map validation exception
     */
    @Test(expected = MapValidationException.class)
    public void testValidate() throws MapValidationException {
        d_map = d_ms.loadMap(d_gameState, "canada.map");

        assertEquals(d_map.Validate(), true);
        d_map = d_ms.loadMap(d_gameState, "game.map");
        d_map.Validate();
    }


    /**
     * Test validate no country.
     *
     * @throws MapValidationException the map validation exception
     */
    @Test(expected = MapValidationException.class)
    public void testValidateNoCountry() throws MapValidationException {
        Continent l_continent = new Continent();
        List<Continent> l_continents = new ArrayList<Continent>();

        l_continents.add(l_continent);
        d_map.setD_continents(l_continents);
        d_map.Validate();
    }


    /**
     * Test continent connectivity.
     *
     * @throws MapValidationException the map validation exception
     */
    @Test(expected = MapValidationException.class)
    public void testContinentConnectivity() throws MapValidationException {
        d_map = d_ms.loadMap(d_gameState, "game.map");
        d_map.Validate();
    }


    /**
     * Test country neighbors.
     *
     * @throws MapValidationException the map validation exception
     */
    @Test(expected = MapValidationException.class)
    public void testCountryNeighbors() throws MapValidationException {
        d_map.addContinent("Asia", 10);
        d_map.addCountry("Paistan", "Asia");
        d_map.addCountry("Japan", "Asia");
        d_map.addCountry("Sri Lanka", "Asia");
        d_map.addCountryNeighbours("Paistan", "Japan");
        d_map.addCountryNeighbours("Japan", "Paistan");
        d_map.addCountry("Paistan", "Sri Lanka");
        d_map.checkConnectionOfCountry();
    }
}
