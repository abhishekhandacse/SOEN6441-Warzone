package Controllers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Models.Continent;
import Models.Country;
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

    @Test
	public void testEditContinentValidCommand() throws IOException, CommandValidationException, MapValidationException {
		d_map.setD_mapFile("testmap.map");
		d_state.setD_map(d_map);
		CommandHandler l_addCommand = new CommandHandler("editcontinent -add Africa 12 -add Asia 15");
		d_engine.editContinent(l_addCommand);

		List<Continent> l_contList = d_state.getD_map().getD_continents();
		assertEquals(l_contList.size(), 2);
		assertEquals(l_contList.get(0).getD_continentName(), "Africa");
		assertEquals(l_contList.get(0).getD_continentValue().toString(), "12");
		assertEquals(l_contList.get(1).getD_continentName(), "Asia");
		assertEquals(l_contList.get(1).getD_continentValue().toString(), "15");

		CommandHandler l_removeCommand = new CommandHandler("editcontinent -remove Africa");
		d_engine.editContinent(l_removeCommand);
		l_contList = d_state.getD_map().getD_continents();
		assertEquals( 1, l_contList.size());
	}

	@Test
	public void testEditCountryValidCommand() throws IOException, CommandValidationException, MapValidationException {
		d_map.setD_mapFile("testedit.map");
		d_state.setD_map(d_map);
		CommandHandler l_addCommand = new CommandHandler("editcontinent -add Africa 12 -add Asia 15");
		d_engine.editContinent(l_addCommand);

		CommandHandler l_addCountryIndiaCommand = new CommandHandler("editcountry -add India Asia");
		CommandHandler l_addCountryJapanCommand = new CommandHandler("editcountry -add Japan Asia");
		d_engine.editCountry(l_addCountryIndiaCommand);
		d_engine.editCountry(l_addCountryJapanCommand);

		List<Country> l_countriesList = d_state.getD_map().getD_countries();
		assertEquals( 2, l_countriesList.size());		
	}
}
