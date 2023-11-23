package Services;

import Constants.ApplicationConstantsHardcoding;
import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Models.Continent;
import Models.GameState;
import Models.Map;
import Models.Country;
import Utils.CommonUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The `MapService` class provides various services related to game maps, including loading, editing, and saving map files.
 */
public class MapService {

    /**
     * Loads a map from a file and populates the game state with map data.
     *
     * @param p_gameState     The game state to populate with the loaded map.
     * @param p_loadFileName  The name of the map file to load.
     * @return The loaded map.
     * @throws MapValidationException If the map file is invalid or not found.
     */
    public Map loadMap(GameState p_gameState, String p_loadFileName) throws MapValidationException {
        Map l_loadedMap = new Map();
        List<String> l_fileLines = fileLoad(p_loadFileName);

        if (null != l_fileLines && !l_fileLines.isEmpty()) {


            List<String> l_contListData = getMetaData(l_fileLines, "continent");
            List<Continent> l_contListObjects = parseMetadataForContinents(l_contListData);
            List<String> l_countryListData = getMetaData(l_fileLines, "country");
            List<String> l_metaDataBorders = getMetaData(l_fileLines, "border");
            List<Country> l_countryListObjects = parseMetadataForCountries(l_countryListData);


            l_countryListObjects = parseBorderMetaData(l_countryListObjects, l_metaDataBorders);
            l_contListObjects = linkCountryContinents(l_countryListObjects, l_contListObjects);
            l_loadedMap.setD_allContinents(l_contListObjects);
            l_loadedMap.setD_allCountries(l_countryListObjects);
            p_gameState.setD_map(l_loadedMap);
        }
        return l_loadedMap;
    }

    /**
     * Loads the lines from a map file.
     *
     * @param p_fileNameLoaded The name of the map file to load.
     * @return A list of lines from the map file.
     * @throws MapValidationException If the map file is not found.
     */
    public List<String> fileLoad(String p_fileNameLoaded) throws MapValidationException {

        String l_pathForFile = CommonUtil.getAbsolutePathForFile(p_fileNameLoaded);
        List<String> l_listLine;

        BufferedReader l_bufferedReader;
        try {

            l_bufferedReader = new BufferedReader(new FileReader(l_pathForFile));
            l_listLine = l_bufferedReader.lines().collect(Collectors.toList());
            l_bufferedReader.close();
        } catch (IOException l_e1) {
            throw new MapValidationException("Map File not Found!");
        }
        return l_listLine;
    }

    /**
     * Retrieves specific metadata lines from a list of file lines based on the given switch parameter.
     *
     * @param p_fileLines       The list of file lines to extract metadata from.
     * @param p_switchParameter The switch parameter to determine which metadata to retrieve ('continent', 'country', or 'border').
     * @return The extracted metadata lines based on the switch parameter.
     */
    public List<String> getMetaData(List<String> p_fileLines, String p_switchParameter) {
        switch (p_switchParameter) {
            case "continent":
                return p_fileLines.subList(
                        p_fileLines.indexOf(ApplicationConstantsHardcoding.ALL_CONTINENTS) + 1,
                        p_fileLines.indexOf(ApplicationConstantsHardcoding.ALL_COUNTRIES) - 1);
            case "country":
                return p_fileLines.subList(p_fileLines.indexOf(ApplicationConstantsHardcoding.ALL_COUNTRIES) + 1,
                        p_fileLines.indexOf(ApplicationConstantsHardcoding.ALL_BORDERS) - 1);
            case "border":
                return p_fileLines.subList(p_fileLines.indexOf(ApplicationConstantsHardcoding.ALL_BORDERS) + 1,
                        p_fileLines.size());
            default:
                return null;
        }
    }

