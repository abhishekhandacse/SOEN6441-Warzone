package Models;

import Exceptions.MapValidationException;
import Utils.CommonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * this model class manages all the maps.
 */
public class Map {


    /**
     * list of countries.
     */
    List<Country> d_allCountries;

	/**
	 * stores the map file name.
	 */
	String d_inputMapFile;

	/**
	 * list of continents.
	 */
	List<Continent> d_allContinents;


	/**
	 * HashMap of the countries one can reach from the existing position.
	 */
    HashMap<Integer, Boolean> d_allCountriesReachable = new HashMap<Integer, Boolean>();

	/**
	 * getter method to get the map file.
	 * 
	 * @return d_mapfile
	 */
	public String getD_inputMapFile() {
		return d_inputMapFile;
	}

    /**
     * getter method to get the list of continents.
     *
     * @return the list of continents
     */
    public List<Continent> getD_allContinents() {
        return d_allContinents;
    }

	/**
	 * setter method to set the map file.
	 * 
	 * @param p_mapFile mapfile name
	 */
	public void setD_inputMapFile(String p_mapFile) {
		this.d_inputMapFile = p_mapFile;
	}

	/**
	 * setter method to set the list of continents.
	 * 
	 * @param p_continents list of continents
	 */
	public void setD_allContinents(List<Continent> p_continents) {
		this.d_allContinents = p_continents;
	}

	/**
	 * getter method to get the list of countries.
	 * 
	 * @return list of countries
	 */
	public List<Country> getD_allCountries() {
		return d_allCountries;
	}

	/**
	 * setter method to set the countries.
	 *
	 * @param p_countries list of countries
	 */
	public void setD_allCountries(List<Country> p_countries) {
		this.d_allCountries = p_countries;
	}

	/**
	 * adds the continent to the map.
	 *
	 * @param p_continent continent to add
	 */
	public void addContinent(Continent p_continent){
		d_allContinents.add(p_continent);
	}

	/**
	 * adds the country to the map.
	 *
	 * @param p_country country to add
	 */
	public void addCountry(Country p_country){
		d_allCountries.add(p_country);
	}

    /**
     * Get a list of all Ids of countries in Map.
     *
     * @return List of Country Ids
     */
    public List<Integer> getCountryIDs(){
        List<Integer> l_countryIDs = new ArrayList<Integer>();
        if(!d_allCountries.isEmpty()){
            for(Country c: d_allCountries){
                l_countryIDs.add(c.getD_countryId());
            }
        }
        return l_countryIDs;
    }

    /**
     * Get a List of all Ids of continents in Map.
     * 
     * @return List of Continent Ids
     */
    public List<Integer> getContinentIDs(){
        List<Integer> l_continentIDs = new ArrayList<Integer>();
        if (!d_allContinents.isEmpty()) {
            for(Continent c: d_allContinents){
                l_continentIDs.add(c.getD_continentID());
            }
        }
        return l_continentIDs;
    }


    /**
     * check the existing countries.
     */
    public void checkCountries() {
        for (Country c: d_allCountries) {
            System.out.println("Country Id "+ c.getD_countryId());
            System.out.println("Continent Id "+c.getD_continentId());
            System.out.println("Neighbours:");
            for (int i: c.getD_adjacentCountryIds()) {
                System.out.println(i);
            }
        }
    }

	/**
	 * check the existing continents.
	 */
	public void checkContinents() {
		for(Continent c: d_allContinents) {
			System.out.println(c.getD_continentID());
		}
	}

    /**
     * Validates the complete map.
     *
     * @return Bool Value if map is valid
     * @throws MapValidationException Exception
     */
    public Boolean Validate() throws MapValidationException {
        return (!checkForNullObjects() && checkContinentConnectivity() && checkCountryConnectivity());
    }

    /** 
     * Performs Null Check on Objects in Map.
     *
     * @return Boolean if it is false
     * @throws MapValidationException for corresponding Invalid conditions
     */
    public Boolean checkForNullObjects() throws MapValidationException{
        if(d_allContinents ==null || d_allContinents.isEmpty()){
            throw new MapValidationException("Map must possess atleast one continent!");
        }
        if(d_allCountries ==null || d_allCountries.isEmpty()){
            throw new MapValidationException("Map must possess atleast one country!");
        }
        for(Country c: d_allCountries){
            if(c.getD_adjacentCountryIds().size()<1){
                throw new MapValidationException(c.getD_countryName()+" does not possess any neighbour, hence isn't reachable!");
            }
        }
        return false;
    }

