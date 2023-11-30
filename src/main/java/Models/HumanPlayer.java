package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The HumanPlayer class represents a player with a human-controlled strategy.
 * It extends the PlayerBehaviorStrategy class and allows the human player to input commands during the game.
 */
public class HumanPlayer extends PlayerBehaviorStrategy{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createCardOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState, String p_currentCardName) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) throws IOException {

		BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease enter command to issue order for player : " + p_modelPlayer.getPlayerName()
				+ " or give showmap command to view current state of the game.");
		String l_commandEntered = l_reader.readLine();
		return l_commandEntered;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createDeployOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createAdvanceOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPlayerBehavior() {
		return "Human";
	}
    
}
