package Models;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to test functionality of GameState class functions.
 */
public class GameState implements Serializable {

    /**
     * map object.
     */
    Map d_map;

    /**
     * Log Entries for existing game state.
     */
    ModelLogEntryBuffer d_logBuffer = new ModelLogEntryBuffer();

    /**
     * list of players.
     */
    List<ModelPlayer> d_players;

    /**
     * list of unexecuted orders.
     */
    List<Order> d_unexecutedOrderList;

    /**
     * error message.
     */
    String d_error;

    /**
     * Checks if user has used load command.
     */
    Boolean d_loadCommand = false;

    /**
     * Number of turns in tournament.
     */
    int d_maxNumberOfTurns = 0;

    /**
     * Number of remaining turns in tournament.
     */
    int d_numberOfTurnsLeft = 0;

    /**
     * Maintains list of players lost in the game.
     */
    List<ModelPlayer> d_playersFailed = new ArrayList<>();

    /**
     * Winner Player.
     */
    ModelPlayer d_winner;

    /**
     * getter method to get the map.
     *
     * @return map object
     */
    public Map getD_map() {
        return d_map;
    }

    /**
     * setter method to set the map.
     *
     * @param p_map map object
     */
    public void setD_map(Map p_map) {
        this.d_map = p_map;
    }

    /**
     * getter method to get the list of players.
     *
     * @return list of players
     */
    public List<ModelPlayer> getD_players() {
        return d_players;
    }

    /**
     * setter method to set the players.
     *
     * @param p_players list of players
     */
    public void setD_players(List<ModelPlayer> p_players) {
        this.d_players = p_players;
    }

    /**
     * getter method to get the list of orders which are yet to be executed.
     *
     * @return list of orders
     */
    public List<Order> getD_unexecutedOrders() {
        return d_unexecutedOrderList;
    }

    /**
     * setter method to set the unexecuted orders.
     *
     * @param p_unexecutedOrders list of unexecuted orders
     */
    public void setD_unexecutedOrders(List<Order> p_unexecutedOrders) {
        this.d_unexecutedOrderList = p_unexecutedOrders;
    }

    /**
     * getter method to get the error message.
     *
     * @return error message
     */
    public String getError() {
        return d_error;
    }

    /**
     * setter method to set the error message.
     *
     * @param p_error error message
     */
    public void setError(String p_error) {
        this.d_error = p_error;
    }

    /**
     * Message to be added in the log.
     *
     * @param p_logMessage Log Message to be set in the Object
     * @param p_logType    Type of Log Message to be Added
     */
    public void updateLog(String p_logMessage, String p_logType) {
        d_logBuffer.currentLog(p_logMessage, p_logType);
    }


    public String getRecentLog() {
        return d_logBuffer.getD_logMessage();
    }

    public void setD_loadCommand() {
        this.d_loadCommand = true;
    }


    public boolean getD_loadCommand() {
        return this.d_loadCommand;
    }


    public int getD_maxNumberOfTurns() {
        return d_maxNumberOfTurns;
    }


    public void setD_maxNumberOfTurns(int p_maxNumberOfTurns) {
        this.d_maxNumberOfTurns = p_maxNumberOfTurns;
    }


    public int getD_numberOfTurnsLeft() {
        return d_numberOfTurnsLeft;
    }


    public void setD_numberOfTurnsLeft(int p_numberOfTurnsLeft) {
        this.d_numberOfTurnsLeft = p_numberOfTurnsLeft;
    }


    public void removePlayer(ModelPlayer p_player){
        d_playersFailed.add(p_player);
    }


    public List<ModelPlayer> getD_playersFailed() {
        return d_playersFailed;
    }


    public void setD_winner(ModelPlayer p_player){
        d_winner = p_player;
    }


    public ModelPlayer getD_winner(){
        return d_winner;
    }
}