    /**
     * Checks All Continent's Inner Connectivity.
	 *
	 * @return Boolean Value if all are connected
	 * @throws MapValidationException if any continent is not Connected
	 */
	public Boolean checkContinentConnectivity() throws MapValidationException {
		boolean l_flagConnectivity=true;
		for (Continent c: d_allContinents){
			if (null == c.getD_countriesList() || c.getD_countriesList().size()<1){
				throw new MapValidationException(c.getD_continentName() + " has no countries, it must possess atleast 1 country");
			}
			if(!subGraphConnectivity(c)){
				l_flagConnectivity=false;
			}
		}
		return l_flagConnectivity;
	}

    /**
     * Checks Inner Connectivity of a Continent.
     *
     * @param p_continent Continent being checked
     * @return Bool Value if Continent is Connected
     * @throws MapValidationException Which country is not connected
     */
    public boolean subGraphConnectivity(Continent p_continent) throws MapValidationException {
        HashMap<Integer, Boolean> l_continentCountry = new HashMap<Integer, Boolean>();

        for (Country c : p_continent.getD_countriesList()) {
            l_continentCountry.put(c.getD_countryId(), false);
        }
        dfsSubgraph(p_continent.getD_countriesList().get(0), l_continentCountry, p_continent);

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

    /**
     * DFS Applied to the Continent Subgraph.
	 *
	 * @param p_c country visited
     * @param p_continentCountry Hashmap of Visited Boolean Values
     * @param p_continent continent being checked for connectivity
     */
    public void dfsSubgraph(Country p_c, HashMap<Integer, Boolean> p_continentCountry, Continent p_continent) {
        p_continentCountry.put(p_c.getD_countryId(), true);
        for (Country c : p_continent.getD_countriesList()) {
            if (p_c.getD_adjacentCountryIds().contains(c.getD_countryId())) {
                if (!p_continentCountry.get(c.getD_countryId())) {
                    dfsSubgraph(c, p_continentCountry, p_continent);
                }
            }
        }
    }

    /**
     * Checks country connectivity in the map.
	 *
	 * @return boolean value for condition if all the countries are connected
     * @throws MapValidationException pointing out which Country is not connected
     */
    public boolean checkCountryConnectivity() throws MapValidationException {
        for (Country c : d_allCountries) {
            d_allCountriesReachable.put(c.getD_countryId(), false);
        }
        dfsCountry(d_allCountries.get(0));

        // Iterates over entries to locate the unreachable country
        for (Entry<Integer, Boolean> entry : d_allCountriesReachable.entrySet()) {
            if (!entry.getValue()) {
                String l_exceptionMessage = getCountry(entry.getKey()).getD_countryName() + " country is not reachable";
                throw new MapValidationException(l_exceptionMessage);
            }
        }
        return !d_allCountriesReachable.containsValue(false);
    }


    /**
     * Gets the Adjacent Country Objects.
	 *
	 * @param p_countryOfInterest the adjacent country
	 * @return list of Adjacent Country Objects
	 * @throws MapValidationException pointing out which Country is not connected
     * @throws MapValidationException Exception
     */
    public List<Country> getAdjacentCountry(Country p_countryOfInterest) throws MapValidationException {
        List<Country> l_adjCountries = new ArrayList<Country>();

        if (p_countryOfInterest.getD_adjacentCountryIds().size() > 0) {
			for (int i : p_countryOfInterest.getD_adjacentCountryIds()) {
                l_adjCountries.add(getCountry(i));
            }
        } else {
            throw new MapValidationException(p_countryOfInterest.getD_countryName() + " doesn't have any adjacent countries");
		}
		return l_adjCountries;
	}



    /**
     * Iteratively applies the DFS search from the entered node.
     *
     * @param p_countryOfInterest Country visited first
     * @throws MapValidationException Exception
     */
    public void dfsCountry(Country p_countryOfInterest) throws MapValidationException {
        d_allCountriesReachable.put(p_countryOfInterest.getD_countryId(), true);
        for (Country l_nextCountry : getAdjacentCountry(p_countryOfInterest)) {
            if (!d_allCountriesReachable.get(l_nextCountry.getD_countryId())) {
                dfsCountry(l_nextCountry);
            }
        }
    }

    /**
     * Finds the Country object from a given country ID.
	 *
	 * @param p_countryId ID of the country object to be found
     * @return matching country object
     */
    public Country getCountry(Integer p_countryId) {
        return d_allCountries.stream().filter(l_country -> l_country.getD_countryId().equals(p_countryId)).findFirst().orElse(null);
    }

    /**
     * Finds the Country object for the given country Name.
     * 
     * @param p_nameOfCountry Name of the country object to be found
     * @return matching country object
     */
    public Country getCountryByName(String p_nameOfCountry){
        return d_allCountries.stream().filter(l_country -> l_country.getD_countryName().equals(p_nameOfCountry)).findFirst().orElse(null);
    }

    /**
     * Returns Continent Object for given continent Name.
     * 
     * @param p_nameOfContinent Continent Name to be found
     * @return matching continent object
     */
    public Continent getContinent(String p_nameOfContinent){
       return d_allContinents.stream().filter(l_continent -> l_continent.getD_continentName().equals(p_nameOfContinent)).findFirst().orElse(null);
    }

    /**
     * Returns Continent Object for a continent ID.
     * 
     * @param p_continentID Continent Id to be found
     * @return continent object
     */
    public Continent getContinentByID(Integer p_continentID){
        return d_allContinents.stream().filter(l_continent -> l_continent.getD_continentID().equals(p_continentID)).findFirst().orElse(null);
    }

    /**
     * Performs Add Continent operation on Map.
     * 
     * @param p_continentName Name of the Continent to be Added
     * @param p_continentControlValue Control value of the continent to be added
     * @throws MapValidationException to handle Invalid addition
     */
    public void addContinent(String p_continentName, Integer p_continentControlValue) throws MapValidationException{
        int l_continentId;

        if (d_allContinents !=null) {
            l_continentId= d_allContinents.size()>0?Collections.max(getContinentIDs())+1:1;
            if(CommonUtil.isNullObject(getContinent(p_continentName))){
                d_allContinents.add(new Continent(l_continentId, p_continentName, p_continentControlValue));
            }else{
                throw new MapValidationException("Continent "+p_continentName+" cannot be added! It already exists!");
            }
        }else{
            d_allContinents = new ArrayList<Continent>();
            d_allContinents.add(new Continent(1, p_continentName, p_continentControlValue));
        }
    }

    /**
     * Performs the remove continent operation on Map.
     * <ul>
     *     <li> Deletes Specified Continent</li>
     *     <li>Deletes Countries in Continents and their associated data in the Map</li>
     * </ul>
     * @param p_continentUnderConsideration Continent Name to be found
     * @throws MapValidationException Exception
     */
    public void removeContinent(String p_continentUnderConsideration) throws MapValidationException{
        if (d_allContinents !=null) {
            if(!CommonUtil.isNullObject(getContinent(p_continentUnderConsideration))){

                // Deletes the continent and updates neighbour as well as country objects
                if (getContinent(p_continentUnderConsideration).getD_countriesList()!=null) {
                    for(Country c: getContinent(p_continentUnderConsideration).getD_countriesList()){
                        removeCountryNeighboursFromAll(c.getD_countryId());
                        updateNeighboursCont(c.getD_countryId());
                        d_allCountries.remove(c);
                    }
                }
                d_allContinents.remove(getContinent(p_continentUnderConsideration));
            }else{
                throw new MapValidationException("No such Continent exists!");
            }
        } else{
            throw new MapValidationException("No continents in the Map to remove!");
        }
    }

    /**
     * Performs the add country operation on the Map.
     * 
     * @param p_countryName Name of Country to be Added
     * @param p_continentName Name of Continent to be added in
     * @throws MapValidationException Exception
     */
    public void addCountry(String p_countryName, String p_continentName) throws MapValidationException{
        int l_countryId;
        if(d_allCountries ==null){
            d_allCountries = new ArrayList<Country>();
        }
        if(CommonUtil.isNullObject(getCountryByName(p_countryName))){
            l_countryId= d_allCountries.size()>0? Collections.max(getCountryIDs())+1:1;
            if(d_allContinents !=null && getContinent(p_continentName)!=null && getContinentIDs().contains(getContinent(p_continentName).getD_continentID())){
                Country l_country= new Country(l_countryId, p_countryName, getContinent(p_continentName).getD_continentID());
                d_allCountries.add(l_country);
                for (Continent c: d_allContinents) {
                    if (c.getD_continentName().equals(p_continentName)) {
                        c.addingCountry(l_country);
                    }
                }
            } else{
                throw new MapValidationException("Cannot add Country "+p_countryName+" to a Continent that doesn't exist!");
            }
        }else{
            throw new MapValidationException("Country with name "+ p_countryName+" already Exists!");
        }
    }

    /**
     * Performs the remove country operation on Map.
     * 
     * @param p_countryName Name of country to be Added
     * @throws MapValidationException Exception
     */
    public void removeCountry(String p_countryName) throws MapValidationException{
        if(d_allCountries !=null && !CommonUtil.isNullObject(getCountryByName(p_countryName))) {
            for(Continent c: d_allContinents){
                if(c.getD_continentID().equals(getCountryByName(p_countryName).getD_continentId())){
                    c.countryRemove(getCountryByName(p_countryName));
                }
                c.removeCountryNeighboursFromAll(getCountryByName(p_countryName).getD_countryId());
            }
            removeCountryNeighboursFromAll(getCountryByName(p_countryName).getD_countryId());
            d_allCountries.remove(getCountryByName(p_countryName));

        }else{
           throw new MapValidationException("Country: "+ p_countryName+" does not exist!");
        }
    }

    /**
     * Performs the Add Neighbour Operation.
     * 
     * @param p_countryName Country whose neighbours are to be updated
     * @param p_neighbourName Country to be added as neighbour
     * @throws MapValidationException Exception
     */
    public void addCountryNeighbour(String p_countryName, String p_neighbourName) throws MapValidationException{
        if(d_allCountries !=null){
            if(!CommonUtil.isNullObject(getCountryByName(p_countryName)) && !CommonUtil.isNullObject(getCountryByName(p_neighbourName))){
                d_allCountries.get(d_allCountries.indexOf(getCountryByName(p_countryName))).addNeighbour(getCountryByName(p_neighbourName).getD_countryId());
            } else{
                throw new MapValidationException("Invalid Neighbour Pair "+p_countryName+" "+p_neighbourName+"! Either of the Countries Doesn't exist!");
            }
        }
    }

    /**
     * Performs the Remove Neighbor Operation.
     * 
     * @param p_countryName Country whose neighbors are to be updated
     * @param p_neighbourName Country to be removed as neighbor
     * @throws MapValidationException Exception
     */
    public void removeCountryNeighbour(String p_countryName, String p_neighbourName) throws MapValidationException{
        if(d_allCountries !=null){
            if(!CommonUtil.isNullObject(getCountryByName(p_countryName)) && !CommonUtil.isNullObject(getCountryByName(p_neighbourName))) {
                d_allCountries.get(d_allCountries.indexOf(getCountryByName(p_countryName))).removeNeighbour(getCountryByName(p_neighbourName).getD_countryId());
            } else{
                throw new MapValidationException("Invalid Neighbour Pair "+p_countryName+" "+p_neighbourName+"! Either of the Countries Doesn't exist!");
            }
        }
    }

    /**
     * Remove Particular Country as Neighbor from all associated countries (in continent Objects)
     * Used while deletion of a country object.
     * 
     * @param p_countryId Country to be removed
     * @throws MapValidationException indicates Map Object Validation failure
     */
    public void updateNeighboursCont(Integer p_countryId) throws MapValidationException {
        for(Continent c: d_allContinents){
            c.removeCountryNeighboursFromAll(p_countryId);
        }
    }

    /**
     * Remove Particular Country as Neighbor from all associated countries (in Map CountryList)
     * Used while deletion of country object.
     * 
     * @param p_countryID Country to be removed
     * @throws MapValidationException indicates Map Object Validation failure
     */
    public void removeCountryNeighboursFromAll(Integer p_countryID) throws MapValidationException {
        for (Country c: d_allCountries) {
            if (!CommonUtil.isNullObject(c.getD_adjacentCountryIds())) {
                if (c.getD_adjacentCountryIds().contains(p_countryID)) {
                    c.removeNeighbour(p_countryID);
                }
            }
        }
    }
}
