package Controllers;

import Exceptions.MapValidationException;
import Models.Continent;
import Models.ModelCountry;
import Models.Map;
import Models.GameState;
import Utils.CommonUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The type Map controller test.
 */
public class MapControllerTest {


    /**
     * The D mapservice.
     */
    MapController d_mapservice;


    /**
     * The D map.
     */
    Map d_map;


    /**
     * The D state.
     */
    GameState d_state;


    /**
     * Sets .
     */
    @Before
    public void setup() {
        d_mapservice = new MapController();
        d_map = new Map();
        d_state = new GameState();
        d_map = d_mapservice.loadMap(d_state, "europe.map");
    }


    /**
     * Test edit map.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testEditMap() throws IOException {
        d_mapservice.editMap(d_state, "test.map");
        File l_file = new File(CommonUtil.getMapFilePath("test.map"));

        assertTrue(l_file.exists());
    }


    /**
     * Test edit continent add.
     *
     * @throws MapValidationException the map validation exception
     */
    @Test
    public void testEditContinentAdd() throws MapValidationException {
        d_state.setD_map(new Map());
        Map l_updatedContinents = d_mapservice.addRemoveContinents(d_state.getD_map(), "Add", "Asia 1");

        assertEquals(l_updatedContinents.getD_continents().size(), 1);
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentName(), "Asia");
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentValue().toString(), "1");
    }


    /**
     * Test edit continent remove.
     *
     * @throws IOException            the io exception
     * @throws MapValidationException the map validation exception
     */
    @Test
    public void testEditContinentRemove() throws IOException, MapValidationException {
        List<Continent> l_continents = new ArrayList<>();
        Continent l_c1 = new Continent();
        l_c1.setD_continentID(1);
        l_c1.setD_continentName("Asia");
        l_c1.setD_continentValue(1);

        Continent l_c2 = new Continent();
        l_c2.setD_continentID(2);
        l_c2.setD_continentName("Europe");
        l_c2.setD_continentValue(20);

        l_continents.add(l_c1);
        l_continents.add(l_c2);

        Map l_map = new Map();
        l_map.setD_continents(l_continents);
        d_state.setD_map(l_map);
        Map l_updatedContinents = d_mapservice.addRemoveContinents(d_state.getD_map(), "Remove", "Asia");

        assertEquals(l_updatedContinents.getD_continents().size(), 1);
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentName(), "Europe");
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentValue().toString(), "20");
    }


    /**
     * Test continent id and values.
     */
    @Test
    public void testContinentIdAndValues() {
        List<Integer> l_actualContinentIdList = new ArrayList<Integer>();
        List<Integer> l_actualContinentValueList = new ArrayList<Integer>();

        List<Integer> l_expectedContinentIdList = new ArrayList<Integer>();
        l_expectedContinentIdList.addAll(Arrays.asList(1, 2));

        List<Integer> l_expectedContinentValueList = new ArrayList<Integer>();
        l_expectedContinentValueList.addAll(Arrays.asList(1, 20));

        for (Continent l_continent : d_map.getD_continents()) {
            l_actualContinentIdList.add(l_continent.getD_continentID());
            l_actualContinentValueList.add(l_continent.getD_continentValue());
        }

        assertEquals(l_expectedContinentIdList, l_actualContinentIdList);
        assertEquals(l_expectedContinentValueList, l_actualContinentValueList);
    }


    /**
     * Test country id and neighbors.
     */
    @Test
    public void testCountryIdAndNeighbors() {
        List<Integer> l_actualCountryIdList = new ArrayList<Integer>();
        LinkedHashMap<Integer, List<Integer>> l_actualCountryNeighbors = new LinkedHashMap<Integer, List<Integer>>();

        List<Integer> l_expectedCountryIdList = new ArrayList<Integer>();
        l_expectedCountryIdList.addAll(Arrays.asList(1, 2, 3, 4));

        LinkedHashMap<Integer, List<Integer>> l_expectedCountryNeighbors = new LinkedHashMap<Integer, List<Integer>>() {
            {
                put(1, new ArrayList<Integer>(Arrays.asList(2, 3)));
                put(2, new ArrayList<Integer>(Arrays.asList(1)));
                put(3, new ArrayList<Integer>(Arrays.asList(4)));
                put(4, new ArrayList<Integer>(Arrays.asList(2)));
            }
        };

        for (ModelCountry l_country : d_map.getD_countries()) {
            ArrayList<Integer> l_neighbours = new ArrayList<Integer>();
            l_actualCountryIdList.add(l_country.getD_countryId());
            l_neighbours.addAll(l_country.getD_adjacentCountryIds());
            l_actualCountryNeighbors.put(l_country.getD_countryId(), l_neighbours);
        }

        assertEquals(l_expectedCountryIdList, l_actualCountryIdList);
        assertEquals(l_expectedCountryNeighbors, l_actualCountryNeighbors);
    }


    /**
     * Test edit country add.
     *
     * @throws IOException            the io exception
     * @throws MapValidationException the map validation exception
     */
    @Test
    public void testEditCountryAdd() throws IOException, MapValidationException {
        d_mapservice.loadMap(d_state, "test.map");
        d_mapservice.editCountry(d_state, "add", "China Asia");

        assertEquals(d_state.getD_map().getCountryByName("China").getD_countryName(), "China");
    }


    /**
     * Test edit country remove.
     *
     * @throws MapValidationException the map validation exception
     */
    @Test(expected = MapValidationException.class)
    public void testEditCountryRemove() throws MapValidationException {
        d_mapservice.loadMap(d_state, "test.map");
        d_mapservice.editCountry(d_state, "remove", "Ukraine");
    }


    /**
     * Test edit neighbor add.
     *
     * @throws MapValidationException the map validation exception
     * @throws IOException            the io exception
     */
    @Test
    public void testEditNeighborAdd() throws MapValidationException, IOException {
        d_mapservice.loadMap(d_state, "test.map");
        d_mapservice.editContinent(d_state, "Northern-America 10", "add");
        d_mapservice.editCountry(d_state, "add", "Canada Northern-America");
        d_mapservice.editCountry(d_state, "add", "Alaska Northern-America");
        d_mapservice.editNeighbour(d_state, "add", "Canada Alaska");

        assertEquals(d_state.getD_map().getCountryByName("Canada").getD_adjacentCountryIds().get(0), d_state.getD_map().getCountryByName("Alaska").getD_countryId());
    }


    /**
     * Test edit neighbor remove.
     *
     * @throws MapValidationException the map validation exception
     * @throws IOException            the io exception
     */
    @Test(expected = MapValidationException.class)
    public void testEditNeighborRemove() throws MapValidationException, IOException {
        d_mapservice.editMap(d_state, "testedit.map");
        d_mapservice.editContinent(d_state, "Asia 9", "add");
        d_mapservice.editCountry(d_state, "add", "Maldives Asia");
        d_mapservice.editNeighbour(d_state, "add", "Singapore Maldives");
    }
}
