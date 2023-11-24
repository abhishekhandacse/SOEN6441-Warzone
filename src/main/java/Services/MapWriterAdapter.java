package Services;
import java.io.FileWriter;
import java.io.IOException;

import Models.GameState;


public class MapWriterAdapter extends MapFileWriter{

    private ConquestMapFileWriter l_conqMapFileWriter;

    public MapWriterAdapter(ConquestMapFileWriter p_conqMapFileWriter) {
        this.l_conqMapFileWriter = p_conqMapFileWriter;
    }



    public void parseMapToFile(GameState p_gameState, FileWriter p_writer, String p_mapFormat) throws IOException {
        l_conqMapFileWriter.parseMapToFile(p_gameState, p_writer, p_mapFormat);
    }
}
