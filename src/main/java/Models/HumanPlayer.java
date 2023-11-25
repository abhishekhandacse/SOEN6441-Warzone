package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HumanPlayer extends PlayerBehaviorStrategy{

	@Override
	public String createCardOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState, String p_currentCardName) {
		return null;
	}

	@Override
	public String createOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) throws IOException {

		BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease enter command to issue order for player : " + p_modelPlayer.getPlayerName()
				+ " or give showmap command to view current state of the game.");
		String l_commandEntered = l_reader.readLine();
		return l_commandEntered;
	}

	@Override
	public String createDeployOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
		return null;
	}

	@Override
	public String createAdvanceOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
		return null;
	}

	@Override
	public String getPlayerBehavior() {
		return "Human";
	}
    
}
