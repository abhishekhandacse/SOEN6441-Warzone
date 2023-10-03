package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Exceptions.MapValidationException;
import Logger.ConsoleLogger;
import Utils.CommonUtil;
import java.util.Collections;
import java.util.Map.Entry;


public class Map {

    ConsoleLogger consoleLogger = new ConsoleLogger();

    String d_mapFile;
    List<Continent> d_continents;
    List<Country> d_countries;
    HashMap<Integer, Boolean> d_countryReach = new HashMap<Integer, Boolean>();

    public void setD_mapFile(String p_mapFile) {
        this.d_mapFile = p_mapFile;
    }

    public void setD_continents(List<Continent> p_continents) {
        this.d_continents = p_continents;
    }

    public void setD_countries(List<Country> p_countries) {
        this.d_countries = p_countries;
    }

    public List<Continent> getD_continents() {
        return d_continents;
    }

    public List<Country> getD_countries() {
        return d_countries;
    }

    public String getD_mapFile() {
        return d_mapFile;
    }

    public List<Integer> getCountryIDs(){
        List<Integer> l_countryIDs = new ArrayList<Integer>();
        if(!d_countries.isEmpty()){
            for(Country c: d_countries){
                l_countryIDs.add(c.getD_countryId());
            }
        }
        return l_countryIDs;
    }

    public List<Integer> getContinentIDs(){
        List<Integer> l_continentIDs = new ArrayList<Integer>();
        if (!d_continents.isEmpty()) {
            for(Continent c: d_continents){
                l_continentIDs.add(c.getD_continentID());
            }
        }
        return l_continentIDs;
    }

    public void addContinent(Continent p_continent){
        d_continents.add(p_continent);
    }

    public void addCountry(Country p_country){
        d_countries.add(p_country);
    }

    public void checkContinents() {
        for(Continent c: d_continents) {
            System.out.println(c.getD_continentID());
        }
    }


}
