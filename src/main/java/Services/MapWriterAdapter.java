package Services;
import java.io.FileWriter;
import java.io.IOException;

import Models.GameState;

/**
 * Adapter class for writing to conquest map file.
 *
 */
public class MapWriterAdapter extends MapFileWriter{

    /**
     * ConquestMapFileWriter object
     */
    private ConquestMapFileWriter l_conqMapFileWriter;

    /**
     * Adapter constructor for setting conquest map file Writer.
     *
     * @param p_conqMapFileWriter conquest map file Writer
     */
    public MapWriterAdapter(ConquestMapFileWriter p_conqMapFileWriter) {
        this.l_conqMapFileWriter = p_conqMapFileWriter;
    }


    /**
     * Adapter for writing to different type of map file through adaptee.
     *
     * @param p_gameState current state of the game
     * @param p_writer file writer
     * @param p_mapFormat format in which map file has to be saved
     * @throws IOException Io exception
     */
    public void parseMapToFile(GameState p_gameState, FileWriter p_writer, String p_mapFormat) throws IOException {
        l_conqMapFileWriter.parseMapToFile(p_gameState, p_writer, p_mapFormat);
    }
}
