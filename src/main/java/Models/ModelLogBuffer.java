package Models;

import Views.LogWriter;

import java.util.Observable;

/**
 * The `LogEntryBuffer` class represents a log buffer that holds log messages and notifies observers (such as a log writer)
 * when a new log message is added.
 */
public class ModelLogBuffer extends Observable {

    /**
     * The log message to be stored in the buffer.
     */
    String d_logMessage;

    /**
     * Constructs a `LogEntryBuffer` and adds a `LogWriter` observer to handle log messages.
     */
    public ModelLogBuffer() {
        LogWriter l_logWriter = new LogWriter();
        this.addObserver(l_logWriter);
    }

    /**
     * Retrieves the current log message.
     *
     * @return The current log message.
     */
    public String getD_logMessage() {
        return d_logMessage;
    }

    /**
     * Updates the log message with the provided message and log type and notifies observers.
     *
     * @param p_messageToUpdate The message to be added to the log.
     * @param p_logType The type of the log message (e.g., "command," "order," "phase," "effect," "start," or "end").
     */
    public void currentLog(String p_messageToUpdate, String p_logType) {
        p_logType = p_logType.toLowerCase(); // Ensure case insensitivity

        if ("command".equals(p_logType)) {
            d_logMessage = System.lineSeparator() + "Command Entered: " + p_messageToUpdate + System.lineSeparator();
        } else if ("order".equals(p_logType)) {
            d_logMessage = System.lineSeparator() + " Order Issued: " + p_messageToUpdate + System.lineSeparator();
        } else if ("phase".equals(p_logType)) {
            d_logMessage = System.lineSeparator() + "=======" + p_messageToUpdate + "=======" + System.lineSeparator() + System.lineSeparator();
        } else if ("effect".equals(p_logType)) {
            d_logMessage = "Log: " + p_messageToUpdate + System.lineSeparator();
        } else if ("start".equals(p_logType) || "end".equals(p_logType)) {
            d_logMessage = p_messageToUpdate + System.lineSeparator();
        }

        setChanged();
        notifyObservers();
    }
}
