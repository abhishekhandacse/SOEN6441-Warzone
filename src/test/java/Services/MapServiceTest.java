package Services;

import Exceptions.InvalidCommand;
import Exceptions.MapValidationException;
import Models.Continent;
import Models.ModelCountry;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapServiceTest {

	MapService d_mapService;

	Map d_map;

	GameState d_state;

	@Before

	public void setup() throws MapValidationException {
		d_mapService = new MapService();
		d_map = new Map();
		d_state = new GameState();
		d_map = d_mapService.loadMap(d_state, "europe.map");
	}

	@Test
	public void testEditMap() throws IOException, MapValidationException {
		d_mapService.editMap(d_state, "test.map");
		File l_file = new File(CommonUtil.getAbsolutePathForFile("test.map"));

		assertTrue(l_file.exists());
	}

	@Test
	public void testEditContinentAdd() throws IOException, MapValidationException, InvalidCommand {
		d_state.setD_map(new Map());
		Map l_updatedContinents = d_mapService.addRemoveContinents(d_state, d_state.getD_map(), "Add", "Asia 10");

		assertEquals(l_updatedContinents.getD_allContinents().size(), 1);
		assertEquals(l_updatedContinents.getD_allContinents().get(0).getD_continentName(), "Asia");
		assertEquals(l_updatedContinents.getD_allContinents().get(0).getD_continentValue().toString(), "10");
	}

	@Test
	public void testEditContinentRemove() throws IOException, MapValidationException, InvalidCommand {
		List<Continent> l_continents = new ArrayList<>();
		Continent l_c1 = new Continent();
		l_c1.setD_continentID(1);
		l_c1.setD_continentName("Asia");
		l_c1.setD_continentValue(10);

		Continent l_c2 = new Continent();
		l_c2.setD_continentID(2);
		l_c2.setD_continentName("Europe");
		l_c2.setD_continentValue(20);

		l_continents.add(l_c1);
		l_continents.add(l_c2);

		Map l_map = new Map();
		l_map.setD_allContinents(l_continents);
		d_state.setD_map(l_map);
		Map l_updatedContinents = d_mapService.addRemoveContinents(d_state, d_state.getD_map(), "Remove", "Asia");

		assertEquals(l_updatedContinents.getD_allContinents().size(), 1);
		assertEquals(l_updatedContinents.getD_allContinents().get(0).getD_continentName(), "Europe");
		assertEquals(l_updatedContinents.getD_allContinents().get(0).getD_continentValue().toString(), "20");
	}

	@Test
	public void testContinentIdAndValues() {
		List<Integer> l_actualContinentIdList = new ArrayList<Integer>();
		List<Integer> l_actualContinentValueList = new ArrayList<Integer>();

		List<Integer> l_expectedContinentIdList = new ArrayList<Integer>();
		l_expectedContinentIdList.addAll(Arrays.asList(1, 2, 3, 4));

		List<Integer> l_expectedContinentValueList = new ArrayList<Integer>();
		l_expectedContinentValueList.addAll(Arrays.asList(5, 4, 5, 3));

		for (Continent l_continent : d_map.getD_allContinents()) {
			l_actualContinentIdList.add(l_continent.getD_continentID());
			l_actualContinentValueList.add(l_continent.getD_continentValue());
		}

		assertEquals(l_expectedContinentIdList, l_actualContinentIdList);
		assertEquals(l_expectedContinentValueList, l_actualContinentValueList);
	}

	@Test
	public void testCountryIdAndNeighbors() {
		List<Integer> l_actualCountryIdList = new ArrayList<Integer>();
		LinkedHashMap<Integer, List<Integer>> l_actualCountryNeighbors = new LinkedHashMap<Integer, List<Integer>>();

		List<Integer> l_expectedCountryIdList = new ArrayList<Integer>();
		l_expectedCountryIdList.addAll(Arrays.asList(1, 2, 3, 4, 5));

		LinkedHashMap<Integer, List<Integer>> l_expectedCountryNeighbors = new LinkedHashMap<Integer, List<Integer>>() {
			{
				put(1, new ArrayList<Integer>(Arrays.asList(8, 21, 6, 7, 5, 2, 3, 4)));
				put(2, new ArrayList<Integer>(Arrays.asList(8, 1, 3)));
				put(3, new ArrayList<Integer>(Arrays.asList(1, 2)));
				put(4, new ArrayList<Integer>(Arrays.asList(22, 1, 5)));
				put(5, new ArrayList<Integer>(Arrays.asList(1, 4)));
			}
		};

		for (ModelCountry l_country : d_map.getD_allCountries()) {
			ArrayList<Integer> l_neighbours = new ArrayList<Integer>();
			l_actualCountryIdList.add(l_country.getD_countryId());
			l_neighbours.addAll(l_country.getD_adjacentCountryIds());
			l_actualCountryNeighbors.put(l_country.getD_countryId(), l_neighbours);
		}

		assertEquals(l_expectedCountryIdList, l_actualCountryIdList);
		assertEquals(l_expectedCountryNeighbors, l_actualCountryNeighbors);
	}


	@Test
	public void testSaveMapValidationException() throws MapValidationException {
		d_map.setD_inputMapFile("europe.map");
		d_state.setD_map(d_map);
		d_mapService.mapSave(d_state, "europe.map");
		assertEquals("Log: Couldn't save the changes in map file!"+System.lineSeparator(), d_state.getRecentLog());
	}

	@Test
	public void testEditCountryAdd() throws IOException, MapValidationException, InvalidCommand {
		d_mapService.loadMap(d_state, "test.map");
		d_mapService.editFunctions(d_state, "add", "China Asia", 2);

		assertEquals(d_state.getD_map().getCountryByName("China").getD_countryName(), "China");
	}

	@Test
	public void testEditCountryRemove() throws MapValidationException, IOException, InvalidCommand {
		d_mapService.loadMap(d_state, "test.map");
		d_mapService.editFunctions(d_state, "remove", "Ukraine", 2);
		assertEquals("Log: Country: Ukraine does not exist!"+System.lineSeparator(), d_state.getRecentLog());
	}

	@Test
	public void testEditNeighborAdd() throws MapValidationException, IOException, InvalidCommand {
		d_mapService.loadMap(d_state, "test.map");
		d_mapService.editFunctions(d_state, "Northern-America 10", "add", 1 );
		d_mapService.editFunctions(d_state, "add", "Canada Northern-America",  2);
		d_mapService.editFunctions(d_state, "add","Alaska Northern-America", 2);
		d_mapService.editFunctions(d_state, "add", "Canada Alaska", 3);

		assertEquals(d_state.getD_map().getCountryByName("Canada").getD_adjacentCountryIds().get(0), d_state.getD_map().getCountryByName("Alaska").getD_countryId());
	}

	@Test
	public void testEditNeighborRemove() throws MapValidationException, IOException, InvalidCommand{
		d_mapService.editMap(d_state, "testedit.map");
		d_mapService.editFunctions(d_state, "Asia 9", "add",   1);
		d_mapService.editFunctions(d_state, "add", "Maldives Asia", 2);
		d_mapService.editFunctions(d_state, "add", "Singapore Asia", 2);
		d_mapService.editFunctions(d_state, "add", "Singapore Maldives", 3);
		d_mapService.editFunctions(d_state, "remove", "Maldives Singapore", 3);
		assertEquals("Log: No Such Neighbour Exists"+System.lineSeparator(), d_state.getRecentLog());
	}
}
