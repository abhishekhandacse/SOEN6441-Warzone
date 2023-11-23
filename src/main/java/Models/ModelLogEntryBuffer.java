package Models;

import Views.LogWriter;

import java.io.Serializable;
import java.util.Observable;

public class ModelLogEntryBuffer extends Observable implements Serializable {

                    String d_logMessage;

        public ModelLogEntryBuffer() {
        LogWriter l_logWriter = new LogWriter();
        this.addObserver(l_logWriter);
    }

        public String getD_logMessage() {
        return d_logMessage;
    }

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
