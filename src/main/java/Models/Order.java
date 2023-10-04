package Models;

import Logger.ConsoleLogger;

public class Order {

    ConsoleLogger consoleLogger = new ConsoleLogger();
    
    String d_orderAction;
	String d_targetCountryName;
	String d_sourceCountryName;
	Integer d_numberOfArmiesToPlace;
	Order orderObj;

	
	public Order() {
	}

	public Order(String p_orderAction, String p_targetCountryName, Integer p_numberOfArmiesToPlace) {
		this.d_orderAction = p_orderAction;
		this.d_targetCountryName = p_targetCountryName;
		this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
	}

    public void setD_orderAction(String p_orderAction) {
		this.d_orderAction = p_orderAction;
	}

	public void setD_targetCountryName(String p_targetCountryName) {
		this.d_targetCountryName = p_targetCountryName;
	}

	public void setD_sourceCountryName(String p_sourceCountryName) {
		this.d_sourceCountryName = p_sourceCountryName;
	}

	public void setD_numberOfArmiesToPlace(Integer p_numberOfArmiesToPlace) {
		this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
	}

    public String getD_targetCountryName() {
		return d_targetCountryName;
	}

	public String getD_orderAction() {
		return d_orderAction;
	}

	public String getD_sourceCountryName() {
		return d_sourceCountryName;
	}

	public Integer getD_numberOfArmiesToPlace() {
		return d_numberOfArmiesToPlace;
	}

    public void execute(GameState p_gameState, Player p_player) {
		if ("deploy".equals(this.d_orderAction)) {
			if (this.validateDeployOrderCountry(p_player, this)) {
				this.deployOrderExecution(this, p_gameState, p_player);
				consoleLogger.writeLog("\nOrder has been executed successfully. " + this.getD_numberOfArmiesToPlace()
						+ " number of armies has been deployed to country : "
						+ this.getD_targetCountryName());
			} else {
				consoleLogger.writeLog(
						"\nOrder is not executed as the target country given in the deploy command doesn't belong to player : "
								+ p_player.getPlayerName());
			}
		} else {
			consoleLogger.writeLog("Order was not executed due to an invalid Order Command");
		}
	}

    private void deployOrderExecution(Order p_order, GameState p_gameState, Player p_player) {
		for (Country l_country : p_gameState.getD_map().getD_countries()) {
			if (l_country.getD_countryName().equalsIgnoreCase(p_order.getD_targetCountryName())) {
				Integer l_armiesToUpdate = l_country.getD_armies() == null ? p_order.getD_numberOfArmiesToPlace()
						: l_country.getD_armies() + p_order.getD_numberOfArmiesToPlace();
				l_country.setD_armies(l_armiesToUpdate);
			}
		}
	}

    public boolean validateDeployOrderCountry(Player p_player, Order p_order) {
		Country l_country = p_player.getD_coutriesOwned().stream()
				.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(p_order.getD_targetCountryName())).findFirst()
				.orElse(null);
		return l_country != null;
	}
}
