package Exceptions;

/**
 * The MapValidationException class represents an exception that is thrown when there are issues with map validation.
 * It extends the base Exception class.
 */
public class MapValidationException extends Exception {

    /**
     * Constructs a new MapValidationException with the specified detail message.
     *
     * @param p_message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public MapValidationException(String p_message) {
        super(p_message);
    }
}
