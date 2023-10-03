package Controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;


import Exceptions.MapValidationException;
import Logger.ConsoleLogger;
import Models.Continent;
import Models.Country;
import Models.State;
import Models.Map;
import Utils.CommonUtil;

public class MapController {
    ConsoleLogger consoleLogger = new ConsoleLogger();
	public Map loadMap(State p_gameState, String p_loadFileName) {
		Map l_map = new Map();
		List<String> l_linesOfFile = loadFile(p_loadFileName);

		if (null != l_linesOfFile && !l_linesOfFile.isEmpty()) {

			// Parses the file and stores information in objects
			List<String> l_continentData = getMetaData(l_linesOfFile, "continent");
			List<Continent> l_continentObjects = parseContinentsMetaData(l_continentData);
			List<String> l_countryData = getMetaData(l_linesOfFile, "country");
			List<String> l_bordersMetaData = getMetaData(l_linesOfFile, "border");
			List<Country> l_countryObjects = parseCountriesMetaData(l_countryData);

			// Updates the neighbour of countries in Objects
			l_countryObjects = parseBorderMetaData(l_countryObjects, l_bordersMetaData);
			l_continentObjects = linkCountryContinents(l_countryObjects, l_continentObjects);
			l_map.setD_continents(l_continentObjects);
			l_map.setD_countries(l_countryObjects);
			p_gameState.setD_map(l_map);
		}
		return l_map;
	}


    public List<String> getMetaData(List<String> p_fileLines, String p_Parameter) {
		if ("continent".equals(p_Parameter)) {
			return p_fileLines.subList(
					p_fileLines.indexOf("[continents]") + 1,
					p_fileLines.indexOf("[countries]") - 1);
		} else if ("country".equals(p_Parameter)) {
			return p_fileLines.subList(
					p_fileLines.indexOf("[countries]") + 1,
					p_fileLines.indexOf("[borders]") - 1);
		} else if ("border".equals(p_Parameter)) {
			return p_fileLines.subList(
					p_fileLines.indexOf("[borders]") + 1,
					p_fileLines.size());
		} else {
			return null;
		}
	}


    public List<String> loadFile(String p_loadFileName) {

		String l_filePath = CommonUtil.getMapFilePath(p_loadFileName);
		List<String> l_lineList = new ArrayList<>();

		BufferedReader l_reader;
		try {
			l_reader = new BufferedReader(new FileReader(l_filePath));
			l_lineList = l_reader.lines().collect(Collectors.toList());
			l_reader.close();
		} catch (IOException l_e1) {
			consoleLogger.writeLog("File not Found!");
		}
		return l_lineList;
	}


    public List<Continent> parseContinentsMetaData(List<String> p_continentList) {
		int l_continentId = 1;
		List<Continent> l_continents = new ArrayList<Continent>();

		for (String cont : p_continentList) {
			String[] l_metaData = cont.split(" ");
			l_continents.add(new Continent(l_continentId, l_metaData[0], Integer.parseInt(l_metaData[1])));
			l_continentId++;
		}
		return l_continents;
	}


	public List<Continent> linkCountryContinents(List<Country> p_countries, List<Continent> p_continents) {
		for (Country c : p_countries) {
			for (Continent cont : p_continents) {
				if (cont.getD_continentID().equals(c.getD_continentId())) {
					cont.addCountry(c);
				}
			}
		}
		return p_continents;
	}


    public List<Country> parseCountriesMetaData(List<String> p_countriesList) {

		LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();
		List<Country> l_countriesList = new ArrayList<Country>();

		for (String country : p_countriesList) {
			String[] l_metaDataCountries = country.split(" ");
			l_countriesList.add(new Country(Integer.parseInt(l_metaDataCountries[0]), l_metaDataCountries[1],
					Integer.parseInt(l_metaDataCountries[2])));
		}
		return l_countriesList;
	}


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


    public void editMap(State p_gameState, String p_editFilePath) throws IOException {

		String l_filePath = CommonUtil.getMapFilePath(p_editFilePath);
		File l_fileToBeEdited = new File(l_filePath);

		if (l_fileToBeEdited.createNewFile()) {
			consoleLogger.writeLog("File has been created.");
			Map l_map = new Map();
			l_map.setD_mapFile(p_editFilePath);
			p_gameState.setD_map(l_map);
		} else {
			consoleLogger.writeLog("File already exists.");
			this.loadMap(p_gameState, p_editFilePath);
			if (null == p_gameState.getD_map()) {
				p_gameState.setD_map(new Map());
			}
			p_gameState.getD_map().setD_mapFile(p_editFilePath);
		}
	}

    public void editContinent(State p_gameState, String p_argument, String p_operation) throws IOException, MapValidationException {
        String l_mapFileName = p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents())
                && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.loadMap(p_gameState, l_mapFileName)
                : p_gameState.getD_map();

