package Models;

import java.io.Serializable;

public class ModelDeploy implements Order, Serializable {

		String d_targetCountryName;

		Integer d_numberOfArmiesToPlace;

		ModelPlayer d_playerInitiator;

		String d_orderExecutionLog;

		public ModelDeploy(ModelPlayer p_playerInitiator, String p_targetCountry, Integer p_numberOfArmiesToPlace) {
		this.d_targetCountryName = p_targetCountry;
		this.d_playerInitiator = p_playerInitiator;
		this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
	}

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

		@Override
	public boolean checkValid(GameState p_gameState) {
		Country l_country = d_playerInitiator.getD_coutriesOwned().stream()
						.filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountryName.toString()))
				.findFirst().orElse(null);
		return l_country != null;
	}

		@Override
	public void printOrder() {
		this.d_orderExecutionLog = "\n---------- Deploy order issued by player " + this.d_playerInitiator.getPlayerName()+" ----------\n"+System.lineSeparator()+"Deploy " + this.d_numberOfArmiesToPlace + " armies to " + this.d_targetCountryName;
		System.out.println(this.d_orderExecutionLog);
	}

		@Override
	public String orderExecutionLog() {
		return d_orderExecutionLog;
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
	public String getOrderName() {
		return "deploy";
	}
}
