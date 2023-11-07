package Models;

import Utils.CommonUtil;


public class Blockade implements Card {


	ModelPlayer d_initiatorPlayer;


	String d_countryIdTarget;

	String d_executionOrderLog;

	public Blockade(ModelPlayer p_playerInitiator, String p_targetCountry) {
		this.d_initiatorPlayer = p_playerInitiator;
		this.d_countryIdTarget = p_targetCountry;
	}


	@Override
	public boolean valid(GameState p_stateGame) {

		// Validates whether target country belongs to the Player who executed the order
		// or not
		ModelCountry l_countryValidate = d_initiatorPlayer.getD_coutriesOwned().stream()
				.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_countryIdTarget)).findFirst()
				.orElse(null);

		if (CommonUtil.isNullObject(l_countryValidate)) {
			this.setD_orderExecutionLog(this.currentOrder() + " is not executed since Target country : "
					+ this.d_countryIdTarget + " given in blockade command does not owned to the player : "
					+ d_initiatorPlayer.getPlayerName()
					+ " The card will have no affect and you don't get the card back.", "error");
			p_stateGame.updateLog(orderExecutionLog(), "effect");
			return false;
		}
		return true;
	}

	public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
		this.d_executionOrderLog = p_orderExecutionLog;
		if (p_logType.equals("error")) {
			System.err.println(p_orderExecutionLog);
		} else {
			System.out.println(p_orderExecutionLog);
		}
	}
	@Override
	public void execute(GameState p_stateGame) {
		if (valid(p_stateGame)) {
			ModelCountry l_countryIdTarget = p_stateGame.getD_map().getCountryByName(d_countryIdTarget);
			int l_armyCountTargetCountry = l_countryIdTarget.getD_armies() == 0 ? 1
					: l_countryIdTarget.getD_armies();
			l_countryIdTarget.setD_armies(l_armyCountTargetCountry * 3);


			d_initiatorPlayer.getD_coutriesOwned().remove(l_countryIdTarget);

			ModelPlayer l_playerObject = p_stateGame.getD_players().stream()
					.filter(l_pl -> l_pl.getPlayerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);

			if (!CommonUtil.isNullObject(l_playerObject)) {
				l_playerObject.getD_coutriesOwned().add(l_countryIdTarget);
				System.out.println("Neutral territory: " + l_countryIdTarget.getD_countryName() + "assigned to the Neutral Player.");
			}

			d_initiatorPlayer.removeCard("blockade");
			this.setD_orderExecutionLog("\nPlayer : " + this.d_initiatorPlayer.getPlayerName()
					+ " is executing defensive blockade on Country :  " + l_countryIdTarget.getD_countryName()
					+ " with armies :  " + l_countryIdTarget.getD_armies(), "default");
			p_stateGame.updateLog(orderExecutionLog(), "effect");
		}
	}




	@Override
	public void printOrder() {
		this.d_executionOrderLog = "==========Blockade card order issued by player "
				+ this.d_initiatorPlayer.getPlayerName() + "==========" + System.lineSeparator()
				+ "Creating a defensive blockade with armies = " + "on country ID: " + this.d_countryIdTarget;
		System.out.println(System.lineSeparator() + this.d_executionOrderLog);

	}




	@Override
	public Boolean validOrderCheck(GameState p_gameState) {
		ModelCountry l_targetCountry = p_gameState.getD_map().getCountryByName(d_countryIdTarget);
		if (l_targetCountry == null) {
			this.setD_orderExecutionLog("Target Country is Invalid! It does not exist on the map!!!!", "error");
			return false;
		}
		return true;
	}


	@Override
	public String getOrderName() {
		return "blockade";
	}


	private String currentOrder() {
		return "Card Order for Blockage is : " + "blockade" + " " + this.d_countryIdTarget;
	}


	public String orderExecutionLog() {
		return this.d_executionOrderLog;
	}

}
