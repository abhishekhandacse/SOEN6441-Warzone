package Models;
import Utils.CommonUtil;
import java.io.Serializable;

/**
 * The Blockade class represents a Blockade card in the game.
 * It implements the Card interface and is Serializable.
 */

public class Blockade implements Card, Serializable {

	/**
     * The player who initiated the Blockade.
     */
	ModelPlayer d_playerInitiator;

	/**
     * The ID of the target country for the Blockade.
     */
	String d_targetCountryID;

	/**
     * The log of order execution.
     */
	String d_orderExecutionLog;

	/**
     * Constructor for the Blockade class.
     *
     * @param p_playerInitiator The player initiating the Blockade
     * @param p_targetCountry   The ID of the target country for the Blockade
     */
	public Blockade(ModelPlayer p_playerInitiator, String p_targetCountry) {
		this.d_playerInitiator = p_playerInitiator;
		this.d_targetCountryID = p_targetCountry;
	}

    /**
     * Gets the name of the order (Blockade).
     *
     * @return The order name
     */
	@Override
	public String getOrderName() {
		return "blockade";
	}

    /**
     * Retrieves the current Blockade order.
     *
     * @return The current Blockade order as a string
     */
	private String currentOrder() {
		return "Blockade card order : " + "blockade" + " " + this.d_targetCountryID;
	}

    /**
     * Retrieves the order execution log.
     *
     * @return The order execution log as a string
     */
	public String orderExecutionLog() {
		return this.d_orderExecutionLog;
	}

    /**
     * Executes the Blockade order.
     *
     * @param p_gameState The current game state
     */
    @Override
	public void execute(GameState p_gameState) {
		if (checkValid(p_gameState)) {
			Country l_targetCountryID = p_gameState.getD_map().getCountryByName(d_targetCountryID);
			Integer l_noOfArmiesOnTargetCountry = l_targetCountryID.getD_armies() == 0 ? 1
					: l_targetCountryID.getD_armies();
			l_targetCountryID.setD_armies(l_noOfArmiesOnTargetCountry * 3);

			// change territory to neutral territory
			d_playerInitiator.getD_coutriesOwned().remove(l_targetCountryID);

			ModelPlayer l_player = p_gameState.getD_players().stream()
					.filter(l_pl -> l_pl.getPlayerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);

			// assign neutral territory to the existing neutral player.
			if (!CommonUtil.isNull(l_player)) {
				l_player.getD_coutriesOwned().add(l_targetCountryID);
				System.out.println("Neutral territory: " + l_targetCountryID.getD_countryName() + "assigned to the Neutral Player.");
			}

			d_playerInitiator.removeCard("blockade");
			this.setD_orderExecutionLog("\nPlayer : " + this.d_playerInitiator.getPlayerName()
					+ " is executing defensive blockade on Country :  " + l_targetCountryID.getD_countryName()
					+ " with armies :  " + l_targetCountryID.getD_armies(), "default");
			p_gameState.updateLog(orderExecutionLog(), "effect");
		}
	}

    /**
     * Checks if the Blockade order is valid.
     *
     * @param p_gameState The current game state
     * @return True if the order is valid, false otherwise
     */
    @Override
	public boolean checkValid(GameState p_gameState) {
		// Validates whether target country belongs to the Player who executed the order or not
		Country l_country = d_playerInitiator.getD_coutriesOwned().stream()
				.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountryID)).findFirst()
				.orElse(null);

		if (CommonUtil.isNull(l_country)) {
			this.setD_orderExecutionLog(this.currentOrder() + " is not executed since Target country : "
					+ this.d_targetCountryID + " given in blockade command does not owned to the player : "
					+ d_playerInitiator.getPlayerName()
					+ " The card will have no affect and you don't get the card back.", "error");
			p_gameState.updateLog(orderExecutionLog(), "effect");
			return false;
		}
		return true;
	}

	/**
     * Prints the Blockade order.
     */
	@Override
	public void printOrder() {
		this.d_orderExecutionLog = "----------Blockade card order issued by player "
				+ this.d_playerInitiator.getPlayerName() + "----------" + System.lineSeparator()
				+ "Creating a defensive blockade " + "on country ID: " + this.d_targetCountryID;
		System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
	}

    /**
     * Sets the order execution log with the specified log type.
     *
     * @param p_orderExecutionLog The order execution log
     * @param p_logType           The type of log (error or default)
     */
	public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
		this.d_orderExecutionLog = p_orderExecutionLog;
		if (p_logType.equals("error")) {
			System.err.println(p_orderExecutionLog);
		} else {
			System.out.println(p_orderExecutionLog);
		}
	}

    /**
     * Checks if the Blockade order is valid in the current game state.
     *
     * @param p_currentGameState The current game state
     * @return True if the order is valid, false otherwise
     */
	@Override
	public Boolean checkIfOrderIsValid(GameState p_currentGameState) {
		Country l_targetCountry = p_currentGameState.getD_map().getCountryByName(d_targetCountryID);
		if (l_targetCountry == null) {
			this.setD_orderExecutionLog("Invalid Target Country! Doesn't exist on the map!", "error");
			return false;
		}
		return true;
	}

}
