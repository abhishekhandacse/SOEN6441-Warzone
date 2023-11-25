/**
 * The {@code ConquestMapFileReader} class reads and parses Conquest map files to create game entities.
 *
 * @version 1.0
 */
package Services;

import Constants.ApplicationConstants;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and parses Conquest map files to create game entities.
 */
public class ConquestMapFileReader implements Serializable {

    /**
     * Reads Conquest map file, extracts metadata, and populates the game state.
     *
     * @param p_gameState   The current game state.
     * @param p_map         The game map.
     * @param p_linesOfFile The lines of the Conquest map file.
     */
    public void readConquestFile(GameState p_gameState, Map p_map, List<String> p_linesOfFile) {
        List<String> l_continentData = getMetaData(p_linesOfFile, "continent");
        List<Continent> l_continentObjects = parseContinentsMetaData(l_continentData);
        List<String> l_countryData = getMetaData(p_linesOfFile, "country");
        List<Country> l_countryObjects = parseCountriesMetaData(l_countryData, l_continentObjects);
        List<Country> l_updatedCountries = parseBorderMetaData(l_countryObjects, l_countryData);

        l_continentObjects = linkCountryContinents(l_updatedCountries, l_continentObjects);
        p_map.setD_continents(l_continentObjects);
        p_map.setD_countriesList(l_countryObjects);
        p_gameState.setD_map(p_map);
    }

    /**
     * Extracts metadata lines from the Conquest map file.
     *
     * @param p_fileLines       The lines of the Conquest map file.
     * @param p_switchParameter The parameter to switch between continent and country metadata.
     * @return The list of metadata lines.
     */
    public List<String> getMetaData(List<String> p_fileLines, String p_switchParameter) {
        switch (p_switchParameter) {
            case "continent":
                List<String> l_continentLines = p_fileLines.subList(
                        p_fileLines.indexOf(ApplicationConstants.CONQUEST_CONTINENTS) + 1,
                        p_fileLines.indexOf(ApplicationConstants.CONQUEST_TERRITORIES) - 1);
                return l_continentLines;
            case "country":
                List<String> l_countryLines = p_fileLines
                        .subList(p_fileLines.indexOf(ApplicationConstants.CONQUEST_TERRITORIES) + 1, p_fileLines.size());
                return l_countryLines;
            default:
                return null;
        }
    }

    /**
     * Parses continent metadata lines and creates continent objects.
     *
     * @param p_continentList The list of continent metadata lines.
     * @return The list of continent objects.
     */
    public List<Continent> parseContinentsMetaData(List<String> p_continentList) {
        int l_continentId = 1;
        List<Continent> l_continents = new ArrayList<>();

        for (String cont : p_continentList) {
            String[] l_metaData = cont.split("=");
            l_continents.add(new Continent(l_continentId, l_metaData[0], Integer.parseInt(l_metaData[1])));
            l_continentId++;
        }
        return l_continents;
    }

    /**
     * Parses country metadata lines and creates country objects.
     *
     * @param p_countriesList The list of country metadata lines.
     * @param p_continentList The list of continent objects.
     * @return The list of country objects.
     */
    public List<Country> parseCountriesMetaData(List<String> p_countriesList, List<Continent> p_continentList) {
        List<Country> l_countriesList = new ArrayList<>();
        int l_country_id = 1;
        for (String country : p_countriesList) {
            String[] l_metaDataCountries = country.split(",");
            Continent l_continent = this.getContinentByName(p_continentList, l_metaDataCountries[3]);
            Country l_countryObj = new Country(l_country_id, l_metaDataCountries[0],
                    l_continent.getD_continentID());
            l_countriesList.add(l_countryObj);
            l_country_id++;
        }
        return l_countriesList;
    }

    /**
     * Parses border metadata lines and updates country objects with adjacent country IDs.
     *
     * @param p_countriesList The list of country objects.
     * @param p_countryLines  The list of country metadata lines.
     * @return The updated list of country objects.
     */
    public List<Country> parseBorderMetaData(List<Country> p_countriesList, List<String> p_countryLines) {
        List<Country> l_updatedCountryList = new ArrayList<>(p_countriesList);
        String l_matchedCountry = null;
        for (Country l_cont : l_updatedCountryList) {
            for (String l_contStr : p_countryLines) {
                if ((l_contStr.split(",")[0]).equalsIgnoreCase(l_cont.getD_countryName())) {
                    l_matchedCountry = l_contStr;
                    break;
                }
            }
            if (l_matchedCountry.split(",").length > 4) {
                for (int i = 4; i < l_matchedCountry.split(",").length; i++) {
                    Country l_country = this.getCountryByName(p_countriesList, l_matchedCountry.split(",")[i]);
                    l_cont.getD_adjacentCountryIds().add(l_country.getD_countryId());
                }
            }
        }
        return l_updatedCountryList;
    }

    /**
     * Links country objects to their respective continents.
     *
     * @param p_countries  The list of country objects.
     * @param p_continents The list of continent objects.
     * @return The updated list of continent objects.
     */
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

    /**
     * Retrieves a continent by its name from the list of continents.
     *
     * @param p_continentList The list of continent objects.
     * @param p_continentName The name of the continent to retrieve.
     * @return The continent object if found, otherwise {@code null}.
     */
    public Continent getContinentByName(List<Continent> p_continentList, String p_continentName) {
        Continent l_continent = p_continentList.stream()
                .filter(l_cont -> l_cont.getD_continentName().equalsIgnoreCase(p_continentName))
                .findFirst().orElse(null);
        return l_continent;
    }

    /**
     * Retrieves a country by its name from the list of countries.
     *
     * @param p_countryList The list of country objects.
     * @param p_countryName The name of the country to retrieve.
     * @return The country object if found, otherwise {@code null}.
     */
    public Country getCountryByName(List<Country> p_countryList, String p_countryName) {
        Country l_country = p_countryList.stream()
                .filter(l_cont -> l_cont.getD_countryName().equalsIgnoreCase(p_countryName))
                .findFirst().orElse(null);
        return l_country;
    }
}
