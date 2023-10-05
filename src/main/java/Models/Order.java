package Models;

import Logger.ConsoleLogger;

/**
 * The type Order.
 */
public class Order {

    /**
     * The Console logger.
     */
    ConsoleLogger d_consoleLogger = new ConsoleLogger();

    /**
     * The D order action.
     */
    String d_orderAction;
    /**
     * The D target country name.
     */
    String d_targetCountryName;
    /**
     * The D source country name.
     */
    String d_sourceCountryName;
    /**
     * The D number of armies to place.
     */
    Integer d_numberOfArmiesToPlace;
    /**
     * The Order obj.
     */
    Order orderObj;


    /**
     * Instantiates a new Order.
     */
    public Order() {
    }

    /**
     * Instantiates a new Order.
     *
     * @param p_orderAction           the p order action
     * @param p_targetCountryName     the p target country name
     * @param p_numberOfArmiesToPlace the p number of armies to place
     */
    public Order(String p_orderAction, String p_targetCountryName, Integer p_numberOfArmiesToPlace) {
        this.d_orderAction = p_orderAction;
        this.d_targetCountryName = p_targetCountryName;
        this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
    }

    /**
     * Gets d target country name.
     *
     * @return the d target country name
     */
    public String getD_targetCountryName() {
        return d_targetCountryName;
    }

    /**
     * Sets d target country name.
     *
     * @param p_targetCountryName the p target country name
     */
    public void setD_targetCountryName(String p_targetCountryName) {
        this.d_targetCountryName = p_targetCountryName;
    }

    /**
     * Gets d order action.
     *
     * @return the d order action
     */
    public String getD_orderAction() {
        return d_orderAction;
    }

    /**
     * Sets d order action.
     *
     * @param p_orderAction the p order action
     */
    public void setD_orderAction(String p_orderAction) {
        this.d_orderAction = p_orderAction;
    }

    /**
     * Gets d source country name.
     *
     * @return the d source country name
     */
    public String getD_sourceCountryName() {
        return d_sourceCountryName;
    }

    /**
     * Sets d source country name.
     *
     * @param p_sourceCountryName the p source country name
     */
    public void setD_sourceCountryName(String p_sourceCountryName) {
        this.d_sourceCountryName = p_sourceCountryName;
    }

    /**
     * Gets d number of armies to place.
     *
     * @return the d number of armies to place
     */
    public Integer getD_numberOfArmiesToPlace() {
        return d_numberOfArmiesToPlace;
    }

    /**
     * Sets d number of armies to place.
     *
     * @param p_numberOfArmiesToPlace the p number of armies to place
     */
    public void setD_numberOfArmiesToPlace(Integer p_numberOfArmiesToPlace) {
        this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
    }

    /**
     * Execute.
     *
     * @param p_gameState the p game state
     * @param p_player    the p player
     */
    public void execute(State p_gameState, Player p_player) {
        if ("deploy".equals(this.d_orderAction)) {
            if (this.validateDeployOrderCountry(p_player, this)) {
                this.deployOrderExecution(this, p_gameState, p_player);
                d_consoleLogger.writeLog("\nOrder has been executed successfully. " + this.getD_numberOfArmiesToPlace() + " number of armies has been deployed to country : " + this.getD_targetCountryName());
            } else {
                d_consoleLogger.writeLog("\nOrder is not executed as the target country given in the deploy command doesn't belong to player : " + p_player.getPlayerName());
            }
        } else {
            d_consoleLogger.writeLog("Order was not executed due to an invalid Order Command");
        }
    }

    private void deployOrderExecution(Order p_order, State p_gameState, Player p_player) {
        for (Country l_country : p_gameState.getD_map().getD_countries()) {
            if (l_country.getD_countryName().equalsIgnoreCase(p_order.getD_targetCountryName())) {
                Integer l_armiesToUpdate = l_country.getD_armies() == null ? p_order.getD_numberOfArmiesToPlace() : l_country.getD_armies() + p_order.getD_numberOfArmiesToPlace();
                l_country.setD_armies(l_armiesToUpdate);
            }
        }
    }

    /**
     * Validate deploy order country boolean.
     *
     * @param p_player the p player
     * @param p_order  the p order
     * @return the boolean
     */
    public boolean validateDeployOrderCountry(Player p_player, Order p_order) {
        Country l_country = p_player.getD_coutriesOwned().stream().filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(p_order.getD_targetCountryName())).findFirst().orElse(null);
        return l_country != null;
    }
}
