package Services;

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

import Constants.ApplicationConstantsHardcoding;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.ModelCountry;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;

public class MapService {


	public Map loadMap(GameState p_gameState, String p_loadFileName) throws InvalidMap {
		Map l_loadedMap = new Map();
		List<String> l_fileLines = fileLoad(p_loadFileName);

		if (null != l_fileLines && !l_fileLines.isEmpty()) {


			List<String> l_contListData = getMetaData(l_fileLines, "continent");
			List<Continent> l_contListObjects = parseMetadataForContinents(l_contListData);
			List<String> l_countryListData = getMetaData(l_fileLines, "country");
			List<String> l_metaDataBorders = getMetaData(l_fileLines, "border");
			List<ModelCountry> l_countryListObjects = parseMetadataForCountries(l_countryListData);


			l_countryListObjects = parseBorderMetaData(l_countryListObjects, l_metaDataBorders);
			l_contListObjects = linkCountryContinents(l_countryListObjects, l_contListObjects);
			l_loadedMap.setD_allContinents(l_contListObjects);
			l_loadedMap.setD_allCountries(l_countryListObjects);
			p_gameState.setD_map(l_loadedMap);
		}
		return l_loadedMap;
	}


	public List<String> fileLoad(String p_fileNameLoaded) throws InvalidMap{

		String l_pathForFile = CommonUtil.getAbsolutePathForFile(p_fileNameLoaded);
		List<String> l_listLine;

		BufferedReader l_bufferedReader;
		try {

			l_bufferedReader = new BufferedReader(new FileReader(l_pathForFile));
			l_listLine = l_bufferedReader.lines().collect(Collectors.toList());
			l_bufferedReader.close();
		} catch (IOException l_e1) {
			throw new InvalidMap("Map File not Found!");
		}
		return l_listLine;
	}


	public List<String> getMetaData(List<String> p_fileLines, String p_switchParameter) {
		switch (p_switchParameter) {
		case "continent":
			List<String> l_continentLines = p_fileLines.subList(
			p_fileLines.indexOf(ApplicationConstantsHardcoding.ALL_CONTINENTS) + 1,
			p_fileLines.indexOf(ApplicationConstantsHardcoding.ALL_COUNTRIES) - 1);
			return l_continentLines;
		case "country":
			List<String> l_countryLines = p_fileLines.subList(p_fileLines.indexOf(ApplicationConstantsHardcoding.ALL_COUNTRIES) + 1,
			p_fileLines.indexOf(ApplicationConstantsHardcoding.ALL_BORDERS) - 1);
			return l_countryLines;
		case "border":
			List<String> l_bordersLines = p_fileLines.subList(p_fileLines.indexOf(ApplicationConstantsHardcoding.ALL_BORDERS) + 1,
			p_fileLines.size());
			return l_bordersLines;
		default:
			return null;
		}
	}




	public List<ModelCountry> parseMetadataForCountries(List<String> p_countriesList) {

		LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();
		List<ModelCountry> l_countriesList = new ArrayList<ModelCountry>();

		for (String country : p_countriesList) {
			String[] l_metaDataCountries = country.split(" ");
			l_countriesList.add(new ModelCountry(Integer.parseInt(l_metaDataCountries[0]), l_metaDataCountries[1],
					Integer.parseInt(l_metaDataCountries[2])));
		}
		return l_countriesList;
	}

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





	public void editMap(GameState p_gameState, String p_editFilePath) throws IOException, InvalidMap {

		String l_filePath = CommonUtil.getAbsolutePathForFile(p_editFilePath);
		File l_fileToBeEdited = new File(l_filePath);

		if (l_fileToBeEdited.createNewFile()) {
			System.out.println("File has been created.");
			Map l_map = new Map();
			l_map.setD_inputMapFile(p_editFilePath);
			p_gameState.setD_map(l_map);
			p_gameState.updateLog(p_editFilePath+ " File has been created for user to edit", "effect");
		} else {
			System.out.println("File already exists.");
			this.loadMap(p_gameState, p_editFilePath);
			if (null == p_gameState.getD_map()) {
				p_gameState.setD_map(new Map());
			}
			p_gameState.getD_map().setD_inputMapFile(p_editFilePath);
			p_gameState.updateLog(p_editFilePath+ " already exists and is loaded for editing", "effect");
		}
	}
	public List<Continent> linkCountryContinents(List<ModelCountry> p_countries, List<Continent> p_continents) {
		for (ModelCountry c : p_countries) {
			for (Continent cont : p_continents) {
				if (cont.getD_continentID().equals(c.getD_continentId())) {
					cont.addingCountry(c);
				}
			}
		}
		return p_continents;
	}




