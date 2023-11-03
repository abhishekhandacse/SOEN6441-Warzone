package Models;

import Logger.ConsoleLogger;

/**
 * The type Order.
 */
public class Deploy implements Order {

    /**
     * name of the target country.
     */
    String d_nameOfTargetCountry;

    /**
     * number of armies to be placed.
     */
    Integer d_quantityOfArmiesToPlace;

    /**
     * Player Initiator.
     */
    ModelPlayer d_initiatingPlayer;

    /**
     * Sets the Log containing Information about orders.
     */
    String d_logOfOrderExecution;

    /**
     * The constructor receives all the parameters necessary to implement the order.
     * These are then encapsulated in the order.
     *
     * @param p_playerInitiator       player that created the order
     * @param p_targetCountry         country that will receive the new armies
     * @param p_numberOfArmies number of armies to be added
     */
    public Deploy(ModelPlayer p_playerInitiator, String p_targetCountry, Integer p_numberOfArmies) {
        this.d_initiatingPlayer = p_playerInitiator;
        this.d_quantityOfArmiesToPlace = p_numberOfArmies;
        this.d_nameOfTargetCountry = p_targetCountry;
    }

    /**
     * Prints deploy Order.
     */
    @Override
    public void printOrder() {
        this.d_logOfOrderExecution = "\n---------- Deploy order issued by player " + this.d_initiatingPlayer.getPlayerName()+" ----------\n"+System.lineSeparator()+"Deploy " + this.d_quantityOfArmiesToPlace + " armies to " + this.d_nameOfTargetCountry;
        System.out.println(this.d_logOfOrderExecution);
    }

    /**
     * Validates whether country given for deploy belongs to players countries or
     * not.
     */
    @Override
    public boolean valid(GameState p_gameState) {
        ModelCountry l_country = d_initiatingPlayer.getD_coutriesOwned().stream()
                .filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_nameOfTargetCountry.toString()))
                .findFirst().orElse(null);
        return l_country != null;
    }



    /**
     * Executes the deploy order.
     *
     * @param p_internalGameState current state of the game.
     */
    @Override
    public void execute(GameState p_internalGameState) {

        if (valid(p_internalGameState)) {
            for (ModelCountry l_country : p_internalGameState.getD_map().getD_allCountries()) {
                if (l_country.getD_countryName().equalsIgnoreCase(this.d_nameOfTargetCountry)) {
                    Integer l_armiesToUpdate = l_country.getD_armies() == null ? this.d_quantityOfArmiesToPlace
                            : l_country.getD_armies() + this.d_quantityOfArmiesToPlace;
                    l_country.setD_armies(l_armiesToUpdate);
                    this.setD_orderExecutionLog(+l_armiesToUpdate
                                    + " armies have been deployed successfully on country : " + l_country.getD_countryName(),
                            "default");
                }
            }

        } else {
            this.setD_orderExecutionLog("Deploy Order = " + "deploy" + " " + this.d_nameOfTargetCountry + " "
                    + this.d_quantityOfArmiesToPlace + " is not executed since Target country: "
                    + this.d_nameOfTargetCountry + " given in deploy command does not belongs to the player : "
                    + d_initiatingPlayer.getPlayerName(), "error");
            d_initiatingPlayer.setD_noOfUnallocatedArmies(
                    d_initiatingPlayer.getD_noOfUnallocatedArmies() + this.d_quantityOfArmiesToPlace);
        }
        p_internalGameState.updateLog(orderExecutionLog(), "effect");
    }

    /**
     * Gets order execution log.
     */
    @Override
    public String orderExecutionLog() {
        return d_logOfOrderExecution;
    }

    /**
     * Return order name.
     *
     * @return String
     */
    @Override
    public String getOrderName() {
        return "deploy";
    }

    /**
     * Prints and Sets the order execution log.
     *
     * @param p_orderExecutionLog String to be set as log
     * @param p_logType           type of log : error, default
     */
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
        this.d_logOfOrderExecution = p_orderExecutionLog;
        if (p_logType.equals("error")) {
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }
}
