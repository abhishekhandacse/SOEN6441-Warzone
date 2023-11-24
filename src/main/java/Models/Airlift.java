package Models;
import java.io.Serializable;

/**
 * Class handles the execute and validation of Airlift Validate.
 *
 */
public class Airlift implements Card, Serializable {

	/**
	 * Records the execution log.
	 */
	String d_logOfOrderExecution;

	/**
	 * Country to drop armies to.
	 */
	String d_nameOfTargetCountry;

	/**
	 * Number of armies to Airlift.
	 */
	Integer d_quantityOfArmy;

	/**
	 * Player the card is owned by.
	 */

	ModelPlayer d_cardHoldingPlayer;

	/**
	 * Country to take armies from.
	 */
	String d_nameOfSourceCountry;

	/**
	 * Constructor Initialising the card parameters.
	 *
	 * @param p_sourceCountryName source country to airlift from.
	 * @param p_targetCountryName target country to drop off armies to.
	 * @param p_noOfArmies        No of armies to airlift
	 * @param p_player            player owning the card
	 */
	public Airlift(String p_sourceCountryName, String p_targetCountryName, Integer p_noOfArmies, ModelPlayer p_player) {
		this.d_nameOfTargetCountry = p_targetCountryName;
		this.d_quantityOfArmy = p_noOfArmies;
		this.d_cardHoldingPlayer = p_player;
		this.d_nameOfSourceCountry = p_sourceCountryName;
	}

	/**
	 * Executes the order.
	 */
	@Override
	public void execute(GameState p_internalGameState) {
		if (checkValid(p_internalGameState)) {
			Country l_sourceCountry = p_internalGameState.getD_map().getCountryByName(d_nameOfSourceCountry);
			Country l_targetCountry = p_internalGameState.getD_map().getCountryByName(d_nameOfTargetCountry);
			Integer l_updatedTargetArmies = l_targetCountry.getD_armies() + this.d_quantityOfArmy;
			Integer l_updatedSourceArmies = l_sourceCountry.getD_armies() - this.d_quantityOfArmy;
			l_targetCountry.setD_armies(l_updatedTargetArmies);
			l_sourceCountry.setD_armies(l_updatedSourceArmies);
			d_cardHoldingPlayer.removeCard("airlift");
			this.setD_orderExecutionLog("Airlift Operation from "+ d_nameOfSourceCountry + " to "+ d_nameOfTargetCountry +" successful!", "default");
			p_internalGameState.updateLog(d_logOfOrderExecution, "effect");
		} else {
			this.setD_orderExecutionLog("Cannot Complete Execution of given Airlift Command!", "error");
			p_internalGameState.updateLog(d_logOfOrderExecution, "effect");
		}
	}

	/**
	 * Checks the validation before executing orders.
	 */
	@Override
	public boolean checkValid(GameState p_internalGameState) {
		Country l_nameOfSourceCountry = d_cardHoldingPlayer.getD_coutriesOwned().stream()
				.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_nameOfSourceCountry))
				.findFirst().orElse(null);
		if (l_nameOfSourceCountry == null) {
			this.setD_orderExecutionLog(
					this.currentOrder() + " is not executed since Source country : " + this.d_nameOfSourceCountry
							+ " given in card order does not belongs to the player : " + d_cardHoldingPlayer.getPlayerName(),
					"error");
			p_internalGameState.updateLog(orderExecutionLog(), "effect");
			return false;
		}
		Country l_targetCountry = d_cardHoldingPlayer.getD_coutriesOwned().stream()
				.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_nameOfTargetCountry))
				.findFirst().orElse(null);
		if (l_targetCountry == null) {
			this.setD_orderExecutionLog(
					this.currentOrder() + " is not executed since Target country : " + this.d_nameOfSourceCountry
							+ " given in card order does not belongs to the player : " + d_cardHoldingPlayer.getPlayerName(),
					"error");
			p_internalGameState.updateLog(orderExecutionLog(), "effect");
			return false;
		}
		if (this.d_quantityOfArmy > l_nameOfSourceCountry.getD_armies()) {
			this.setD_orderExecutionLog(this.currentOrder()
					+ " is not executed as armies given in card order exceeds armies of source country : "
					+ this.d_nameOfSourceCountry, "error");
			p_internalGameState.updateLog(orderExecutionLog(), "effect");
			return false;
		}
		return true;
	}

	/**
	 * Prints the Order.
	 */
	@Override
	public void printOrder() {
		this.d_logOfOrderExecution = "########### Airlift order issued by player " + this.d_cardHoldingPlayer.getPlayerName()
				+ "############## " + System.lineSeparator() + "Move " + this.d_quantityOfArmy + " armies from "
				+ this.d_nameOfSourceCountry + " to " + this.d_nameOfTargetCountry;
		System.out.println(System.lineSeparator()+this.d_logOfOrderExecution);
	}

	/**
	 * Set the order execution Log.
	 *
	 * @return String of Log.
	 */
	@Override
	public String orderExecutionLog() {
		return this.d_logOfOrderExecution;
	}

	/**
	 * Prints and Sets the order execution log.
	 *
	 * @param p_executionOrderLog String to be set as log
	 * @param p_typeOfLog           type of log : error, default
	 */
	public void setD_orderExecutionLog(String p_executionOrderLog, String p_typeOfLog) {
		this.d_logOfOrderExecution = p_executionOrderLog;
		if (p_typeOfLog.equals("error")) {
			System.err.println(p_executionOrderLog);
		} else {
			System.out.println(p_executionOrderLog);
		}
	}


	/**
	 * Return order name.
	 *
	 * @return String
	 */
	@Override
	public String getOrderName() {
		return "airlift";
	}


	@Override
	public Boolean checkValidOrder(GameState p_internalGameState) {
		Country l_sourceCountry = p_internalGameState.getD_map().getCountryByName(d_nameOfSourceCountry);
		Country l_targetCountry = p_internalGameState.getD_map().getCountryByName(d_nameOfTargetCountry);
		if (l_sourceCountry == null) {
			this.setD_orderExecutionLog("Invalid Source Country name! This country Doesn't exist on the map!", "error");
			p_internalGameState.updateLog(orderExecutionLog(), "effect");
			return false;
		}
		if (l_targetCountry == null) {
			this.setD_orderExecutionLog("Invalid Target Country! Doesn't exist on the map!", "error");
			p_internalGameState.updateLog(orderExecutionLog(), "effect");
			return false;
		}
		return true;
	}


	/**
	 * Gives current advance order which is being executed.
	 *
	 * @return advance order command
	 */
	private String currentOrder() {
		return "Airlift Order : " + "airlift" + " " + this.d_nameOfSourceCountry + " " + this.d_nameOfTargetCountry + " "
				+ this.d_quantityOfArmy;
	}

}