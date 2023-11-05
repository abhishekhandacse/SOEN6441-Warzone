package Models;

import Logger.ConsoleLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Country.
 */
public class ModelCountry {

    /**
     * The Console logger.
     */
    ConsoleLogger d_consoleLogger = new ConsoleLogger();
    /**
     * The D armies is used to represent the number of armies a country currently has.
     */
    Integer d_armies;
    /**
     * The D country id represents the country id of the country .
     */
    Integer d_countryId;
    /**
     * The D continent id represents the continent id of the continent a particular country lies in.
     * It acts as a foreign key for continent model.
     */
    Integer d_continentId;
    /**
     * The D country name represents the name of the country
     */
    String d_countryName;
    /**
     * The D adjacent country ids. represent the id of all other countries adjacent to it.
     */
    List<Integer> d_adjacentCountryIds = new ArrayList<Integer>();

    /**
     * Instantiates a new Country.
     *
     * @param p_countryId   the p country id is the id of country
     * @param p_countryName the p country name is the name of the country
     * @param p_continentId the p continent id is the id of the continent a particular country lies in.
     */
    public ModelCountry(int p_countryId, String p_countryName, int p_continentId) {
        d_countryId = p_countryId;
        d_countryName = p_countryName;
        d_continentId = p_continentId;
    }


    /**
     * Instantiates a new Country.
     *
     * @param p_countryName the p country name is a variable to store country name.
     */
    public ModelCountry(String p_countryName) {
        this.d_countryName = p_countryName;
    }

    /**
     * Getter method to get the country id.
     *
     * @return the d country id
     */
    public Integer getD_countryId() {
        return d_countryId;
    }

    /**
     * Setter method to set  d country id. of a country.
     *
     * @param p_countryId the p country id
     */
    public void setD_countryId(Integer p_countryId) {
        this.d_countryId = p_countryId;
    }

    /**
     * Getter method to get the armies of a country.
     *
     * @return the d armies
     */
    public Integer getD_armies() {
        return d_armies;
    }

    /**
     * Setter method  to set  d armies. in a country
     *
     * @param p_armies the p armies
     */
    public void setD_armies(Integer p_armies) {
        this.d_armies = p_armies;
    }

    /**
     * Getter method to get the continent id of the country
     *
     * @return the d continent id
     */
    public Integer getD_continentId() {
        return d_continentId;
    }

    /**
     * Setter method to set  d continent id of a country
     *
     * @param p_continentId the p continent id
     */
    public void setD_continentId(Integer p_continentId) {
        this.d_continentId = p_continentId;
    }

    /**
     * Getter method to get the country name.
     *
     * @return the d country name
     */
    public String getD_countryName() {
        return d_countryName;
    }

    /**
     * Setter  method to set the country name of a country.
     *
     * @param p_countryName the p country name
     */
    public void setD_countryName(String p_countryName) {
        this.d_countryName = p_countryName;
    }

    /**
     * Getter method to get the adjacent country ids of the country
     *
     * @return the d adjacent country ids
     */
    public List<Integer> getD_adjacentCountryIds() {
        if (d_adjacentCountryIds == null) {
            d_adjacentCountryIds = new ArrayList<Integer>();
        }
        return d_adjacentCountryIds;
    }

    /**
     * Setter method to set all adjacent country ids. to country.
     *
     * @param p_adjacentCountryIds the p adjacent country ids
     */
    public void setD_adjacentCountryIds(List<Integer> p_adjacentCountryIds) {
        this.d_adjacentCountryIds = p_adjacentCountryIds;
    }

    /**
     * Remove method to remove any  neighbour of the country by providing the country id of the neighbour to remove.
     *
     * @param p_countryId the p country id
     */
    public void removeNeighbours(Integer p_countryId) {
        if (d_adjacentCountryIds.contains(p_countryId)) {
            d_adjacentCountryIds.remove(p_countryId);
        } else {
            d_consoleLogger.writeLog("No Such Neighbour Exists");
        }
    }

    /**
     * Add method to add the neighbour of the country by providing the country id of the neighbor to add.
     *
     * @param p_countryId the p country id
     */
    public void addNeighbours(Integer p_countryId) {
        if (!d_adjacentCountryIds.contains(p_countryId)) d_adjacentCountryIds.add(p_countryId);
    }
}
