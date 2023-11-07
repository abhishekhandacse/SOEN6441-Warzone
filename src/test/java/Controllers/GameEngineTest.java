package Controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.GameState;
import Models.Map;
import Models.Phase;
import Models.InitialStartUpPhase;


public class GameEngineTest {

	Map d_Map;

	Phase d_presentPhase;

	GameEngine d_gameEngine;

	@Before
	public void setup() {
		d_Map = new Map();
		d_gameEngine = new GameEngine();
		d_presentPhase = d_gameEngine.getD_CurrentPhase();
	}

	@Test(expected = InvalidCommand.class)
	public void testPerformEditMapInvalidCommand() throws IOException, InvalidCommand, InvalidMap {
		d_presentPhase.handleCommand("editmap");
	}

	@Test
	public void testPerformEditContinentInvalidCommand() throws InvalidCommand, IOException, InvalidMap {
		d_presentPhase.handleCommand("editcontinent");
		GameState l_state = d_presentPhase.getD_gameState();

		assertEquals("Log: Can not Edit Continent, please perform `editmap` first" + System.lineSeparator(),
				l_state.getRecentLog());
	}

	@Test
	public void testPerformEditContinentValidCommand() throws IOException, InvalidCommand, InvalidMap {
		d_Map.setD_inputMapFile("testeditmap.map");
		GameState l_state = d_presentPhase.getD_gameState();

		l_state.setD_map(d_Map);
		d_presentPhase.setD_gameState(l_state);

		d_presentPhase.handleCommand("editcontinent -add Europe 10 -add America 20");

		l_state = d_presentPhase.getD_gameState();

		List<Continent> l_continents = l_state.getD_map().getD_allContinents();
		assertEquals(2, l_continents.size());
		assertEquals("Europe", l_continents.get(0).getD_continentName());
		assertEquals("10", l_continents.get(0).getD_continentValue().toString());
		assertEquals("America", l_continents.get(1).getD_continentName());
		assertEquals("20", l_continents.get(1).getD_continentValue().toString());

		d_presentPhase.handleCommand("editcontinent -remove Europe");

		l_state = d_presentPhase.getD_gameState();
		l_continents = l_state.getD_map().getD_allContinents();
		assertEquals(1, l_continents.size());
	}

	@Test
	public void testPerformSaveMapInvalidCommand() throws InvalidCommand, InvalidMap, IOException {
		d_presentPhase.handleCommand("savemap");
		GameState l_state = d_presentPhase.getD_gameState();

		assertEquals("Log: No map found to save, Please `editmap` first" + System.lineSeparator(),
				l_state.getRecentLog());

	}

	@Test(expected = InvalidCommand.class)
	public void testAssignCountriesInvalidCommand() throws IOException, InvalidMap, InvalidCommand {
		d_presentPhase.handleCommand("assigncountries -add india");
		;
	}

	@Test
	public void testCorrectStartupPhase() {
		assertTrue(d_gameEngine.getD_CurrentPhase() instanceof InitialStartUpPhase);
	}
}
