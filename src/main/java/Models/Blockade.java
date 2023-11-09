package Models;

import Utils.CommonUtil;

/**
 * The `Blockade` class represents a card that can be used in the game to create a defensive blockade
 * in a target country.
 */
public class Blockade implements Card {

    /**
     * The player who initiated the blockade.
     */
    ModelPlayer d_initiatorPlayer;

    /**
     * The ID of the target country where the blockade is created.
     */
    String d_countryIdTarget;

    /**
     * Log of the execution order.
     */
    String d_executionOrderLog;

    /**
     * Constructs a `Blockade` card with the initiator player and the target country.
     *
     * @param p_playerInitiator The player who initiates the blockade.
     * @param p_targetCountry   The ID of the target country for the blockade.
     */
    public Blockade(ModelPlayer p_playerInitiator, String p_targetCountry) {
        this.d_initiatorPlayer = p_playerInitiator;
        this.d_countryIdTarget = p_targetCountry;
    }

    @Override
    public boolean valid(GameState p_stateGame) {
        // Validates whether the target country belongs to the player who executed the order or not.
        ModelCountry l_countryValidate = d_initiatorPlayer.getD_coutriesOwned().stream()
                .filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_countryIdTarget)).findFirst()
                .orElse(null);

        if (CommonUtil.isNullObject(l_countryValidate)) {
            this.setD_orderExecutionLog(this.currentOrder() + " is not executed since Target country : "
                    + this.d_countryIdTarget + " given in blockade command does not belong to the player : "
                    + d_initiatorPlayer.getPlayerName()
                    + " The card will have no effect, and you don't get the card back.", "error");
            p_stateGame.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
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
    public void execute(GameState p_stateGame) {
        if (valid(p_stateGame)) {
            ModelCountry l_countryIdTarget = p_stateGame.getD_map().getCountryByName(d_countryIdTarget);
            int l_armyCountTargetCountry = l_countryIdTarget.getD_armies() == 0 ? 1
                    : l_countryIdTarget.getD_armies();
            l_countryIdTarget.setD_armies(l_armyCountTargetCountry * 3);

            d_initiatorPlayer.getD_coutriesOwned().remove(l_countryIdTarget);

            ModelPlayer l_playerObject = p_stateGame.getD_playersList().stream()
                    .filter(l_pl -> l_pl.getPlayerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);

            if (!CommonUtil.isNullObject(l_playerObject)) {
                l_playerObject.getD_coutriesOwned().add(l_countryIdTarget);
                System.out.println("Neutral territory: " + l_countryIdTarget.getD_countryName() + " assigned to the Neutral Player.");
            }

            d_initiatorPlayer.removeCard("blockade");
            this.setD_orderExecutionLog("\nPlayer : " + this.d_initiatorPlayer.getPlayerName()
                    + " is executing a defensive blockade on Country :  " + l_countryIdTarget.getD_countryName()
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