    /**
     * Parses the metadata lines for countries and creates a list of ModelCountry objects.
     *
     * @param p_countriesList The list of metadata lines for countries.
     * @return A list of ModelCountry objects created from the parsed metadata.
     */
    public List<Country> parseMetadataForCountries(List<String> p_countriesList) {

        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<>();
        List<Country> l_countriesList = new ArrayList<Country>();

        for (String country : p_countriesList) {
            String[] l_metaDataCountries = country.split(" ");
            l_countriesList.add(new Country(Integer.parseInt(l_metaDataCountries[0]), l_metaDataCountries[1],
                    Integer.parseInt(l_metaDataCountries[2])));
        }
        return l_countriesList;
    }

    /**
     * Parses the metadata lines for continents and creates a list of Continent objects.
     *
     * @param p_continentList The list of metadata lines for continents.
     * @return A list of Continent objects created from the parsed metadata.
     */
    public List<Continent> parseMetadataForContinents(List<String> p_continentList) {
        int l_continentId = 1;
        List<Continent> l_continents = new ArrayList<Continent>();


        for (String cont : p_continentList) {
            String[] l_metaData = cont.split(" ");
            l_continents.add(new Continent(l_continentId, l_metaData[0], Integer.parseInt(l_metaData[1])));
            l_continentId++;
        }
        return l_continents;
    }

    /**
     * Edits a map file in the game state or creates a new one if it doesn't exist.
     *
     * @param p_gameState   The game state containing the map data.
     * @param p_editFilePath The path to the map file to be edited or created.
     * @throws IOException  If there is an issue with file I/O.
     * @throws MapValidationException  If the map is invalid.
     */
    public void editMap(GameState p_gameState, String p_editFilePath) throws IOException, MapValidationException {

        String l_filePath = CommonUtil.getAbsolutePathForFile(p_editFilePath);
        File l_fileToBeEdited = new File(l_filePath);

        if (l_fileToBeEdited.createNewFile()) {
            System.out.println("File has been created.");
            Map l_map = new Map();
            l_map.setD_inputMapFile(p_editFilePath);
            p_gameState.setD_map(l_map);
            p_gameState.updateLog(p_editFilePath + " File has been created for user to edit", "effect");
        } else {
            System.out.println("File already exists.");
            this.loadMap(p_gameState, p_editFilePath);
            if (null == p_gameState.getD_map()) {
                p_gameState.setD_map(new Map());
            }
            p_gameState.getD_map().setD_inputMapFile(p_editFilePath);
            p_gameState.updateLog(p_editFilePath + " already exists and is loaded for editing", "effect");
        }
    }

    /**
     * Links countries to their corresponding continents based on continent IDs.
     *
     * @param p_countries   The list of ModelCountry objects to link with continents.
     * @param p_continents  The list of Continent objects containing continent information.
     * @return The updated list of Continent objects with linked countries.
     */
    public List<Continent> linkCountryContinents(List<Country> p_countries, List<Continent> p_continents) {
        for (Country c : p_countries) {
            for (Continent cont : p_continents) {
                if (cont.getD_continentID().equals(c.getD_continentId())) {
                    cont.addingCountry(c);
                }
            }
        }
        return p_continents;
    }

