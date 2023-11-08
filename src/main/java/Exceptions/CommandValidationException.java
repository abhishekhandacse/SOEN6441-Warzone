package Exceptions;

/**
 * Command Exceptions are thrown by this class.
 */
public class CommandValidationException extends Exception {

	/**
	 * InvalidCommand constructor is used to print message when exception is caught in
	 * case command is invalid.
	 *
	 * @param p_message message to print when command is invalid.
	 */
	public CommandValidationException(String p_message) {
		super(p_message);
	}
}
