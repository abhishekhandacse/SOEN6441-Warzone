package Models;
import static org.junit.Assert.assertEquals;

import Controllers.MapController;
import Exceptions.MapValidationException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class MapTest {

    Map d_map;
    MapController d_ms;
    State d_gameState;


    @Before
    public void beforeValidateTest(){
        d_map=new Map();
        d_gameState=new State();
        d_ms= new MapController();
    }


    @Test (expected = MapValidationException.class)
    public void testValidateNoContinent() throws MapValidationException{
        assertEquals(d_map.Validate(), false);
    }


    @Test (expected = MapValidationException.class)
    public void testValidate() throws MapValidationException {
        d_map= d_ms.loadMap(d_gameState, "canada.map");

        assertEquals(d_map.Validate(), true);
        d_map= d_ms.loadMap(d_gameState, "swiss.map");
        d_map.Validate();
    }


    @Test (expected = MapValidationException.class)
    public void testValidateNoCountry() throws MapValidationException{
        Continent l_continent = new Continent();
        List <Continent> l_continents = new ArrayList<Continent>();

        l_continents.add(l_continent);
        d_map.setD_continents(l_continents);
        d_map.Validate();
    }


    @Test (expected = MapValidationException.class)
    public void testContinentConnectivity() throws  MapValidationException{
        d_map= d_ms.loadMap(d_gameState, "continentConnectivity.map");
        d_map.Validate();
    }


    @Test(expected = MapValidationException.class)
    public void testCountryConnectivity() throws MapValidationException{
        d_map.addContinent("Asia", 10);
        d_map.addCountry("India", "Asia");
        d_map.addCountry("China", "Asia");
        d_map.addCountry("Maldives", "Asia");
        d_map.addCountryNeighbours("India", "China");
        d_map.addCountryNeighbours("China", "India");
        d_map.addCountry("India", "Maldives");
        d_map.checkConnectionOfCountry();
    }
}
