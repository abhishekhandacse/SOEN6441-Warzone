package Controllers;

import org.junit.Before;

import Models.Map;
import Models.State;

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
}
