package Models;

import java.util.List;

/**
 * The type State.
 */
public class GameState {


    /**
     * The map.
     */
    Map d_map;

    /**
     * The players.
     */
    List<ModelPlayer> d_playersList;

    /**
     * The unexecuted orders.
     */
    List<Order> d_unexecutedOrdersList;

     /**
      * The load command
      */
    Boolean d_loadCommand = false;

	/**
     * Log Buffer object
     */
	ModelLogBuffer d_logBuffer = new ModelLogBuffer();

    /**
     * The error.
     */
    String d_error;

    /**
     * Gets d players.
     *
     * @return the d players
     */
    public List<ModelPlayer> getD_playersList() {
        return d_playersList;
    }

    /**
     * Sets d players.
     *
     * @param p_players the p players
     */
    public void setD_playersList(List<ModelPlayer> p_players) {
        this.d_playersList = p_players;
    }

    /**
     * Gets d map.
     *
     * @return the d map
     */
    public Map getD_map() {
        return d_map;
    }

    /**
     * Sets d map.
     *
     * @param p_map the p map
     */
    public void setD_map(Map p_map) {
        this.d_map = p_map;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public String getError() {
        return d_error;
    }

    /**
     * Sets error.
     *
     * @param p_error the p error
     */
    public void setError(String p_error) {
        this.d_error = p_error;
    }

	public boolean getD_loadCommand() {
		return this.d_loadCommand;
	}

	public void setD_loadCommand() {
		this.d_loadCommand = true;
	}

    /**
     * Gets d unexecuted orders.
     *
     * @return the d unexecuted orders
     */
	public List<Order> getD_unexecutedOrdersList() {
		return d_unexecutedOrdersList;
	}

    /**
     * Sets d unexecuted orders.
     *
     * @param p_unexecutedOrders the p unexecuted orders
     */
	public void setD_unexecutedOrdersList(List<Order> p_unexecutedOrders) {
		this.d_unexecutedOrdersList = p_unexecutedOrders;
	}

	public String getRecentLog() {
		return d_logBuffer.getD_logMessage();
	}

	public void updateLog(String p_logMessage, String p_logType) {
		d_logBuffer.currentLog(p_logMessage, p_logType);
	}

}
