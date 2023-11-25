package Utils;

import Models.GameState;

import java.io.Serializable;


/**
 * Class to Add Exception to Logs that are not caught using try/catch.
 */
public class LogHandlerException implements Thread.UncaughtExceptionHandler, Serializable {

    /**
     * GameState to which Exception Log Belongs to.
     */
    GameState d_gameState;

    /**
     * Updates the Log in the GameState.
     *
     * @param p_t Thread of Exception.
     * @param p_e Throwable Instance of Exception
     */
    @Override
    public void uncaughtException(Thread p_t, Throwable p_e) {
        d_gameState.updateLog(p_e.getMessage(), "effect");
    }

    /**
     * Constructor to set the GameState Object.
     *
     * @param p_currentGameState Current GameState
     */
    public LogHandlerException(GameState p_currentGameState){
        d_gameState = p_currentGameState;
    }


}
