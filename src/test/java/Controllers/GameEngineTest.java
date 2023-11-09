package Controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Models.ModelContinent;
import Models.GameState;
import Models.Map;
import Models.Phase;
import Models.InitialStartUpPhase;


/**
 * This class is responsible for testing the functionality of the GameEngine class.
 */
public class GameEngineTest {

	/**
	 * The map used for testing.
	*/
	Map d_Map;

	/**
	 * The current game phase.
	 */
	Phase d_presentPhase;

	/**
	 * The game engine being tested.
	 */
	GameEngine d_gameEngine;

	/**
     * Setup method to initialize the test environment and create necessary objects.
     */
	@Before
	public void setup() {
		d_Map = new Map();
		d_gameEngine = new GameEngine();
		d_presentPhase = d_gameEngine.getD_CurrentPhase();
	}

	/**
     * Test for handling an invalid "editmap" command.
     *
     * @throws IOException if there is an I/O error.
     * @throws CommandValidationException if there is a command validation error.
     * @throws MapValidationException if there is a map validation error.
     */
	@Test(expected = CommandValidationException.class)
	public void testPerformEditMapInvalidCommand() throws IOException, CommandValidationException, MapValidationException {
		d_presentPhase.handleCommand("editmap");
	}

	/**
     * Test for handling an invalid "editcontinent" command.
     *
     * @throws CommandValidationException if there is a command validation error.
     * @throws IOException if there is an I/O error.
     * @throws MapValidationException if there is a map validation error.
     */
	@Test
	public void testPerformEditContinentInvalidCommand() throws CommandValidationException, IOException, MapValidationException {
		d_presentPhase.handleCommand("editcontinent");
		GameState l_state = d_presentPhase.getD_gameState();

		assertEquals("Log: Can not Edit Continent, please perform `editmap` first" + System.lineSeparator(),
				l_state.getRecentLog());
	}

	/**
     * Test for handling a valid "editcontinent" command.
     *
     * @throws IOException if there is an I/O error.
     * @throws CommandValidationException if there is a command validation error.
     * @throws MapValidationException if there is a map validation error.
     */
	@Test
	public void testPerformEditContinentValidCommand() throws IOException, CommandValidationException, MapValidationException {
		d_Map.setD_inputMapFile("testeditmap.map");
		GameState l_state = d_presentPhase.getD_gameState();

		l_state.setD_map(d_Map);
		d_presentPhase.setD_gameState(l_state);

		d_presentPhase.handleCommand("editcontinent -add Europe 10 -add America 20");

		l_state = d_presentPhase.getD_gameState();

		List<ModelContinent> l_continents = l_state.getD_map().getD_allContinents();
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

	/**
     * Test for handling an invalid "savemap" command.
     *
     * @throws CommandValidationException if there is a command validation error.
     * @throws MapValidationException if there is a map validation error.
     * @throws IOException if there is an I/O error.
     */
	@Test
	public void testPerformSaveMapInvalidCommand() throws CommandValidationException, MapValidationException, IOException {
		d_presentPhase.handleCommand("savemap");
		GameState l_state = d_presentPhase.getD_gameState();

		assertEquals("Log: No map found to save, Please `editmap` first" + System.lineSeparator(),
				l_state.getRecentLog());

	}

	/**
     * Test for handling an invalid "assigncountries" command.
     *
     * @throws IOException if there is an I/O error.
     * @throws MapValidationException if there is a map validation error.
     * @throws CommandValidationException if there is a command validation error.
     */
	@Test(expected = CommandValidationException.class)
	public void testAssignCountriesInvalidCommand() throws IOException, MapValidationException, CommandValidationException {
		d_presentPhase.handleCommand("assigncountries -add india");
		;
	}

	/**
	 * Required Test #3
     * Test to ensure that the current game phase is the InitialStartUpPhase.
     */
	@Test
	public void testCorrectStartupPhase() {
		assertTrue(d_gameEngine.getD_CurrentPhase() instanceof InitialStartUpPhase);
	}
}
