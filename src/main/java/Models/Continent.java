package Models;

import Exceptions.MapValidationException;
import Utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This model class manages all the Continents in the map.
 */
public class Continent {

	/**
	 * continent name.
	 */
	String d_continentName;

	/**
	 * continent ID.
	 */
	Integer d_continentID;

	/**
	 * continent value.
	 */
	Integer d_continentValue;

	/**
	 * List of countries.
	 */
	List<Country> d_countriesList;

	/**
	 * Default constructor for ModelContinent.
	 */
	public Continent() {
	}

	/**
	 * Constructor for ModelContinent with specified attributes.
	 *
	 * @param p_continentID The continent's unique ID.
	 * @param p_continentName The name of the continent.
	 * @param p_continentValue The reinforcement value of the continent.
	 */
	public Continent(Integer p_continentID, String p_continentName, int p_continentValue) {
		this.d_continentID = p_continentID;
		this.d_continentName = p_continentName;
		this.d_continentValue = p_continentValue;
	}

	/**
	 * Constructor for ModelContinent with a specified continent name.
	 *
	 * @param p_continentName The name of the continent.
	 */
	public Continent(String p_continentName) {
		this.d_continentName = p_continentName;
	}

	/**
	 * Set the list of countries belonging to this continent.
	 *
	 * @param p_countries The list of countries in the continent.
	 */
	public void setD_countriesList(List<Country> p_countries) {
		this.d_countriesList = p_countries;
	}

	/**
	 * Set the reinforcement value of the continent.
	 *
	 * @param p_continentValue The reinforcement value of the continent.
	 */
	public void setD_continentValue(Integer p_continentValue) {
		this.d_continentValue = p_continentValue;
	}

	/**
	 * Set the unique ID of the continent.
	 *
	 * @param p_continentID The continent's unique ID.
	 */
	public void setD_continentID(Integer p_continentID) {
		this.d_continentID = p_continentID;
	}

	/**
	 * Set the name of the continent.
	 *
	 * @param p_continentName The name of the continent.
	 */
	public void setD_continentName(String p_continentName) {
		this.d_continentName = p_continentName;
	}

	/**
	 * Get the reinforcement value of the continent.
	 *
	 * @return The reinforcement value of the continent.
	 */
	public Integer getD_continentValue() {
		return d_continentValue;
	}

	/**
	 * Get the unique ID of the continent.
	 *
	 * @return The continent's unique ID.
	 */
	public Integer getD_continentID() {
		return d_continentID;
	}

	/**
	 * Get the name of the continent.
	 *
	 * @return The name of the continent.
	 */
	public String getD_continentName() {
		return d_continentName;
	}

	/**
	 * Get the list of countries in the continent.
	 *
	 * @return The list of countries in the continent.
	 */
	public List<Country> getD_countriesList() {
		return d_countriesList;
	}

	/**
	 * Add a country to the list of countries in the continent.
	 *
	 * @param p_country The country to be added.
	 */
	public void addingCountry(Country p_country) {
		if (d_countriesList != null) {
			d_countriesList.add(p_country);
		} else {
			d_countriesList = new ArrayList<Country>();
			d_countriesList.add(p_country);
		}
	}

	/**
	 * Remove a country from the list of countries in the continent.
	 *
	 * @param p_country The country to be removed.
	 * @throws MapValidationException If the country does not exist.
	 */
	public void countryRemove(Country p_country) throws MapValidationException {
		if (d_countriesList == null) {
			System.out.println("No such Country Exists");
		} else {
			d_countriesList.remove(p_country);
		}
	}

	/**
	 * Remove the given country's neighbors from all countries in the continent.
	 *
	 * @param p_countryId The ID of the country to be removed from neighbors.
	 * @throws MapValidationException If the country does not exist.
	 */
	public void removeCountryNeighboursFromAll(Integer p_countryId) throws MapValidationException {
		if (null != d_countriesList && !d_countriesList.isEmpty()) {
			for (Country c : d_countriesList) {
				if (!CommonUtil.isNull(c.d_adjacentCountryIds)) {
					if (c.getD_adjacentCountryIds().contains(p_countryId)) {
						c.removeNeighbour(p_countryId);
					}
				}
			}
		}
	}
}
