package Services;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.GameState;
import Models.Map;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ConquestMapFileReaderTest {

	MapService d_mapService;


	List<String> d_mapLines;

	Map d_map;


	GameState d_state;


	ConquestMapFileReader d_conquestMapFileReader;


	@Before
	public void setup() throws InvalidMap {
		d_conquestMapFileReader = new ConquestMapFileReader();
		d_mapService = new MapService();
		d_map = new Map();
		d_state = new GameState();
		d_mapLines = d_mapService.loadFile("testconquest.map");
	}


	@Test
	public void testReadConquestFile() throws IOException, InvalidMap {
		d_conquestMapFileReader.readConquestFile(d_state, d_map, d_mapLines);

		assertNotNull(d_state.getD_map());
		assertEquals(d_state.getD_map().getD_continents().size(), 8);
		assertEquals(d_state.getD_map().getD_countries().size(), 99);
	}

	@Test
	public void testEditMap() throws IOException, InvalidMap, InvalidCommand {
		d_conquestMapFileReader.readConquestFile(d_state, d_map, d_mapLines);
		Map l_updatedContinents = d_mapService.addRemoveContinents(d_state, d_state.getD_map(), "Add", "Asia 10");
		l_updatedContinents = d_mapService.addRemoveContinents(d_state, d_state.getD_map(), "Add", "Europe 20");

		assertEquals(l_updatedContinents.getD_continents().size(), 10);
		assertEquals(l_updatedContinents.getD_continents().get(8).getD_continentName(), "Asia");
		assertEquals(l_updatedContinents.getD_continents().get(8).getD_continentValue().toString(), "10");

		l_updatedContinents = d_mapService.addRemoveContinents(d_state, d_state.getD_map(), "Remove", "Asia");
		assertEquals(l_updatedContinents.getD_continents().size(), 9);

		d_mapService.editFunctions(d_state, "add", "Swiss Europe", 2);
		d_mapService.editFunctions(d_state, "add", "Norway Europe", 2);
		assertEquals(d_state.getD_map().getD_countries().size(), 101);

		d_mapService.editFunctions(d_state, "remove", "Swiss", 2);
		assertEquals(d_state.getD_map().getD_countries().size(), 100);
	}
}
