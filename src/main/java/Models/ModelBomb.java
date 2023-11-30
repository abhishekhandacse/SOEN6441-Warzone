/**
 * The {@code ModelBomb} class represents a Bomb card in a game, allowing a player to execute a bomb order on a target country,
 * reducing the number of armies on that country by half (rounded down) if the order is valid.
 *
 * @version 1.0
 */
package Models;

import Utils.CommonUtil;

import java.io.Serializable;

/**
 * Represents a Bomb card in the game.
 * This card allows a player to execute a bomb order on a target country, reducing the number of armies by half if the order is valid.
 */
public class ModelBomb implements Card, Serializable {

    /** The player who initiated the bomb card. */
    ModelPlayer d_playerInitiator;

    /** The ID of the target country for the bomb card. */
    String d_targetCountryID;

    /** The log of order execution for the bomb card. */
    String d_orderExecutionLog;

    /**
     * Constructs a new Bomb card with the specified initiator player and target country.
     *
     * @param p_playerInitiator The player who initiated the bomb card.
     * @param p_targetCountry   The ID of the target country for the bomb card.
     */
    public ModelBomb(ModelPlayer p_playerInitiator, String p_targetCountry) {
        this.d_playerInitiator = p_playerInitiator;
        this.d_targetCountryID = p_targetCountry;
    }

    /**
     * Executes the bomb card, reducing the number of armies on the target country by half if the order is valid.
     *
     * @param p_gameState The current game state.
     */
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

    /**
     * Checks if the bomb card order is valid.
     *
     * @param p_gameState The current game state.
     * @return {@code true} if the order is valid, {@code false} otherwise.
     */
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

    /**
     * Returns the current bomb card order.
     *
     * @return The current bomb card order.
     */
    private String currentOrder() {
        return "Bomb card order: " + "bomb" + " " + this.d_targetCountryID;
    }

    /**
     * Prints the bomb card order.
     */
    @Override
    public void printOrder() {
        this.d_orderExecutionLog = "----------Bomb card order issued by player "
                + this.d_playerInitiator.getPlayerName() + "----------" + System.lineSeparator()
                + "Creating a bomb order = " + "on country ID. " + this.d_targetCountryID;
        System.out.println(System.lineSeparator() + this.d_orderExecutionLog);

    }

    /**
     * Returns the order execution log for the bomb card.
     *
     * @return The order execution log for the bomb card.
     */
    public String orderExecutionLog() {
        return this.d_orderExecutionLog;
    }

    /**
     * Sets the order execution log and prints it based on the log type.
     *
     * @param p_orderExecutionLog The order execution log.
     * @param p_logType           The type of the log ('error' or 'default').
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
     * Checks if the bomb card order is valid in the given game state.
     *
     * @param p_currentGameState The current game state.
     * @return {@code true} if the order is valid, {@code false} otherwise.
     */
    @Override
    public Boolean checkIfOrderIsValid(GameState p_currentGameState) {
        Country l_targetCountry = p_currentGameState.getD_map().getCountryByName(d_targetCountryID);
        if (l_targetCountry == null) {
            setD_orderExecutionLog("Invalid Target Country! Doesn't exist on the map!", "error");
            return false;
        }
        return true;
    }

    /**
     * Returns the name of the bomb card order.
     *
     * @return The name of the bomb card order.
     */
    @Override
    public String getOrderName() {
        return "bomb";
    }
}



