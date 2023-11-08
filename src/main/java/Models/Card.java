package Models;

/**
 * The Card interface represents an abstraction for cards used within a game. 
 * It extends the Order interface.
 */
public interface Card extends Order {

    /**
     * Checks the validity of the card order in the context of the provided game state.
     * Implementing classes must define the validation logic for the card's order.
     *
     * @param p_gameState the current state of the game
     * @return a Boolean value indicating whether the card order is valid (true) or not (false)
     */
    Boolean validOrderCheck(GameState p_gameState);
}