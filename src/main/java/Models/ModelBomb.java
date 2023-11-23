package Models;

import Utils.CommonUtil;

import java.io.Serializable;

public class ModelBomb implements Card, Serializable {

		ModelPlayer d_playerInitiator;




		String d_targetCountryID;

		String d_orderExecutionLog;

		public ModelBomb(ModelPlayer p_playerInitiator, String p_targetCountry) {
		this.d_playerInitiator = p_playerInitiator;
		this.d_targetCountryID = p_targetCountry;
	}

		@Override
	public void execute(GameState p_gameState) {
		if (checkValid(p_gameState)) {
			Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryID);
			int l_noOfArmiesOnTargetCountry = Math.max(1, l_targetCountry.getD_armies());
			int l_newArmies = (int) Math.floor(l_noOfArmiesOnTargetCountry / 2);
					l_targetCountry.setD_armies(l_newArmies);
			d_playerInitiator.removeCard("bomb");
			setD_orderExecutionLog(
					"\nPlayer: " + this.d_playerInitiator.getPlayerName() + " is executing Bomb card on country: "
							+ l_targetCountry.getD_countryName() + " with armies: " + l_noOfArmiesOnTargetCountry
							+ ". New armies: " + l_targetCountry.getD_armies(),
					"default");
			p_gameState.updateLog(orderExecutionLog(), "effect");
		}
	}

		@Override
	public boolean checkValid(GameState p_gameState) {
		Country l_country = d_playerInitiator.getD_coutriesOwned().stream()
				.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountryID))
				.findFirst().orElse(null);

		// Player cannot bomb own territory
		if (!CommonUtil.isNull(l_country)) {
			setD_orderExecutionLog(this.currentOrder() + " is not executed since Target country: "
					+ this.d_targetCountryID + " given in bomb command is owned by the player: "
						+ d_playerInitiator.getPlayerName() + " VALIDATES: You cannot bomb your own territory!", "error");
			p_gameState.updateLog(orderExecutionLog(), "effect");
			return false;
		}

		if (!d_playerInitiator.negotiationValidation(this.d_targetCountryID)) {
			setD_orderExecutionLog(this.currentOrder() + " is not executed as " + d_playerInitiator.getPlayerName()
					+ " has a negotiation pact with the target country's player!", "error");
			p_gameState.updateLog(orderExecutionLog(), "effect");
			return false;
			}
		return true;
	}

		private String currentOrder() {
		return "Bomb card order: " + "bomb" + " " + this.d_targetCountryID;
	}

		@Override
	public void printOrder() {
		this.d_orderExecutionLog = "----------Bomb card order issued by player "
				+ this.d_playerInitiator.getPlayerName() + "----------" + System.lineSeparator()
				+ "Creating a bomb order = " + "on country ID. " + this.d_targetCountryID;
		System.out.println(System.lineSeparator() + this.d_orderExecutionLog);

	}

		public String orderExecutionLog() {
		return this.d_orderExecutionLog;
	}

		public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
				this.d_orderExecutionLog = p_orderExecutionLog;
		if (p_logType.equals("error")) {
			System.err.println(p_orderExecutionLog);
		} else {
			System.out.println(p_orderExecutionLog);
		}
	}

		@Override
	public Boolean checkIfOrderIsValid(GameState p_currentGameState) {
		Country l_targetCountry = p_currentGameState.getD_map().getCountryByName(d_targetCountryID);
		if (l_targetCountry == null) {
			setD_orderExecutionLog("Invalid Target Country! Doesn't exist on the map!", "error");
			return false;
		}
		return true;
	}

		@Override
	public String getOrderName() {
		return "bomb";
	}
}



