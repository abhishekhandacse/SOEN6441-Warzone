/**
 * The {@code ModelDeploy} class represents a Deploy order in a game, allowing a player to deploy a specified number of armies
 * to a target country.
 *
 * @version 1.0
 */
package Models;

import java.io.Serializable;

/**
 * Represents a Deploy order in the game.
 * This order allows a player to deploy a specified number of armies to a target country.
 */
public class ModelDeploy implements Order, Serializable {

	/** The name of the target country for the deploy order. */
	String d_targetCountryName;

	/** The number of armies to be deployed. */
	Integer d_numberOfArmiesToPlace;

	/** The player who initiated the deploy order. */
	ModelPlayer d_playerInitiator;

	/** The log of order execution for the deploy order. */
	String d_orderExecutionLog;

	/**
	 * Constructs a new Deploy order with the specified initiator player, target country, and number of armies to deploy.
	 *
	 * @param p_playerInitiator      The player who initiated the deploy order.
	 * @param p_targetCountry       The name of the target country for the deploy order.
	 * @param p_numberOfArmiesToPlace The number of armies to be deployed.
	 */
	public ModelDeploy(ModelPlayer p_playerInitiator, String p_targetCountry, Integer p_numberOfArmiesToPlace) {
		this.d_targetCountryName = p_targetCountry;
		this.d_playerInitiator = p_playerInitiator;
		this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
	}

	/**
	 * Executes the deploy order, deploying the specified number of armies to the target country if the order is valid.
	 *
	 * @param p_gameState The current game state.
	 */
	@Override
	public void execute(GameState p_gameState) {

		if (checkValid(p_gameState)) {
			for (Country l_country : p_gameState.getD_map().getD_countries()) {
				if (l_country.getD_countryName().equalsIgnoreCase(this.d_targetCountryName)) {
					Integer l_armiesToUpdate = l_country.getD_armies() == null ? this.d_numberOfArmiesToPlace
							: l_country.getD_armies() + this.d_numberOfArmiesToPlace;
					l_country.setD_armies(l_armiesToUpdate);
					this.setD_orderExecutionLog(+l_armiesToUpdate
									+ " armies have been deployed successfully on country : " + l_country.getD_countryName(),
							"default");
				}
			}

		} else {
			this.setD_orderExecutionLog("Deploy Order = " + "deploy" + " " + this.d_targetCountryName + " "
					+ this.d_numberOfArmiesToPlace + " is not executed since Target country: "
					+ this.d_targetCountryName + " given in deploy command does not belong to the player : "
					+ d_playerInitiator.getPlayerName(), "error");
			d_playerInitiator.setD_noOfUnallocatedArmies(
					d_playerInitiator.getD_noOfUnallocatedArmies() + this.d_numberOfArmiesToPlace);
		}
		p_gameState.updateLog(orderExecutionLog(), "effect");
	}

	/**
	 * Checks if the deploy order is valid.
	 *
	 * @param p_gameState The current game state.
	 * @return {@code true} if the order is valid, {@code false} otherwise.
	 */
	@Override
	public boolean checkValid(GameState p_gameState) {
		Country l_country = d_playerInitiator.getD_coutriesOwned().stream()
						.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountryName.toString()))
				.findFirst().orElse(null);
		return l_country != null;
	}

	/**
	 * Prints the deploy order.
	 */
	@Override
	public void printOrder() {
		this.d_orderExecutionLog = "\n---------- Deploy order issued by player " + this.d_playerInitiator.getPlayerName()+" ----------\n"+System.lineSeparator()+"Deploy " + this.d_numberOfArmiesToPlace + " armies to " + this.d_targetCountryName;
		System.out.println(this.d_orderExecutionLog);
	}

	/**
	 * Returns the order execution log for the deploy order.
	 *
	 * @return The order execution log for the deploy order.
	 */
	@Override
	public String orderExecutionLog() {
		return d_orderExecutionLog;
	}

	/**
	 * Sets the order execution log and prints it based on the log type.
	 *
	 * @param p_orderExecutionLog The order execution log.
	 * @param p_logType           The type of the log ('error' or 'default').
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
	 * Returns the name of the deploy order.
	 *
	 * @return The name of the deploy order.
	 */
	@Override
	public String getOrderName() {
		return "deploy";
	}
}
