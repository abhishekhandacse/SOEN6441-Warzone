package Models;

import java.util.ArrayList;
import java.util.List;

import Utils.CommonUtil;

/**
 * The type Continent.
 */
public class Continent {

    /**
     * The D continent value.
     */
    Integer d_continentValue;
    /**
     * The D countries.
     */
    List<Country> d_countries;
    /**
     * The D continent id.
     */
    Integer d_continentID;
    /**
     * The D continent name.
     */
    String d_continentName;

    /**
     * Instantiates a new Continent.
     */
    public Continent() {
    }

    /**
     * Instantiates a new Continent.
     *
     * @param p_continentID    the p continent id
     * @param p_continentName  the p continent name
     * @param p_continentValue the p continent value
     */
    public Continent(Integer p_continentID, String p_continentName, int p_continentValue) {
        this.d_continentID = p_continentID;
        this.d_continentName = p_continentName;
        this.d_continentValue = p_continentValue;
    }

    /**
     * Instantiates a new Continent.
     *
     * @param p_continentName the p continent name
     */
    public Continent(String p_continentName) {
        this.d_continentName = p_continentName;
    }

    /**
     * Gets d continent id.
     *
     * @return the d continent id
     */
    public Integer getD_continentID() {
        return d_continentID;
    }

    /**
     * Sets d continent id.
     *
     * @param p_continentID the p continent id
     */
    public void setD_continentID(Integer p_continentID) {
        this.d_continentID = p_continentID;
    }

    /**
     * Gets d continent name.
     *
     * @return the d continent name
     */
    public String getD_continentName() {
        return d_continentName;
    }

    /**
     * Sets d continent name.
     *
     * @param p_continentName the p continent name
     */
    public void setD_continentName(String p_continentName) {
        this.d_continentName = p_continentName;
    }

    /**
     * Gets d continent value.
     *
     * @return the d continent value
     */
    public Integer getD_continentValue() {
        return d_continentValue;
    }

    /**
     * Sets d continent value.
     *
     * @param p_continentValue the p continent value
     */
    public void setD_continentValue(Integer p_continentValue) {
        this.d_continentValue = p_continentValue;
    }

    /**
     * Gets d countries.
     *
     * @return the d countries
     */
    public List<Country> getD_countries() {
        return d_countries;
    }

    /**
     * Sets d countries.
     *
     * @param p_countries the p countries
     */
    public void setD_countries(List<Country> p_countries) {
        this.d_countries = p_countries;
    }

    /**
     * Remove all country neighbours.
     *
     * @param p_countryId the p country id
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
     * Remove country.
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
     * Add country.
     *
     * @param p_country the p country
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
