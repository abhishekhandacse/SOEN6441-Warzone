/**
 * The {@code ModelLogEntryBuffer} class represents a buffer for logging entries in a game.
 *
 * @version 1.0
 */
package Models;

import Views.LogWriter;

import java.io.Serializable;
import java.util.Observable;

/**
 * Represents a buffer for logging entries in a game.
 */
public class ModelLogEntryBuffer extends Observable implements Serializable {

    /** The log message to be stored in the buffer. */
                String d_logMessage;

    /**
     * Constructs a new log entry buffer and adds a {@code LogWriter} observer to handle log updates.
     */
    public ModelLogEntryBuffer() {
        LogWriter l_logWriter = new LogWriter();
        this.addObserver(l_logWriter);
    }

    /**
     * Gets the current log message.
     *
     * @return The current log message.
     */
    public String getD_logMessage() {
        return d_logMessage;
    }

    /**
     * Updates the log message based on the provided message and log type.
     *
     * @param p_messageToUpdate The message to be added to the log.
     * @param p_logType         The type of log entry (command, order, phase, effect, start, end).
     */
    public void currentLog(String p_messageToUpdate, String p_logType) {

                  switch (p_logType.toLowerCase()) {
            case "command":
                d_logMessage = System.lineSeparator() + "Command Entered: " + p_messageToUpdate + System.lineSeparator();
                break;
            case "order":
                d_logMessage = System.lineSeparator() + " Order Issued: " + p_messageToUpdate + System.lineSeparator();
                break;
            case "phase":
                d_logMessage = System.lineSeparator() + "=======" + p_messageToUpdate + "=======" + System.lineSeparator() + System.lineSeparator();
                break;
            case "effect":
                d_logMessage = "Log: " + p_messageToUpdate + System.lineSeparator();
                break;
            case "start":
            case "end":
                d_logMessage = p_messageToUpdate + System.lineSeparator();
                break;
        }
        setChanged();
        notifyObservers();
    }
}
