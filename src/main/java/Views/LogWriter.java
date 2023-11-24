package Views;

import Models.ModelLogEntryBuffer;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Observable;
import java.util.Observer;

/**
 * The class updates the Log File composed of logs derived from LogEntryBuffer Class.
 */
public class LogWriter implements Observer {

    /**
     * Updated LogEntry Buffer Observable Object.
     */
    ModelLogEntryBuffer d_bufferLogEntry;

    /**
     * Writes the updated LogEntryBuffer Object into Log file.
     *
     * @param p_observable LogEntryBuffer Object.
     * @param p_object Object
     */
    @Override
    public void update(Observable p_observable, Object p_object) {
        d_bufferLogEntry = (ModelLogEntryBuffer) p_observable;
        File l_fileLog = new File("AllLogs.txt");
        String l_logMessage = d_bufferLogEntry.getD_logMessage();

        try{
            if(l_logMessage.equals("Initializing the Game ......"+System.lineSeparator()+System.lineSeparator())) {
                Files.newBufferedWriter(Paths.get("AllLogs.txt"), StandardOpenOption.TRUNCATE_EXISTING).write(" ");
            }
            Files.write(Paths.get("AllLogs.txt"), l_logMessage.getBytes(StandardCharsets.US_ASCII), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }catch(Exception l_e){
            l_e.printStackTrace();
        }
    }
}
