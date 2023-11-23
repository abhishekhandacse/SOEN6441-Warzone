package Services;

import Models.GameState;
import Models.Map;

import java.util.List;

public class MapReaderAdapter extends MapFileReader {

        private ConquestMapFileReader l_conquestMapFileReader;

        public MapReaderAdapter(ConquestMapFileReader p_conquestMapFileReader) {
        this.l_conquestMapFileReader = p_conquestMapFileReader;
    }

        public void parseMapFile(GameState p_gameState, Map p_map, List<String> p_linesOfFile) {
        l_conquestMapFileReader.readConquestFile(p_gameState, p_map, p_linesOfFile);
    }
}
