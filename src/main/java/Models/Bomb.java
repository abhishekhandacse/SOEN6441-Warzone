package Models;

import Utils.CommonUtil;

public class Bomb implements Card{
    ModelPlayer d_player;

	String d_targetCountryID;

	String d_logOrderExecution;

	//Constructor
	public Bomb(ModelPlayer p_player, String p_targetCountry) {
		this.d_player = p_player;
		this.d_targetCountryID = p_targetCountry;
	}

    	@Override
	public String getOrderName() {
		return "bomb";
	}

	@Override
	public void printOrder() {
		this.d_logOrderExecution = "----------Bomb card order issued by player "
				+ this.d_player.getPlayerName() + "----------" + System.lineSeparator()
				+ "Creating a bomb order = " + "on country ID. " + this.d_targetCountryID;
		System.out.println(System.lineSeparator() + this.d_logOrderExecution);

	}

	private String currentOrder() {
		return "Bomb card order : " + "bomb" + " " + this.d_targetCountryID;
	}

	@Override
	public boolean valid(GameState p_gameState) {
		Country l_country = d_player.getD_coutriesOwned().stream()
				.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountryID)).findFirst()
				.orElse(null);

		// Player cannot bomb own territory
		if (!CommonUtil.isNullObject(l_country)) {
			this.setD_orderExecutionLog(this.currentOrder() + " is not executed since Target country : "
					+ this.d_targetCountryID + " given in bomb command is owned by the player : "
					+ d_player.getPlayerName() + " VALIDATES:- You cannot bomb your own territory!", "error");
			p_gameState.updateLog(orderExecutionLog(), "effect");
			return false;
		}

		if(!d_player.negotiationValidation(this.d_targetCountryID)){
			this.setD_orderExecutionLog(this.currentOrder() + " is not executed as "+ d_player.getPlayerName()+ " has negotiation pact with the target country's player!", "error");
			p_gameState.updateLog(orderExecutionLog(), "effect");
			return false;
		}
		return true;
	}

	@Override
	public Boolean validOrderCheck(GameState p_gameState) {
		Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryID);
		if (l_targetCountry == null) {
			this.setD_orderExecutionLog("Invalid Target Country! Doesn't exist on the map!", "error");
			return false;
		}
		return true;
	}

	public String orderExecutionLog() {
		return this.d_logOrderExecution;
	}

	public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
		this.d_logOrderExecution = p_orderExecutionLog;
		if (p_logType.equals("error")) {
			System.err.println(p_orderExecutionLog);
		} else {
			System.out.println(p_orderExecutionLog);
		}
	}

	@Override
	public void execute(GameState p_gameState) {
		if (valid(p_gameState)) {
			Country l_targetCountryID = p_gameState.getD_map().getCountryByName(d_targetCountryID);
			Integer l_armiesCountOnTargetCountry = l_targetCountryID.getD_armies() == 0 ? 1
					: l_targetCountryID.getD_armies();
			Integer l_newArmies = (int) Math.floor(l_armiesCountOnTargetCountry / 2);
			l_targetCountryID.setD_armies(l_newArmies);
			d_player.removeCard("bomb");
			this.setD_orderExecutionLog(
					"\nPlayer : " + this.d_player.getPlayerName() + " is executing Bomb card on country :  "
							+ l_targetCountryID.getD_countryName() + " with armies :  " + l_armiesCountOnTargetCountry
							+ ". New armies: " + l_targetCountryID.getD_armies(),
					"default");
			p_gameState.updateLog(orderExecutionLog(), "effect");
		}
	}

    
}
