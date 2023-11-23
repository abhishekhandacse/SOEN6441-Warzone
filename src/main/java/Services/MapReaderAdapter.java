/**
 * The {@code MapReaderAdapter} class adapts the Conquest map file reader to the standard map file reader.
 *
 * @version 1.0
 */
package Services;

import Models.GameState;
import Models.Map;

import java.util.List;

/**
 * Adapts the Conquest map file reader to the standard map file reader.
 */
public class MapReaderAdapter extends MapFileReader {

    /** The Conquest map file reader to be adapted. */
    private ConquestMapFileReader l_conquestMapFileReader;

    /**
     * Constructs a new map reader adapter with the given Conquest map file reader.
     *
     * @param p_conquestMapFileReader The Conquest map file reader to be adapted.
     */
    public MapReaderAdapter(ConquestMapFileReader p_conquestMapFileReader) {
        this.l_conquestMapFileReader = p_conquestMapFileReader;
    }

    /**
     * Parses the map file using the adapted Conquest map file reader.
     *
     * @param p_gameState   The current game state.
     * @param p_map         The game map.
     * @param p_linesOfFile The lines of the map file.
     */
    public void parseMapFile(GameState p_gameState, Map p_map, List<String> p_linesOfFile) {
        l_conquestMapFileReader.readConquestFile(p_gameState, p_map, p_linesOfFile);
    }
}
