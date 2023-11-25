package Models;


import java.io.IOException;
import java.io.Serializable;

/**
 * This is the abstract strategy class of Player Behavior.
 */
public abstract class PlayerBehaviorStrategy implements Serializable {


    /**
     * object of GameState class.
     */
    GameState d_currentGameState;

    /**
     * object of player class.
     */
    ModelPlayer d_modelPlayer;





    /**
     * This method returns the player behavior.
     * @return String player behavior
     */
    public abstract String getPlayerBehavior();

    /**
     * This method creates a new order for Random, Aggressive, Cheater and Benevolent Players.
     *
     * @param p_modelPlayer object of Player class
     * @param p_currentGameState object of GameState class
     *
     * @return Order object of order class
     * @throws IOException Exception
     */
    public abstract String createOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) throws IOException;



    /**
     * Card Orders to be defined via Strategy.
     *
     * @param p_modelPlayer player to give Card Orders.
     * @param p_currentGameState GameState representing Current Game
     * @param p_currentCardName Card Name to create Order for
     * @return String representing order
     */
    public abstract String createCardOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState, String p_currentCardName);

    /**
     * Advance Orders to be defined via Strategy.
     *
     * @param p_modelPlayer player to give advance orders
     * @param p_currentGameState GameState representing current Game
     * @return String representing Order.
     */
    public abstract String createAdvanceOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState);

    /**
     * Deploy Orders to be defined via Strategy.
     *
     * @param p_modelPlayer player to give deploy orders
     * @param p_currentGameState current Gamestate
     * @return String representing Order
     */
    public abstract String createDeployOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState);



}

