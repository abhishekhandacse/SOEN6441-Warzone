package Views;

import Models.ModelLogBuffer;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Observable;
import java.util.Observer;


public class LogWriter implements Observer {


    ModelLogBuffer d_bufferLogEntry;


    @Override
    public void update(Observable p_observable, Object p_object) {
        d_bufferLogEntry = (ModelLogBuffer) p_observable;
        File l_logfile = new File("FileLogs.txt");
        String l_logMessage = d_bufferLogEntry.getD_logMessage();

        try{
            if(l_logMessage.equals("Game is being initialized ......"+System.lineSeparator()+System.lineSeparator())) {
                Files.newBufferedWriter(Paths.get("FileLogs.txt"), StandardOpenOption.TRUNCATE_EXISTING).write(" ");
            }
            Files.write(Paths.get("FileLogs.txt"), l_logMessage.getBytes(StandardCharsets.US_ASCII), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }catch(Exception l_e){
            l_e.printStackTrace();
        }
    }
}
