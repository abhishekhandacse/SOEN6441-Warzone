package Models;

import Services.PlayerService;

public class Diplomacy implements Card {



	ModelPlayer d_sourcePlayer;

	String d_destPlayer;



	String d_executionOrderLog;



	public Diplomacy(String p_targetPlayer, ModelPlayer p_IssuingPlayer){
		this.d_destPlayer = p_targetPlayer;
		this.d_sourcePlayer = p_IssuingPlayer;
	}





	@Override
	public boolean valid(GameState p_gameState) {
		return true;
	}

	public void printOrder() {
		this.d_executionOrderLog = "==========Diplomacy order issued by player " + this.d_sourcePlayer.getPlayerName()
				+ "==========" + System.lineSeparator() + "Request to " + " negotiate attacks from "
				+ this.d_destPlayer;
		System.out.println(System.lineSeparator()+this.d_executionOrderLog);
	}


	@Override
	public String orderExecutionLog() {
		return this.d_executionOrderLog;
	}

	@Override
	public void execute(GameState p_gameState) {
		PlayerService l_playerService = new PlayerService();
		ModelPlayer l_targetPlayer = l_playerService.findPlayerByName(d_destPlayer, p_gameState);
		l_targetPlayer.addPlayerNegotiation(d_sourcePlayer);
		d_sourcePlayer.addPlayerNegotiation(l_targetPlayer);
		d_sourcePlayer.removeCard("negotiate");
		this.setD_orderExecutionLog("Negotiation with "+ d_destPlayer+ " approached by "+d_sourcePlayer.getPlayerName()+" successful!", "default");
		p_gameState.updateLog(d_executionOrderLog, "effect");
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
	public Boolean validOrderCheck(GameState p_gameState) {
		PlayerService l_playerService = new PlayerService();
		ModelPlayer l_targetPlayer = l_playerService.findPlayerByName(d_destPlayer, p_gameState);
		if(!p_gameState.getD_players().contains(l_targetPlayer)){
			this.setD_orderExecutionLog("Player to negotiate doesn't exist!", "error");
			p_gameState.updateLog(orderExecutionLog(), "effect");
			return false;
		}
		return true;
	}

	private String currentOrder() {
		return "Diplomacy Order : " + "negotiate" + " " + this.d_destPlayer;
	}


	@Override
	public String getOrderName() {
		return "diplomacy";
	}
}
