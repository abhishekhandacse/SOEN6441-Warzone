package Controllers;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Models.Map;
import Models.State;
import Utils.CommandHandler;

public class MainGameEngineControllerTest {

    Map d_map;
	State d_state;
	MainGameEngineController d_engine;

	
	@Before
	public void initialize() {
		d_map = new Map();
		d_engine = new MainGameEngineController();
		d_state = d_engine.getD_state();
	}

    @Test(expected = CommandValidationException.class)
	public void testLoadMapInvalidCommand() throws IOException, CommandValidationException {
		CommandHandler l_command = new CommandHandler("loadmap");
		d_engine.loadMap(l_command);
	}

    @Test(expected = MapValidationException.class)
	public void testEditCountryInvalidCommand() throws IOException, CommandValidationException, MapValidationException {
        d_map.setD_mapFile("test.map");
		d_state.setD_map(d_map);
		CommandHandler l_command = new CommandHandler("editcountry -add India 1");
		d_engine.editCountry(l_command);
        
	}

    @Test(expected = CommandValidationException.class)
	public void testSaveMapInvalidCommand() throws CommandValidationException, MapValidationException {
		CommandHandler l_command = new CommandHandler("savemap");
		d_engine.saveMap(l_command);
	}
	
	
	@Test(expected = CommandValidationException.class)
	public void testAssignCountriesInvalidCommand() throws CommandValidationException, IOException {
		CommandHandler l_command = new CommandHandler("assigncountries -add");
		d_engine.assignCountries(l_command);
	}

    @Test(expected = CommandValidationException.class)
	public void testValidateMapInvalidCommand() throws CommandValidationException, IOException {
		CommandHandler l_command = new CommandHandler("validatemap test.map");
		d_engine.assignCountries(l_command);
	}
}
