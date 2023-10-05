package Exceptions;

/**
 * The type Command validation exception.
 *
 * @author Anurag Teckchandani
 */
public class CommandValidationException extends Exception {

    /**
     * Instantiates a new Command validation exception.
     *
     * @param p_message: Exception Message
     */
    public CommandValidationException(String p_message) {
        super(p_message);
    }
}
