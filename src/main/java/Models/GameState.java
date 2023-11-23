package Models;

import java.util.List;

/**
 * The GameState class represents the state of a game, including the map, player information,
 * unexecuted orders, error messages, and a log buffer for game events.
 */
public class GameState {

    /** 
	 * The game map. 
	 * 
	*/
    Map d_map;

    /**
	 *  An error message, if any. 
	 * */
    String d_error;

    /** 
	 * Flag indicating whether a load command has been executed. 
	 * */
    Boolean d_loadCommand = false;

    /** 
	 * A list of unexecuted orders. 
	 * */
    List<Order> d_unexecutedOrdersList;

    /** 
	 * A list of players in the game. 
	 * */
    List<ModelPlayer> d_playersList;

    /** 
	 * A log buffer for storing game events. 
	 * */
    ModelLogEntryBuffer d_logBuffer = new ModelLogEntryBuffer();

    /**
     * Get the game map.
     *
     * @return The game map.
     */
    public Map getD_map() {
        return d_map;
    }

    /**
     * Set the game map.
     *
     * @param p_map The game map to set.
     */
    public void setD_map(Map p_map) {
        this.d_map = p_map;
    }

    /**
     * Get the error message.
     *
     * @return The error message, if any.
     */
    public String getError() {
        return d_error;
    }

    /**
     * Set an error message.
     *
     * @param p_error The error message to set.
     */
    public void setError(String p_error) {
        this.d_error = p_error;
    }

    /**
     * Check if a load command has been executed.
     *
     * @return `true` if a load command has been executed, `false` otherwise.
     */
    public boolean getD_loadCommand() {
        return this.d_loadCommand;
    }

    /**
     * Set the load command as executed.
     */
    public void setD_loadCommand() {
        this.d_loadCommand = true;
    }

    /**
     * Get the list of unexecuted orders.
     *
     * @return The list of unexecuted orders.
     */
    public List<Order> getD_unexecutedOrdersList() {
        return d_unexecutedOrdersList;
    }

    /**
     * Set the list of unexecuted orders.
     *
     * @param p_unexecutedOrders The list of unexecuted orders to set.
     */
    public void setD_unexecutedOrdersList(List<Order> p_unexecutedOrders) {
        this.d_unexecutedOrdersList = p_unexecutedOrders;
    }

    /**
     * Get the list of players in the game.
     *
     * @return The list of players.
     */
    public List<ModelPlayer> getD_playersList() {
        return d_playersList;
    }

    /**
     * Set the list of players in the game.
     *
     * @param p_players The list of players to set.
     */
    public void setD_playersList(List<ModelPlayer> p_players) {
        this.d_playersList = p_players;
    }

    /**
     * Get the most recent log message from the log buffer.
     *
     * @return The most recent log message.
     */
    public String getRecentLog() {
        return d_logBuffer.getD_logMessage();
    }

    /**
     * Update the log buffer with a new log message and its type.
     *
     * @param p_logMessage The log message to add.
     * @param p_logType    The type of the log message.
     */
    public void updateLog(String p_logMessage, String p_logType) {
        d_logBuffer.currentLog(p_logMessage, p_logType);
    }
}
