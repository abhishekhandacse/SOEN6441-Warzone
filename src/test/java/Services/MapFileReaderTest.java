package Services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.MapValidationException;
import Models.GameState;
import Models.Map;

public class MapFileReaderTest {

	GameState d_state;

	Map d_map;

	MapService d_mapservice;

	List<String> d_mapLines;

	MapFileReader d_mapFileReader;

	@Before
	public void setup() throws MapValidationException {
		d_mapFileReader = new MapFileReader();
		d_mapservice = new MapService();
		d_map = new Map();
		d_state = new GameState();
		d_mapLines = d_mapservice.loadFile("canada.map");
	}

	@Test
	public void testReadMapFile() throws IOException, MapValidationException {
		d_mapFileReader.parseMapFile(d_state, d_map, d_mapLines);
		
		assertNotNull(d_state.getD_map());
		assertEquals(d_state.getD_map().getD_continents().size(), 6);
		assertEquals(d_state.getD_map().getD_countriesList().size(), 31);
	}
	
}