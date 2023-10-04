package Models;

import Utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Model Class for Continent
 *
 * @author Anurag Teckchandani
 */
public class Continent {

    /**
     * d_continentValue: Signifies the value of continent ie the bonus value a player gets after conquering all countries in a continent.
     */
    Integer d_continentValue;
    /**
     * d_countries: List of countries.
     */
    List<Country> d_countries;
    /**
     * d_continentID: Unique continent ID
     */
    Integer d_continentID;
    /**
     * d_continentName: Continent Name
     */
    String d_continentName;

    /**
     * Instantiates a new Continent.
     */
    public Continent() {
    }

    /**
     * Parameterized Constructor
     *
     * @param p_continentID    the continent id
     * @param p_continentName  the continent name
     * @param p_continentValue the continent value
     */
    public Continent(Integer p_continentID, String p_continentName, int p_continentValue) {
        this.d_continentID = p_continentID;
        this.d_continentName = p_continentName;
        this.d_continentValue = p_continentValue;
    }

    /**
     * Parameterized Constructor
     *
     * @param p_continentName the continent name
     */
    public Continent(String p_continentName) {
        this.d_continentName = p_continentName;
    }

    /**
     * Gets continent id.
     *
     * @return the continent id
     */
    public Integer getD_continentID() {
        return d_continentID;
    }

    /**
     * Sets continent id.
     *
     * @param p_continentID the continent id
     */
    public void setD_continentID(Integer p_continentID) {
        this.d_continentID = p_continentID;
    }

    /**
     * Gets continent name.
     *
     * @return the continent name
     */
    public String getD_continentName() {
        return d_continentName;
    }

    /**
     * Sets continent name.
     *
     * @param p_continentName the continent name
     */
    public void setD_continentName(String p_continentName) {
        this.d_continentName = p_continentName;
    }

    /**
     * Gets continent value.
     *
     * @return the continent value
     */
    public Integer getD_continentValue() {
        return d_continentValue;
    }

    /**
     * Sets continent value.
     *
     * @param p_continentValue the continent value
     */
    public void setD_continentValue(Integer p_continentValue) {
        this.d_continentValue = p_continentValue;
    }

    /**
     * Gets countries.
     *
     * @return the countries
     */
    public List<Country> getD_countries() {
        return d_countries;
    }

    /**
     * Sets countries.
     *
     * @param p_countries the countries
     */
    public void setD_countries(List<Country> p_countries) {
        this.d_countries = p_countries;
    }

    /**
     * Remove all country neighbours.
     *
     * @param p_countryId the country id
     */
    public void removeAllCountryNeighbours(Integer p_countryId) {
        if (null != d_countries && !d_countries.isEmpty()) {
            for (Country c : d_countries) {
                if (!CommonUtil.isNull(c.d_adjacentCountryIds)) {
                    if (c.getD_adjacentCountryIds().contains(p_countryId)) {
                        c.removeNeighbours(p_countryId);
                    }
                }
            }
        }
    }

    /**
     * Remove country from map.
     *
     * @param p_country the p country
     */
    public void removeCountry(Country p_country) {
        if (d_countries == null) {
            System.out.println("No such Country Exists");
        } else {
            d_countries.remove(p_country);
        }
    }

    /**
     * Add country to the map.
     *
     * @param p_country the country
     */
    public void addCountry(Country p_country) {
        if (d_countries != null) {
            d_countries.add(p_country);
        } else {
            d_countries = new ArrayList<Country>();
            d_countries.add(p_country);
        }
    }

}
