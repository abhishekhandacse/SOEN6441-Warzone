package Models;

import java.util.List;

/**
 * The type State.
 */
public class State {


    /**
     * The D map.
     */
    Map d_map;
    /**
     * The D players.
     */
    List<Player> d_players;
    /**
     * The D unexecuted orders.
     */
    List<Deploy> d_unexecutedOrders;
    /**
     * The D error.
     */
    String d_error;

    /**
     * Gets d players.
     *
     * @return the d players
     */
    public List<Player> getD_players() {
        return d_players;
    }

    /**
     * Sets d players.
     *
     * @param p_players the p players
     */
    public void setD_players(List<Player> p_players) {
        this.d_players = p_players;
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
     * Gets d unexecuted orders.
     *
     * @return the d unexecuted orders
     */
    public List<Deploy> getD_unexecutedOrders() {
        return d_unexecutedOrders;
    }

    /**
     * Sets d unexecuted orders.
     *
     * @param p_unexecutedOrders the p unexecuted orders
     */
    public void setD_unexecutedOrders(List<Deploy> p_unexecutedOrders) {
        this.d_unexecutedOrders = p_unexecutedOrders;
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

}
