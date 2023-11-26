package Services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import Constants.ApplicationConstants;
import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;

/**
 * The MapService class load, read, parse, edit, and save map file.
 */
public class MapService implements Serializable {




    /**
     * The loadFile method load and read map file.
     *
     * @param p_fileNameLoaded map file name to load.
     * @return List of lines from map file.
     * @throws MapValidationException indicates Map Object Validation failure
     */
    public List<String> loadFile(String p_fileNameLoaded) throws MapValidationException{

        String l_filePath = CommonUtil.getMapFilePath(p_fileNameLoaded);
        List<String> l_lineList = new ArrayList<>();

        BufferedReader l_reader;
        try {
            l_reader = new BufferedReader(new FileReader(l_filePath));
            l_lineList = l_reader.lines().collect(Collectors.toList());
            l_reader.close();
        } catch (IOException l_e1) {
            throw new MapValidationException("Map File not Found!");
        }
        return l_lineList;
    }

    /**
     * The loadmap method process map file.
     *
     * @param p_currentGameState current state of game.
     * @param p_currentLoadFileName map file name.
     * @return Map object after processing map file.
     * @throws MapValidationException indicates Map Object Validation failure
     */
    public Map loadMap(GameState p_currentGameState, String p_currentLoadFileName) throws MapValidationException {
        Map l_map = new Map();
        List<String> l_linesOfFile = loadFile(p_currentLoadFileName);

        if (null != l_linesOfFile && !l_linesOfFile.isEmpty()) {
            if(l_linesOfFile.contains("[Territories]")) {
                MapReaderAdapter l_mapReaderAdapter = new MapReaderAdapter(new ConquestMapFileReader());
                l_mapReaderAdapter.parseMapFile(p_currentGameState, l_map, l_linesOfFile);
            } else if(l_linesOfFile.contains("[countries]")) {
                new MapFileReader().parseMapFile(p_currentGameState, l_map, l_linesOfFile);
            }
        }
        return l_map;
    }


