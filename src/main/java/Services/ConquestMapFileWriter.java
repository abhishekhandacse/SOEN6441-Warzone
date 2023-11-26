package Services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import Constants.ApplicationConstants;
import Models.Continent;
import Models.Country;
import Models.GameState;

/**
 * Writer to read and create conquest map file.
 *
 */
public class ConquestMapFileWriter implements Serializable {



    /**
     * Retrieves continents' data from game state and writes it to file.
     *
     * @param p_currentGameState Current GameState
     * @param p_currentFileWriter    Writer Object for file
     * @throws IOException handles I/O
     */
    private void writeContinentMetadata(GameState p_currentGameState, FileWriter p_currentFileWriter) throws IOException {
        p_currentFileWriter.write(System.lineSeparator() + ApplicationConstants.CONQUEST_CONTINENTS + System.lineSeparator());
        for (Continent l_continent : p_currentGameState.getD_map().getD_continents()) {
            p_currentFileWriter.write(
                    l_continent.getD_continentName().concat("=").concat(l_continent.getD_continentValue().toString())
                            + System.lineSeparator());
        }
    }

    /**
     * Retrieves country and boarder data from game state and writes it to file
     * writer.
     *
     * @param p_initialGameState Current GameState Object
     * @param p_currentFileWriter    Writer object for file
     * @throws IOException handles I/0
     */
    private void writeCountryAndBoarderMetaData(GameState p_initialGameState, FileWriter p_currentFileWriter) throws IOException {
        String l_countryMetaData = new String();

        // Writes Country Objects to File And Organizes Border Data for each of them
        p_currentFileWriter.write(System.lineSeparator() + ApplicationConstants.CONQUEST_TERRITORIES + System.lineSeparator());
        for (Country l_country : p_initialGameState.getD_map().getD_countriesList()) {
            l_countryMetaData = new String();
            l_countryMetaData = l_country.getD_countryName().concat(",dummy1,dummy2,")
                    .concat(p_initialGameState.getD_map().getContinentByID(l_country.getD_continentId()).getD_continentName());

            if (null != l_country.getD_adjacentCountryIds() && !l_country.getD_adjacentCountryIds().isEmpty()) {
                for (Integer l_adjCountry : l_country.getD_adjacentCountryIds()) {
                    l_countryMetaData = l_countryMetaData.concat(",")
                            .concat(p_initialGameState.getD_map().getCountryByID(l_adjCountry).getD_countryName());
                }
            }
            p_currentFileWriter.write(l_countryMetaData + System.lineSeparator());
        }
    }

    /**
     * Reads conquest map, parses it and stores it in conquest type of map file.
     *
     * @param p_currentGameState current state of the game
     * @param p_currentFileWriter    file writer
     * @param p_mapFormatSelected format in which map file has to be saved
     * @throws IOException IOException
     */
    public void parseMapToFile(GameState p_currentGameState, FileWriter p_currentFileWriter, String p_mapFormatSelected) throws IOException {
        if (null != p_currentGameState.getD_map().getD_continents() && !p_currentGameState.getD_map().getD_continents().isEmpty()) {
            writeContinentMetadata(p_currentGameState, p_currentFileWriter);
        }
        if (null != p_currentGameState.getD_map().getD_countriesList() && !p_currentGameState.getD_map().getD_countriesList().isEmpty()) {
            writeCountryAndBoarderMetaData(p_currentGameState, p_currentFileWriter);
        }
    }

}