    /**
     * Adds or removes continents from the map.
     *
     * @param p_gameState   The game state containing the map data.
     * @param p_mapToBeUpdated The map to be updated.
     * @param p_operation    The operation to perform ('add' or 'remove').
     * @param p_argument     The argument specifying the continent to add or remove.
     * @return The updated map with the continent added or removed.
     * @throws MapValidationException    If there is an issue with adding or removing the continent.
     */
    public Map addRemoveContinents(GameState p_gameState, Map p_mapToBeUpdated, String p_operation, String p_argument) throws MapValidationException {

        try {
            if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length == 2) {
                p_mapToBeUpdated.addContinent(p_argument.split(" ")[0], Integer.parseInt(p_argument.split(" ")[1]));
                this.setD_logMapService("Continent " + p_argument.split(" ")[0] + " added successfully!", p_gameState);
            } else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length == 1) {
                p_mapToBeUpdated.removeContinent(p_argument.split(" ")[0]);
                this.setD_logMapService("Continent " + p_argument.split(" ")[0] + " removed successfully!", p_gameState);
            } else {
                throw new MapValidationException("Continent " + p_argument.split(" ")[0] + " couldn't be added/removed. Changes are not made due to Invalid Command Passed.");
            }
        } catch (MapValidationException | NumberFormatException l_e) {
            this.setD_logMapService(l_e.getMessage(), p_gameState);
        }
        return p_mapToBeUpdated;
    }

    /**
     * Parses border metadata and updates country neighbors in the list of ModelCountry objects.
     *
     * @param p_countriesList The list of ModelCountry objects.
     * @param p_bordersList   The list of metadata lines for country borders.
     * @return The updated list of ModelCountry objects with neighbor information.
     */
    public List<Country> parseBorderMetaData(List<Country> p_countriesList, List<String> p_bordersList) {
        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();

        for (String l_border : p_bordersList) {
            if (null != l_border && !l_border.isEmpty()) {
                ArrayList<Integer> l_neighbours = new ArrayList<Integer>();
                String[] l_splitString = l_border.split(" ");
                for (int i = 1; i <= l_splitString.length - 1; i++) {
                    l_neighbours.add(Integer.parseInt(l_splitString[i]));

                }
                l_countryNeighbors.put(Integer.parseInt(l_splitString[0]), l_neighbours);
            }
        }
        for (Country c : p_countriesList) {
            List<Integer> l_adjacentCountries = l_countryNeighbors.get(c.getD_countryId());
            c.setD_adjacentCountryIds(l_adjacentCountries);
        }
        return p_countriesList;
    }

    /**
     * Edits map functions based on a switch parameter.
     *
     * @param p_gameState     The game state containing the map data.
     * @param p_argument      The argument specifying the action to perform.
     * @param p_operation     The operation to perform ('add' or 'remove').
     * @param p_switchParameter The switch parameter to determine which function to execute (1 for continents, 2 for countries, 3 for neighbors).
     * @throws IOException    If there is an issue with file I/O.
     * @throws MapValidationException    If the map is invalid.
     * @throws CommandValidationException If an invalid command is passed.
     */
    public void editFunctions(GameState p_gameState, String p_argument, String p_operation, Integer p_switchParameter) throws IOException, MapValidationException, CommandValidationException {
        Map l_updatedMap;
        String l_mapFileName = p_gameState.getD_map().getD_inputMapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNullObject(p_gameState.getD_map().getD_allContinents()) && CommonUtil.isNullObject(p_gameState.getD_map().getD_allCountries())) ? this.loadMap(p_gameState, l_mapFileName) : p_gameState.getD_map();

        // Edit Control Logic for Continent, Country & Neighbor
        if (!CommonUtil.isNullObject(l_mapToBeUpdated)) {
            switch (p_switchParameter) {
                case 1:
                    l_updatedMap = addRemoveContinents(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
                    break;
                case 2:
                    l_updatedMap = addRemoveCountry(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
                    break;
                case 3:
                    l_updatedMap = addRemoveNeighbour(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + p_switchParameter);
            }
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_inputMapFile(l_mapFileName);
        }
    }

    /**
     * Adds or removes a country from the map.
     *
     * @param p_gameState   The game state containing the map data.
     * @param p_mapToBeUpdated The map to be updated.
     * @param p_argument    The argument specifying the country to add or remove.
     * @param p_operation   The operation to perform ('add' or 'remove').
     * @return The updated map with the country added or removed.
     * @throws MapValidationException  If there is an issue with adding or removing the country.
     */
    public Map addRemoveCountry(GameState p_gameState, Map p_mapToBeUpdated, String p_argument, String p_operation) throws MapValidationException {

        try {
            if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length == 2) {
                p_mapToBeUpdated.addCountry(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_logMapService("Country " + p_argument.split(" ")[0] + " added successfully!", p_gameState);
            } else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length == 1) {
                p_mapToBeUpdated.removeCountry(p_argument.split(" ")[0]);
                this.setD_logMapService("Country " + p_argument.split(" ")[0] + " removed successfully!", p_gameState);
            } else {
                throw new MapValidationException("Country " + p_argument.split(" ")[0] + " could not be " + p_operation + "ed!");
            }
        } catch (MapValidationException l_e) {
            this.setD_logMapService(l_e.getMessage(), p_gameState);
        }
        return p_mapToBeUpdated;
    }

    /**
     * Saves the current map data to a map file.
     *
     * @param p_gameState The game state containing the map data to be saved.
     * @param p_fileName  The name of the map file to save.
     * @return `true` if the map is successfully saved, `false` otherwise.
     * @throws MapValidationException If there is an issue with saving the map.
     */
    public boolean mapSave(GameState p_gameState, String p_fileName) throws MapValidationException {
        boolean l_flagValidate = false;
        try {

            // Verifies if the file linked to savemap and edited by user are same
            if (!p_fileName.equalsIgnoreCase(p_gameState.getD_map().getD_inputMapFile())) {
                p_gameState.setError("Kindly provide same file name to save which you have given for edit");
                return false;
            } else {
                if (null != p_gameState.getD_map()) {
                    Map l_currentMap = p_gameState.getD_map();

                    // Proceeds to save the map if it passes the validation check
                    this.setD_logMapService("Validating Map......", p_gameState);
                    //boolean l_mapValidationStatus = l_currentMap.Validate();
                    if (l_currentMap.Validate()) {
                        Files.deleteIfExists(Paths.get(CommonUtil.getAbsolutePathForFile(p_fileName)));
                        FileWriter l_writer = new FileWriter(CommonUtil.getAbsolutePathForFile(p_fileName));

                        if (null != p_gameState.getD_map().getD_allContinents()
                                && !p_gameState.getD_map().getD_allContinents().isEmpty()) {
                            writeMetadataForContinent(p_gameState, l_writer);
                        }
                        if (null != p_gameState.getD_map().getD_allCountries()
                                && !p_gameState.getD_map().getD_allCountries().isEmpty()) {
                            writeCountryAndBoarderMetaData(p_gameState, l_writer);
                        }
                        p_gameState.updateLog("Map Saved Successfully", "effect");
                        l_writer.close();
                    }
                } else {
                    p_gameState.updateLog("Validation failed! Cannot Save the Map file!", "effect");
                    p_gameState.setError("Validation Failed");
                    return false;
                }
            }
            return true;
        } catch (IOException | MapValidationException l_e) {
            this.setD_logMapService(l_e.getMessage(), p_gameState);
            p_gameState.updateLog("Couldn't save the changes in map file!", "effect");
            p_gameState.setError("Error in saving map file");
            return false;
        }
    }

    /**
     * Adds or removes a neighbor for a country in the map.
     *
     * @param p_gameState  The game state containing the map data.
     * @param p_mapToBeUpdated  The map to be updated with the new neighbor.
     * @param p_argument  The argument specifying the operation and the neighbor pair (e.g., "add 1 2").
     * @param p_operation  The operation to perform, either "add" or "remove".
     * @return The updated map after adding or removing the neighbor.
     * @throws MapValidationException If there is an issue with adding or removing the neighbor.
     */
    public Map addRemoveNeighbour(GameState p_gameState, Map p_mapToBeUpdated, String p_argument, String p_operation) throws MapValidationException {

        try {
            if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length == 2) {
                p_mapToBeUpdated.addCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_logMapService("Neighbour Pair " + p_argument.split(" ")[0] + " " + p_argument.split(" ")[1] + " added successfully!", p_gameState);
            } else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length == 2) {
                p_mapToBeUpdated.removeCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_logMapService("Neighbour Pair " + p_argument.split(" ")[0] + " " + p_argument.split(" ")[1] + " removed successfully!", p_gameState);
            } else {
                throw new MapValidationException("Neighbour could not be " + p_operation + "ed!");
            }
        } catch (MapValidationException l_e) {
            this.setD_logMapService(l_e.getMessage(), p_gameState);
        }
        return p_mapToBeUpdated;
    }

    /**
     * Writes the country and border metadata to the map file.
     *
     * @param p_gameState The game state containing map data to be written.
     * @param p_writer The FileWriter for the map file.
     * @throws IOException If there is an issue with writing the metadata to the file.
     */
    private void writeCountryAndBoarderMetaData(GameState p_gameState, FileWriter p_writer) throws IOException {
        String l_countryMetaData;
        String l_bordersMetaData;
        List<String> l_bordersList = new ArrayList<>();

        // Writes Country Objects to File And Organizes Border Data for each of them
        p_writer.write(System.lineSeparator() + ApplicationConstantsHardcoding.ALL_COUNTRIES + System.lineSeparator());
        for (Country l_country : p_gameState.getD_map().getD_allCountries()) {
            l_countryMetaData = l_country.getD_countryId().toString().concat(" ").concat(l_country.getD_countryName())
                    .concat(" ").concat(l_country.getD_continentId().toString());
            p_writer.write(l_countryMetaData + System.lineSeparator());

            if (null != l_country.getD_adjacentCountryIds() && !l_country.getD_adjacentCountryIds().isEmpty()) {
                l_bordersMetaData = "";
                l_bordersMetaData = l_country.getD_countryId().toString();
                for (Integer l_adjCountry : l_country.getD_adjacentCountryIds()) {
                    l_bordersMetaData = l_bordersMetaData.concat(" ").concat(l_adjCountry.toString());
                }
                l_bordersList.add(l_bordersMetaData);
            }
        }

        // Writes Border data to the File
        if (null != l_bordersList && !l_bordersList.isEmpty()) {
            p_writer.write(System.lineSeparator() + ApplicationConstantsHardcoding.ALL_BORDERS + System.lineSeparator());
            for (String l_borderStr : l_bordersList) {
                p_writer.write(l_borderStr + System.lineSeparator());
            }
        }
    }

    /**
     * Resets the map in the game state.
     *
     * @param p_gameState The game state to reset the map.
     * @param p_fileToLoad The name of the map file that couldn't be loaded.
     */
    public void mapReset(GameState p_gameState, String p_fileToLoad) {
        System.out.println("Map cannot be loaded, as it is invalid. Kindly provide valid map");
        p_gameState.updateLog(p_fileToLoad + " map could not be loaded as it is invalid!", "effect");
        p_gameState.setD_map(new Map());
    }

    /**
     * Sets the log message for map service and logs it to the game state.
     *
     * @param p_MapServiceLog The log message to set.
     * @param p_gameState The game state to log the message.
     */
    public void setD_logMapService(String p_MapServiceLog, GameState p_gameState) {
        System.out.println(p_MapServiceLog);
        p_gameState.updateLog(p_MapServiceLog, "effect");
    }

    /**
     * Writes the metadata for continents to the map file.
     *
     * @param p_gameState The game state containing continent data to be written.
     * @param p_writer The FileWriter for the map file.
     * @throws IOException If there is an issue with writing the metadata to the file.
     */
    private void writeMetadataForContinent(GameState p_gameState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + ApplicationConstantsHardcoding.ALL_CONTINENTS + System.lineSeparator());
        for (Continent l_continent : p_gameState.getD_map().getD_allContinents()) {
            p_writer.write(
                    l_continent.getD_continentName().concat(" ").concat(l_continent.getD_continentValue().toString())
                            + System.lineSeparator());
        }
    }
}