	public Map addRemoveContinents(GameState p_gameState, Map p_mapToBeUpdated, String p_operation, String p_argument) throws InvalidMap {

		try {
			if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2) {
				p_mapToBeUpdated.addContinent(p_argument.split(" ")[0], Integer.parseInt(p_argument.split(" ")[1]));
				this.setD_logMapService("Continent "+ p_argument.split(" ")[0]+ " added successfully!", p_gameState);
			} else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==1) {
				p_mapToBeUpdated.removeContinent(p_argument.split(" ")[0]);
				this.setD_logMapService("Continent "+ p_argument.split(" ")[0]+ " removed successfully!", p_gameState);
			} else {
				throw new InvalidMap("Continent "+p_argument.split(" ")[0]+" couldn't be added/removed. Changes are not made due to Invalid Command Passed.");
			}
		} catch (InvalidMap | NumberFormatException l_e) {
			this.setD_logMapService(l_e.getMessage(), p_gameState);
		}
		return p_mapToBeUpdated;
	}


	public List<ModelCountry> parseBorderMetaData(List<ModelCountry> p_countriesList, List<String> p_bordersList) {
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
		for (ModelCountry c : p_countriesList) {
			List<Integer> l_adjacentCountries = l_countryNeighbors.get(c.getD_countryId());
			c.setD_adjacentCountryIds(l_adjacentCountries);
		}
		return p_countriesList;
	}




	public void editFunctions(GameState p_gameState, String p_argument, String p_operation, Integer p_switchParameter) throws IOException, InvalidMap, InvalidCommand{
		Map l_updatedMap;
		String l_mapFileName = p_gameState.getD_map().getD_inputMapFile();
		Map l_mapToBeUpdated = (CommonUtil.isNullObject(p_gameState.getD_map().getD_allContinents()) && CommonUtil.isNullObject(p_gameState.getD_map().getD_allCountries())) ? this.loadMap(p_gameState, l_mapFileName) : p_gameState.getD_map();

		// Edit Control Logic for Continent, Country & Neighbor
		if(!CommonUtil.isNullObject(l_mapToBeUpdated)){
			switch(p_switchParameter){
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

	public Map addRemoveCountry(GameState p_gameState, Map p_mapToBeUpdated, String p_argument, String p_operation) throws InvalidMap{

		try {
			if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
				p_mapToBeUpdated.addCountry(p_argument.split(" ")[0], p_argument.split(" ")[1]);
				this.setD_logMapService("Country "+ p_argument.split(" ")[0]+ " added successfully!", p_gameState);
			}else if(p_operation.equalsIgnoreCase("remove")&& p_argument.split(" ").length==1){
				p_mapToBeUpdated.removeCountry(p_argument.split(" ")[0]);
				this.setD_logMapService("Country "+ p_argument.split(" ")[0]+ " removed successfully!", p_gameState);
			}else{
				throw new InvalidMap("Country "+p_argument.split(" ")[0]+" could not be "+ p_operation +"ed!");
			}
		} catch (InvalidMap l_e) {
			this.setD_logMapService(l_e.getMessage(), p_gameState);
		}
		return p_mapToBeUpdated;
	}




	public boolean mapSave(GameState p_gameState, String p_fileName) throws InvalidMap {
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
		} catch (IOException | InvalidMap l_e) {
			this.setD_logMapService(l_e.getMessage(), p_gameState);
			p_gameState.updateLog("Couldn't save the changes in map file!", "effect");
			p_gameState.setError("Error in saving map file");
			return false;
		}
	}

	public Map addRemoveNeighbour(GameState p_gameState, Map p_mapToBeUpdated, String p_argument, String p_operation) throws InvalidMap{

		try {
			if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
				p_mapToBeUpdated.addCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
				this.setD_logMapService("Neighbour Pair "+p_argument.split(" ")[0]+" "+p_argument.split(" ")[1]+" added successfully!", p_gameState);
			}else if(p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==2){
				p_mapToBeUpdated.removeCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
				this.setD_logMapService("Neighbour Pair "+p_argument.split(" ")[0]+" "+p_argument.split(" ")[1]+" removed successfully!", p_gameState);
			}else{
				throw new InvalidMap("Neighbour could not be "+ p_operation +"ed!");
			}
		} catch (InvalidMap l_e) {
			this.setD_logMapService(l_e.getMessage(), p_gameState);
		}
		return p_mapToBeUpdated;
	}
	private void writeCountryAndBoarderMetaData(GameState p_gameState, FileWriter p_writer) throws IOException {
		String l_countryMetaData = new String();
		String l_bordersMetaData = new String();
		List<String> l_bordersList = new ArrayList<>();

		// Writes Country Objects to File And Organizes Border Data for each of them
		p_writer.write(System.lineSeparator() + ApplicationConstantsHardcoding.ALL_COUNTRIES + System.lineSeparator());
		for (ModelCountry l_country : p_gameState.getD_map().getD_allCountries()) {
			l_countryMetaData = new String();
			l_countryMetaData = l_country.getD_countryId().toString().concat(" ").concat(l_country.getD_countryName())
					.concat(" ").concat(l_country.getD_continentId().toString());
			p_writer.write(l_countryMetaData + System.lineSeparator());

			if (null != l_country.getD_adjacentCountryIds() && !l_country.getD_adjacentCountryIds().isEmpty()) {
				l_bordersMetaData = new String();
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





	public void mapReset(GameState p_gameState, String p_fileToLoad) {
		System.out.println("Map cannot be loaded, as it is invalid. Kindly provide valid map");
		p_gameState.updateLog(p_fileToLoad+" map could not be loaded as it is invalid!", "effect");
		p_gameState.setD_map(new Map());
	}




	public void setD_logMapService(String p_MapServiceLog, GameState p_gameState){
		System.out.println(p_MapServiceLog);
		p_gameState.updateLog(p_MapServiceLog, "effect");
	}

	private void writeMetadataForContinent(GameState p_gameState, FileWriter p_writer) throws IOException {
		p_writer.write(System.lineSeparator() + ApplicationConstantsHardcoding.ALL_CONTINENTS + System.lineSeparator());
		for (Continent l_continent : p_gameState.getD_map().getD_allContinents()) {
			p_writer.write(
					l_continent.getD_continentName().concat(" ").concat(l_continent.getD_continentValue().toString())
							+ System.lineSeparator());
		}
	}
}
