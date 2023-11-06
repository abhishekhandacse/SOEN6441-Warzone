package Models;

import Exceptions.InvalidMap;
import java.util.ArrayList;
import java.util.List;

/**
 * The `Country` class represents a country in a game map.
 */
public class ModelCountry {
    /**
     * The name of the country.
     */
    String d_countryName;

    /**
     * The number of armies in the country.
     */
    Integer d_armies;

    /**
     * The unique identifier for the country.
     */
    Integer d_countryId;

    /**
     * The identifier of the continent to which the country belongs.
     */
    Integer d_continentId;

    /**
     * The list of unique identifiers of adjacent countries.
     */
    List<Integer> d_adjacentCountryIds = new ArrayList<Integer>();

    /**
     * Constructs a `Country` with a specific country identifier, name, and continent identifier.
     *
     * @param p_countryId   The unique identifier for the country.
     * @param p_countryName The name of the country.
     * @param p_continentId The identifier of the continent to which the country belongs.
     */
    public ModelCountry(int p_countryId, String p_countryName, int p_continentId) {
        d_countryId = p_countryId;
        d_countryName = p_countryName;
        d_continentId = p_continentId;
        d_armies = 0;
    }

    /**
     * Constructs a `Country` with a specific country identifier and continent identifier.
     *
     * @param p_countryId   The unique identifier for the country.
     * @param p_continentId The identifier of the continent to which the country belongs.
     */
    public ModelCountry(int p_countryId, int p_continentId) {
        d_countryId = p_countryId;
        d_continentId = p_continentId;
    }

    /**
     * Constructs a `Country` with a specific country name.
     *
     * @param p_countryName The name of the country.
     */
    public ModelCountry(String p_countryName) {
        d_countryName = p_countryName;
    }

    /**
     * Adds a neighboring country to the list of adjacent countries.
     *
     * @param p_countryId The unique identifier of the neighboring country.
     */
    public void addNeighbour(Integer p_countryId) {
        if (!d_adjacentCountryIds.contains(p_countryId))
            d_adjacentCountryIds.add(p_countryId);
    }

    /**
     * Retrieves the number of armies in the country.
     *
     * @return The number of armies in the country.
     */
    public Integer getD_armies() {
        return d_armies;
    }

    /**
     * Retrieves the unique identifier for the country.
     *
     * @return The unique identifier for the country.
     */
    public Integer getD_countryId() {
        return d_countryId;
    }

    /**
     * Retrieves the identifier of the continent to which the country belongs.
     *
     * @return The identifier of the continent.
     */
    public Integer getD_continentId() {
        return d_continentId;
    }

    /**
     * Retrieves the list of unique identifiers of adjacent countries.
     *
     * @return The list of adjacent country identifiers.
     */
    public List<Integer> getD_adjacentCountryIds() {
        if (d_adjacentCountryIds == null) {
            d_adjacentCountryIds = new ArrayList<Integer>();
        }
        return d_adjacentCountryIds;
    }

    /**
     * Retrieves the name of the country.
     *
     * @return The name of the country.
     */
    public String getD_countryName() {
        return d_countryName;
    }

    /**
     * Sets the number of armies in the country.
     *
     * @param p_armies The number of armies to set.
     */
    public void setD_armies(Integer p_armies) {
        this.d_armies = p_armies;
    }

    /**
     * Sets the unique identifier for the country.
     *
     * @param p_countryId The unique identifier to set.
     */
    public void setD_countryId(Integer p_countryId) {
        this.d_countryId = p_countryId;
    }

    /**
     * Sets the identifier of the continent to which the country belongs.
     *
     * @param p_continentId The continent identifier to set.
     */
    public void setD_continentId(Integer p_continentId) {
        this.d_continentId = p_continentId;
    }

    /**
     * Sets the list of unique identifiers of adjacent countries.
     *
     * @param p_adjacentCountryIds The list of adjacent country identifiers to set.
     */
    public void setD_adjacentCountryIds(List<Integer> p_adjacentCountryIds) {
        this.d_adjacentCountryIds = p_adjacentCountryIds;
    }

    /**
     * Sets the name of the country.
     *
     * @param p_countryName The name of the country to set.
     */
    public void setD_countryName(String p_countryName) {
        this.d_countryName = p_countryName;
    }

    /**
     * Removes a neighboring country from the list of adjacent countries.
     *
     * @param p_countryId The unique identifier of the neighboring country to remove.
     * @throws InvalidMap If the specified neighboring country is not found in the list.
     */
    public void removeNeighbour(Integer p_countryId) throws InvalidMap {
        if (d_adjacentCountryIds.contains(p_countryId)) {
            d_adjacentCountryIds.remove(d_adjacentCountryIds.indexOf(p_countryId));
        } else {
            throw new InvalidMap("No Such Neighbour Exists");
        }
    }
}
