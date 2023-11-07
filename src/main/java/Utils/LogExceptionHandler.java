package Utils;

import Models.GameState;

/**
 * Class to Add Exception to Logs that are not caught using try/catch.
 */
public class LogExceptionHandler implements Thread.UncaughtExceptionHandler{

    /**
     * GameState to which Exception Log Belongs to.
     */
    GameState d_initialGameState;

    /**
     * Constructor to set the GameState Object.
     *
     * @param p_gameState Current GameState
     */
    public LogExceptionHandler(GameState p_gameState){
        d_initialGameState = p_gameState;
    }

    /**
     * Updates the Log in the GameState.
     *
     * @param p_t Thread of Exception.
     * @param p_e Throwable Instance of Exception
     */
    @Override
    public void uncaughtException(Thread p_t, Throwable p_e) {
            d_initialGameState.updateLog(p_e.getMessage(), "effect");
    }
}
