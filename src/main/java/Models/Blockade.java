package Models;
import Utils.CommonUtil;
import java.io.Serializable;

public class Blockade implements Card, Serializable {

    ModelPlayer d_playerInitiator;
	String d_targetCountryID;
	String d_orderExecutionLog;

    public Blockade(ModelPlayer p_playerInitiator, String p_targetCountry) {
		this.d_playerInitiator = p_playerInitiator;
		this.d_targetCountryID = p_targetCountry;
	}

    @Override
	public String getOrderName() {
		return "blockade";
	}

	private String currentOrder() {
		return "Blockade card order : " + "blockade" + " " + this.d_targetCountryID;
	}

	public String orderExecutionLog() {
		return this.d_orderExecutionLog;
	}

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

	// Print Blockade order
	@Override
	public void printOrder() {
		this.d_orderExecutionLog = "----------Blockade card order issued by player "
				+ this.d_playerInitiator.getPlayerName() + "----------" + System.lineSeparator()
				+ "Creating a defensive blockade " + "on country ID: " + this.d_targetCountryID;
		System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
	}

	public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
		this.d_orderExecutionLog = p_orderExecutionLog;
		if (p_logType.equals("error")) {
			System.err.println(p_orderExecutionLog);
		} else {
			System.out.println(p_orderExecutionLog);
		}
	}

    /**
     * Returns the current order as a string.
     *
     * @return The current order.
     */
    private String currentOrder() {
        return "Card Order for Blockage is : " + "blockade" + " " + this.d_countryIdTarget;
    }

    /**
     * Gets the order execution log.
     *
     * @return The order execution log.
     */
    public String orderExecutionLog() {
        return this.d_executionOrderLog;
    }
}