    /**
     * Controls the Flow of Edit Operations: editcontinent, editcountry, editneighbor.
     *
     * @param p_currentGameState Current GameState Object.
     * @param p_currentArgument Arguments for the pertaining command operation.
     * @param p_currentOperation Add/Remove operation to be performed.
     * @param p_switchParameterToDifferentParameter Type of Edit Operation to be performed.
     * @throws IOException Exception.
     * @throws MapValidationException MapValidationException exception.
     * @throws CommandValidationException invalid command exception
     */
    public void editFunctions(GameState p_currentGameState, String p_currentArgument, String p_currentOperation, Integer p_switchParameterToDifferentParameter) throws IOException, MapValidationException, CommandValidationException{
        Map l_updatedMap;
        String l_mapFileName = p_currentGameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_currentGameState.getD_map().getD_continents()) && CommonUtil.isNull(p_currentGameState.getD_map().getD_countries())) ? this.loadMap(p_currentGameState, l_mapFileName) : p_currentGameState.getD_map();

        // Edit Control Logic for Continent, Country & Neighbor
        if(!CommonUtil.isNull(l_mapToBeUpdated)){
            switch(p_switchParameterToDifferentParameter){
                case 1:
                    l_updatedMap = addRemoveContinents(p_currentGameState, l_mapToBeUpdated, p_currentOperation, p_currentArgument);
                    break;
                case 2:
                    l_updatedMap = addRemoveCountry(p_currentGameState, l_mapToBeUpdated, p_currentOperation, p_currentArgument);
                    break;
                case 3:
                    l_updatedMap = addRemoveNeighbour(p_currentGameState, l_mapToBeUpdated, p_currentOperation, p_currentArgument);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + p_switchParameterToDifferentParameter);
            }
            p_currentGameState.setD_map(l_updatedMap);
            p_currentGameState.getD_map().setD_mapFile(l_mapFileName);
        }
    }

    /**
     * Constructs updated Continents list based on passed operations - Add/Remove
     * and Arguments.
     *
     * @param p_currentGameState Current GameState Object
     * @param p_currentMapToBeUpdated Map Object to be Updated
     * @param p_currentOperation Operation to perform on Continents
     * @param p_currentArgument Arguments pertaining to the operations
     * @return List of updated continents
     * @throws MapValidationException MapValidationException exception
     */
    public Map addRemoveContinents(GameState p_currentGameState, Map p_currentMapToBeUpdated, String p_currentOperation, String p_currentArgument) throws MapValidationException {

        try {
            if (p_currentOperation.equalsIgnoreCase("add") && p_currentArgument.split(" ").length==2) {
                p_currentMapToBeUpdated.addContinent(p_currentArgument.split(" ")[0], Integer.parseInt(p_currentArgument.split(" ")[1]));
                this.setD_MapServiceLog("Continent "+ p_currentArgument.split(" ")[0]+ " added successfully!", p_currentGameState);
            } else if (p_currentOperation.equalsIgnoreCase("remove") && p_currentArgument.split(" ").length==1) {
                p_currentMapToBeUpdated.removeContinent(p_currentArgument.split(" ")[0]);
                this.setD_MapServiceLog("Continent "+ p_currentArgument.split(" ")[0]+ " removed successfully!", p_currentGameState);
            } else {
                throw new MapValidationException("Continent "+p_currentArgument.split(" ")[0]+" couldn't be added/removed. Changes are not made due to Invalid Command Passed.");
            }
        } catch (MapValidationException | NumberFormatException l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_currentGameState);
        }
        return p_currentMapToBeUpdated;
    }

    /**
     * Method is responsible for creating a new map if map to be edited does not
     * exists, and if it exists it parses the map file to game state object.
     *
     * @param p_currentGameState GameState model class object
     * @param p_editCurrentFilePath consists of base filepath
     * @throws MapValidationException indicates Map Object Validation failure
     * @throws IOException triggered in case the file does not exist or the file name is invalid
     */
    public void editMap(GameState p_currentGameState, String p_editCurrentFilePath) throws IOException, MapValidationException {

        String l_filePath = CommonUtil.getMapFilePath(p_editCurrentFilePath);
        File l_fileToBeEdited = new File(l_filePath);

        if (l_fileToBeEdited.createNewFile()) {
            System.out.println("File has been created.");
            Map l_map = new Map();
            l_map.setD_mapFile(p_editCurrentFilePath);
            p_currentGameState.setD_map(l_map);
            p_currentGameState.updateLog(p_editCurrentFilePath+ " File has been created for user to edit", "effect");
        } else {
            System.out.println("File already exists.");
            this.loadMap(p_currentGameState, p_editCurrentFilePath);
            if (null == p_currentGameState.getD_map()) {
                p_currentGameState.setD_map(new Map());
            }
            p_currentGameState.getD_map().setD_mapFile(p_editCurrentFilePath);
            p_currentGameState.updateLog(p_editCurrentFilePath+ " already exists and is loaded for editing", "effect");
        }
    }







    /**
     * Parses the updated map to .map file and stores it at required location.
     *
     * @param p_currentGameState Current GameState
     * @param p_currentFileName filename to save things in
     * @return true/false based on successful save operation of map to file
     * @throws MapValidationException MapValidationException exception
     */
    public boolean saveMap(GameState p_currentGameState, String p_currentFileName) throws MapValidationException {
        try {
            String l_mapFormat = null;
            // Verifies if the file linked to savemap and edited by user are same
            if (!p_currentFileName.equalsIgnoreCase(p_currentGameState.getD_map().getD_mapFile())) {
                p_currentGameState.setError("Kindly provide same file name to save which you have given for edit");
                return false;
            } else {
                if (null != p_currentGameState.getD_map()) {
                    Models.Map l_currentMap = p_currentGameState.getD_map();

                    // Proceeds to save the map if it passes the validation check
                    this.setD_MapServiceLog("Validating Map......", p_currentGameState);
                    if (l_currentMap.Validate()) {
                        l_mapFormat = this.getFormatToSave();
                        Files.deleteIfExists(Paths.get(CommonUtil.getMapFilePath(p_currentFileName)));
                        FileWriter l_writer = new FileWriter(CommonUtil.getMapFilePath(p_currentFileName));

                        parseMapToFile(p_currentGameState, l_writer, l_mapFormat);
                        p_currentGameState.updateLog("Map Saved Successfully", "effect");
                        l_writer.close();
                    }
                } else {
                    p_currentGameState.updateLog("Validation failed! Cannot Save the Map file!", "effect");
                    p_currentGameState.setError("Validation Failed");
                    return false;
                }
            }
            return true;
        } catch (IOException | MapValidationException l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_currentGameState);
            p_currentGameState.updateLog("Couldn't save the changes in map file!", "effect");
            p_currentGameState.setError("Error in saving map file");
            return false;
        }
    }

    /**
     * Performs the add/remove operation on the countries in map.
     *
     * @param p_currentGameState Current GameState Object
     * @param p_mapToBeUpdatedCorrectly The Map to be updated
     * @param p_currentOperation Operation to be performed
     * @param p_currentArgument Arguments for the pertaining command operation
     * @return Updated Map Object
     * @throws MapValidationException MapValidationException exception
     */
    public Map addRemoveCountry(GameState p_currentGameState, Map p_mapToBeUpdatedCorrectly, String p_currentArgument, String p_currentOperation) throws MapValidationException{

        try {
            if (p_currentOperation.equalsIgnoreCase("add") && p_currentArgument.split(" ").length==2){
                p_mapToBeUpdatedCorrectly.addCountry(p_currentArgument.split(" ")[0], p_currentArgument.split(" ")[1]);
                this.setD_MapServiceLog("Country "+ p_currentArgument.split(" ")[0]+ " added successfully!", p_currentGameState);
            }else if(p_currentOperation.equalsIgnoreCase("remove")&& p_currentArgument.split(" ").length==1){
                p_mapToBeUpdatedCorrectly.removeCountry(p_currentArgument.split(" ")[0]);
                this.setD_MapServiceLog("Country "+ p_currentArgument.split(" ")[0]+ " removed successfully!", p_currentGameState);
            }else{
                throw new MapValidationException("Country "+p_currentArgument.split(" ")[0]+" could not be "+ p_currentOperation +"ed!");
            }
        } catch (MapValidationException l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_currentGameState);
        }
        return p_mapToBeUpdatedCorrectly;
    }

    /**
     * Performs the add/remove operation on Map Object.
     *
     * @param p_currentGameState Current GameState Object
     * @param p_currentMapToBeUpdated The Map to be updated
     * @param p_currentOperation Add/Remove operation to be performed
     * @param p_argumentPassed Arguments for the pertaining command operation
     * @return map to be updated
     * @throws MapValidationException MapValidationException exception
     */
    public Map addRemoveNeighbour(GameState p_currentGameState, Map p_currentMapToBeUpdated, String p_argumentPassed, String p_currentOperation) throws MapValidationException{

        try {
            if (p_currentOperation.equalsIgnoreCase("add") && p_argumentPassed.split(" ").length==2){
                p_currentMapToBeUpdated.addCountryNeighbour(p_argumentPassed.split(" ")[0], p_argumentPassed.split(" ")[1]);
                this.setD_MapServiceLog("Neighbour Pair "+p_argumentPassed.split(" ")[0]+" "+p_argumentPassed.split(" ")[1]+" added successfully!", p_currentGameState);
            }else if(p_currentOperation.equalsIgnoreCase("remove") && p_argumentPassed.split(" ").length==2){
                p_currentMapToBeUpdated.removeCountryNeighbour(p_argumentPassed.split(" ")[0], p_argumentPassed.split(" ")[1]);
                this.setD_MapServiceLog("Neighbour Pair "+p_argumentPassed.split(" ")[0]+" "+p_argumentPassed.split(" ")[1]+" removed successfully!", p_currentGameState);
            }else{
                throw new MapValidationException("Neighbour could not be "+ p_currentOperation +"ed!");
            }
        } catch (MapValidationException l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_currentGameState);
        }
        return p_currentMapToBeUpdated;
    }



    /**
     * Parses the Map Object to File.
     *
     * @param p_currentGameState current gamestate
     * @param l_fileWriter file writer object.
     * @param l_mapFormatUpdated  Map Format
     * @throws IOException Exception
     */
    private void parseMapToFile(GameState p_currentGameState, FileWriter l_fileWriter, String l_mapFormatUpdated) throws IOException {
        if(l_mapFormatUpdated.equalsIgnoreCase("ConquestMap")) {
            MapWriterAdapter l_mapWriterAdapter = new MapWriterAdapter(new ConquestMapFileWriter());
            l_mapWriterAdapter.parseMapToFile(p_currentGameState, l_fileWriter, l_mapFormatUpdated);
        } else {
            new MapFileWriter().parseMapToFile(p_currentGameState, l_fileWriter, l_mapFormatUpdated);
        }
    }



    /**
     * Checks in what format user wants to save the map file.
     *
     * @return String map format to be saved
     * @throws IOException exception in reading inputs from user
     */
    public String getFormatToSave() throws IOException {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Kindly press 1 to save the map as conquest map format or else press 2");
        String l_nextOrderCheck = l_reader.readLine();
        if (l_nextOrderCheck.equalsIgnoreCase("1")) {
            return "ConquestMap";
        } else if (l_nextOrderCheck.equalsIgnoreCase("2")) {
            return "NormalMap";
        } else {
            System.err.println("Invalid Input Passed.");
            return this.getFormatToSave();
        }
    }

    /**
     * Resets Game State's Map.
     *
     * @param p_currentGameState object of GameState class
     * @param p_correctFileToLoad File which couldn't be loaded
     */
    public void resetMap(GameState p_currentGameState, String p_correctFileToLoad) {
        System.err.println("Map cannot be loaded, as it is invalid. Kindly provide valid map");
        p_currentGameState.updateLog(p_correctFileToLoad+" map could not be loaded as it is invalid!", "effect");
        p_currentGameState.setD_map(new Models.Map());
    }

    /**
     * Set the log of map editor methods.
     *
     * @param p_ServiceLogMap String containing log
     * @param p_currentGameState current gamestate instance
     */
    public void setD_MapServiceLog(String p_ServiceLogMap, GameState p_currentGameState){
        System.out.println(p_ServiceLogMap);
        p_currentGameState.updateLog(p_ServiceLogMap, "effect");
    }

}

