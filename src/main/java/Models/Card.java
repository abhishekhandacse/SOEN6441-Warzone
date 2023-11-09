package Models;

/**
 * The card interface extends the Order
 * It represents an abstraction for cards used within a game. 
 * 
 */
public interface Card extends Order {

    /**
     * 
     * Implementing classes must define the validation logic for the card's order and checks the order validity
     *
     * @param p_gameState Game's current state
     * @return a Boolean value indicating whether the card order is valid (true) or not (false)
     */
    Boolean validOrderCheck(GameState p_gameState);
}