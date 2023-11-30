package Models;

import Services.PlayerService;

import java.io.Serializable;

/**
 * Handles diplomacy command.
 *
 */
public class Diplomacy implements Card, Serializable {

    /**
     * Player issuing the negotiate order.
     */
    ModelPlayer d_modelIssuingPlayer;

    /**
     * Records the execution log.
     */
    String d_orderExecutionLog;

    /**
     * Target player
     */
    String d_playerTarget;



    /**
     * Constructor to create diplomacy order.
     *
     * @param p_targetPlayer target player to negotiate with
     * @param p_IssuingPlayer negotiate issuing player.
     */
    public Diplomacy(String p_targetPlayer, ModelPlayer p_IssuingPlayer){
        this.d_playerTarget = p_targetPlayer;
        this.d_modelIssuingPlayer = p_IssuingPlayer;
    }

    /**
     * Prints orders.
     */
    public void printOrder() {
        this.d_orderExecutionLog = "----------Diplomacy order issued by player " + this.d_modelIssuingPlayer.getPlayerName()
                + "----------" + System.lineSeparator() + "Request to " + " negotiate attacks from "
                + this.d_playerTarget;
        System.out.println(System.lineSeparator()+this.d_orderExecutionLog);
    }

    /**
     * Executing the negotiate order.
     */
    @Override
    public void execute(GameState p_currentGameState) {
        PlayerService l_playerService = new PlayerService();
        ModelPlayer l_targetPlayer = l_playerService.findPlayerByName(d_playerTarget, p_currentGameState);
        l_targetPlayer.addPlayerNegotiation(d_modelIssuingPlayer);
        d_modelIssuingPlayer.addPlayerNegotiation(l_targetPlayer);
        d_modelIssuingPlayer.removeCard("negotiate");
        this.setD_orderExecutionLog("Negotiation with "+ d_playerTarget + " approached by "+ d_modelIssuingPlayer.getPlayerName()+" successful!", "default");
        p_currentGameState.updateLog(d_orderExecutionLog, "effect");
    }

    /**
     * checks if order is valid.
     */
    @Override
    public boolean checkValid(GameState p_gameState) {
        return true;
    }



    /**
     * sets execution log.
     */
    @Override
    public String orderExecutionLog() {
        return this.d_orderExecutionLog;
    }

    /**
     * Prints and Sets the order execution log.
     *
     * @param p_orderExecutionLog String to be set as log
     * @param p_logType           type of log : error, default
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
     * checks valid order.
     */
    @Override
    public Boolean checkIfOrderIsValid(GameState p_currentGameState) {
        PlayerService l_playerService = new PlayerService();
        ModelPlayer l_targetPlayer = l_playerService.findPlayerByName(d_playerTarget, p_currentGameState);
        if(!p_currentGameState.getD_players().contains(l_targetPlayer)){
            this.setD_orderExecutionLog("Player to negotiate doesn't exist!", "error");
            p_currentGameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
    }



    /**
     * Return order name.
     *
     * @return String
     */
    @Override
    public String getOrderName() {
        return "diplomacy";
    }

    /**
     * Gives current advance order which is being executed.
     *
     * @return advance order command
     */
    private String currentOrder() {
        return "Diplomacy Order : " + "negotiate" + " " + this.d_playerTarget;
    }


}