        if(!CommonUtil.isNull(l_mapToBeUpdated)) {
            Map l_updatedMap = addRemoveContinents(l_mapToBeUpdated, p_operation, p_argument);
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }
    }

    public Map addRemoveContinents(Map p_mapToBeUpdated, String p_operation,
                                String p_argument) throws MapValidationException {

        if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2) {
            p_mapToBeUpdated.addContinent(p_argument.split(" ")[0], Integer.parseInt(p_argument.split(" ")[1]));
        } else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==1) {
            p_mapToBeUpdated.removeContinent(p_argument.split(" ")[0]);
        } else {
            consoleLogger.writeLog("Continent couldn't be added/removed. Changes are not made");
        }

        return p_mapToBeUpdated;
    }

    public void editCountry(State p_gameState, String p_operation, String p_argument) throws MapValidationException{
        String l_mapFileName= p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents())
                && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.loadMap(p_gameState, l_mapFileName)
                : p_gameState.getD_map();

        if(!CommonUtil.isNull(l_mapToBeUpdated)) {
            Map l_updatedMap = addRemoveCountry(l_mapToBeUpdated, p_operation, p_argument);
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }
    }

    public Map addRemoveCountry(Map p_mapToBeUpdated, String p_operation, String p_argument) throws MapValidationException{
        if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
            p_mapToBeUpdated.addCountry(p_argument.split(" ")[0], p_argument.split(" ")[1]);
        }else if(p_operation.equalsIgnoreCase("remove")&& p_argument.split(" ").length==1){
            p_mapToBeUpdated.removeCountry(p_argument.split(" ")[0]);
        }else{
            consoleLogger.writeLog("Couldn't Save your changes");
        }
        return p_mapToBeUpdated;
    }

    public void editNeighbour(State p_gameState, String p_operation, String p_argument) throws MapValidationException{
        String l_mapFileName= p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents())
                && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.loadMap(p_gameState, l_mapFileName)
                : p_gameState.getD_map();

        if(!CommonUtil.isNull(l_mapToBeUpdated)) {
            Map l_updatedMap = addRemoveNeighbour(l_mapToBeUpdated, p_operation, p_argument);
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }
    }

    public Map addRemoveNeighbour(Map p_mapToBeUpdated, String p_operation, String p_argument) throws MapValidationException{
        if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
            p_mapToBeUpdated.addCountryNeighbours(p_argument.split(" ")[0], p_argument.split(" ")[1]);
        }else if(p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==2){
            p_mapToBeUpdated.removeCountryNeighbours(p_argument.split(" ")[0], p_argument.split(" ")[1]);
        }else{
            consoleLogger.writeLog("Couldn't Save your changes");
        }
        return p_mapToBeUpdated;
    }

    public boolean saveMap(State p_gameState, String p_fileName) throws MapValidationException {
        try {

            // Verifies if the file linked to savemap and edited by user are same
            if (!p_fileName.equalsIgnoreCase(p_gameState.getD_map().getD_mapFile())) {
                p_gameState.setError("Kindly provide same file name to save which you have given for edit");
                return false;
            } else {
                if (null != p_gameState.getD_map()) {
                    Models.Map l_currentMap = p_gameState.getD_map();

                    // Proceeds to save the map if it passes the validation check
                    consoleLogger.writeLog("Validating Map......");
                    boolean l_mapValidationStatus = l_currentMap.Validate();
                    if (l_mapValidationStatus) {
                        Files.deleteIfExists(Paths.get(CommonUtil.getMapFilePath(p_fileName)));
                        FileWriter l_writer = new FileWriter(CommonUtil.getMapFilePath(p_fileName));

                        if (null != p_gameState.getD_map().getD_continents()
                                && !p_gameState.getD_map().getD_continents().isEmpty()) {
                            writeContinentMetadata(p_gameState, l_writer);
                        }
                        if (null != p_gameState.getD_map().getD_countries()
                                && !p_gameState.getD_map().getD_countries().isEmpty()) {
                            writeCountryAndBoarderMetaData(p_gameState, l_writer);
                        }
                        l_writer.close();
                    }
                } else {
                    consoleLogger.writeLog("Validation Failed");
                    return false;
                }
            }
            return true;
        } catch (IOException l_e) {
            l_e.printStackTrace();
            consoleLogger.writeLog("Error in saving map file");
            return false;
        }
    }

    private void writeCountryAndBoarderMetaData(State p_gameState, FileWriter p_writer) throws IOException {
        String l_countryMetaData;
        String l_bordersMetaData;
        List<String> l_bordersList = new ArrayList<>();

        // Writes Country Objects to File And Organizes Border Data for each of them
        p_writer.write(System.lineSeparator() + "[countries]" + System.lineSeparator());
        for (Country l_country : p_gameState.getD_map().getD_countries()) {
            l_countryMetaData = l_country.getD_countryId().toString().concat(" ").concat(l_country.getD_countryName())
                    .concat(" ").concat(l_country.getD_continentId().toString());
            p_writer.write(l_countryMetaData + System.lineSeparator());

            if (null != l_country.getD_adjacentCountryIds() && !l_country.getD_adjacentCountryIds().isEmpty()) {
                l_bordersMetaData = l_country.getD_countryId().toString();
                for (Integer l_adjCountry : l_country.getD_adjacentCountryIds()) {
                    l_bordersMetaData = l_bordersMetaData.concat(" ").concat(l_adjCountry.toString());
                }
                l_bordersList.add(l_bordersMetaData);
            }
        }

        //Writes Border data to the File
        if (!l_bordersList.isEmpty()) {
            p_writer.write(System.lineSeparator() + "[borders]" + System.lineSeparator());
            for (String l_borderStr : l_bordersList) {
                p_writer.write(l_borderStr + System.lineSeparator());
            }
        }
    }

    private void writeContinentMetadata(State p_gameState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + "[continents]" + System.lineSeparator());
        for (Continent l_continent : p_gameState.getD_map().getD_continents()) {
            p_writer.write(
                    l_continent.getD_continentName().concat(" ").concat(l_continent.getD_continentValue().toString())
                            + System.lineSeparator());
        }
    }

    public void resetMap(State p_gameState) {
        consoleLogger.writeLog("Map cannot be loaded, as it is invalid. Kindly provide valid map");
        p_gameState.setD_map(new Models.Map());
    }
}
