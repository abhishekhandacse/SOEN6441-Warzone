package Models;

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

    public Country getCountry(Integer p_countryId) {
        return d_countries.stream().filter(l_country -> l_country.getD_countryId().equals(p_countryId)).findFirst().orElse(null);
    }


    public Country getCountryByName(String p_countryName){
        return d_countries.stream().filter(l_country -> l_country.getD_countryName().equals(p_countryName)).findFirst().orElse(null);
    }


    public Continent getContinent(String p_continentName){
        return d_continents.stream().filter(l_continent -> l_continent.getD_continentName().equals(p_continentName)).findFirst().orElse(null);
    }


    public Continent getContinentByID(Integer p_continentID){
        return d_continents.stream().filter(l_continent -> l_continent.getD_continentID().equals(p_continentID)).findFirst().orElse(null);
    }

    public void checkCountries() {
        for (Country c: d_countries) {
            consoleLogger.writeLog("Country Id "+ c.getD_countryId());
            consoleLogger.writeLog("Continent Id "+c.getD_continentId());
            consoleLogger.writeLog("Neighbours:");
            for (int i: c.getD_adjacentCountryIds()) {
                System.out.println(i);
            }
        }
    }


    public Boolean Validate() throws MapValidationException {
        return (!checkForNullObjects() && checkConnectionOfContinent() && checkConnectionOfCountry());
    }

    public Boolean checkForNullObjects() throws MapValidationException{
        if(d_continents==null || d_continents.isEmpty()){
            throw new MapValidationException("Map must possess atleast one continent!");
        }
        if(d_countries==null || d_countries.isEmpty()){
            throw new MapValidationException("Map must possess atleast one country!");
        }
        for(Country c: d_countries){
            if(c.getD_adjacentCountryIds().size()<1){
                throw new MapValidationException(c.getD_countryName()+" does not possess any neighbour, hence isn't reachable!");
            }
        }
        return false;
    }

    public Boolean checkConnectionOfContinent() throws MapValidationException {
        boolean l_flagConnectivity=true;
        for (Continent c:d_continents){
            if (null == c.getD_countries() || c.getD_countries().size()<1){
                throw new MapValidationException(c.getD_continentName() + " has no countries, it must possess atleast 1 country");
            }
            if(!subGraphConnectivity(c)){
                l_flagConnectivity=false;
            }
        }
        return l_flagConnectivity;
    }

    public boolean subGraphConnectivity(Continent p_continent) throws MapValidationException {
        HashMap<Integer, Boolean> l_continentCountry = new HashMap<Integer, Boolean>();

        for (Country c : p_continent.getD_countries()) {
            l_continentCountry.put(c.getD_countryId(), false);
        }
        dfsSubgraph(p_continent.getD_countries().get(0), l_continentCountry, p_continent);

        // Iterates Over Entries to locate unreachable countries in continent
        for (Entry<Integer, Boolean> entry : l_continentCountry.entrySet()) {
            if (!entry.getValue()) {
                Country l_country = getCountry(entry.getKey());
                String l_messageException = l_country.getD_countryName() + " in Continent " + p_continent.getD_continentName() + " is not reachable";
                throw new MapValidationException(l_messageException);
            }
        }
        return !l_continentCountry.containsValue(false);
    }

}
