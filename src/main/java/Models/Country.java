package Models;

import Logger.ConsoleLogger;

import java.util.ArrayList;
import java.util.List;

public class Country {

    ConsoleLogger consoleLogger = new ConsoleLogger();
    Integer d_armies;
    Integer d_countryId;
    Integer d_continentId;
    String d_countryName;
    List<Integer> d_adjacentCountryIds = new ArrayList<Integer>();;

    public Country(int p_countryId, String p_countryName, int p_continentId) {
        d_countryId = p_countryId;
        d_countryName = p_countryName;
        d_continentId = p_continentId;
    }

    public Country(String p_countryName) {
        d_countryName = p_countryName;
    }

}
