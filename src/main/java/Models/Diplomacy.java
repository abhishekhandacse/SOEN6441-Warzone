package Models;

import Services.PlayerService;

/**
 * The `Diplomacy` class represents a card that allows players to negotiate attacks with other players.
 */
public class Diplomacy implements Card {

    /**
     * The player who issued the diplomacy request.
     */
    ModelPlayer d_sourcePlayer;

    /**
     * The name of the target player for the diplomacy request.
     */
    String d_destPlayer;

    /**
     * Log of the execution order.
     */
    String d_executionOrderLog;

    /**
     * Constructs a `Diplomacy` card with the target player and the issuing player.
     *
     * @param p_targetPlayer   The name of the target player for the diplomacy request.
     * @param p_IssuingPlayer  The player who issues the diplomacy request.
     */
    public Diplomacy(String p_targetPlayer, ModelPlayer p_IssuingPlayer) {
        this.d_destPlayer = p_targetPlayer;
        this.d_sourcePlayer = p_IssuingPlayer;
    }

    @Override
    public boolean valid(GameState p_gameState) {
        // Diplomacy card is always valid.
        return true;
    }

    /**
     * Prints the diplomacy order information.
     */
    public void printOrder() {
        this.d_executionOrderLog = "==========Diplomacy order issued by player " + this.d_sourcePlayer.getPlayerName()
                + "==========" + System.lineSeparator() + "Request to negotiate attacks from "
                + this.d_destPlayer;
        System.out.println(System.lineSeparator() + this.d_executionOrderLog);
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
        this.setD_orderExecutionLog("Negotiation with " + d_destPlayer + " approached by " + d_sourcePlayer.getPlayerName() + " successful!", "default");
        p_gameState.updateLog(d_executionOrderLog, "effect");
    }

    /**
     * Sets the execution order log.
     *
     * @param p_orderExecutionLog The log message for the execution of the order.
     * @param p_logType           The type of log (e.g., "error" or other).
     */
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
        if (!p_gameState.getD_playersList().contains(l_targetPlayer)) {
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
