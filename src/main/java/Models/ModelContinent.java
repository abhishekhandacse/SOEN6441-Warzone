package Models;

import Exceptions.MapValidationException;
import Utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This model class manages all the Continents in the map.
 */
public class ModelContinent {

	Integer d_continentID;
	String d_continentName;
	Integer d_continentValue;
	List<ModelCountry> d_countries;

	/**
	 * Default constructor for ModelContinent.
	 */
	public ModelContinent() {
	}

	/**
	 * Constructor for ModelContinent with specified attributes.
	 *
	 * @param p_continentID The continent's unique ID.
	 * @param p_continentName The name of the continent.
	 * @param p_continentValue The reinforcement value of the continent.
	 */
	public ModelContinent(Integer p_continentID, String p_continentName, int p_continentValue) {
		this.d_continentID = p_continentID;
		this.d_continentName = p_continentName;
		this.d_continentValue = p_continentValue;
	}

	/**
	 * Constructor for ModelContinent with a specified continent name.
	 *
	 * @param p_continentName The name of the continent.
	 */
	public ModelContinent(String p_continentName) {
		this.d_continentName = p_continentName;
	}

	/**
	 * Set the list of countries belonging to this continent.
	 *
	 * @param p_countries The list of countries in the continent.
	 */
	public void setD_countries(List<ModelCountry> p_countries) {
		this.d_countries = p_countries;
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
	public List<ModelCountry> getD_countries() {
		return d_countries;
	}

	/**
	 * Add a country to the list of countries in the continent.
	 *
	 * @param p_country The country to be added.
	 */
	public void addingCountry(ModelCountry p_country) {
		if (d_countries != null) {
			d_countries.add(p_country);
		} else {
			d_countries = new ArrayList<ModelCountry>();
			d_countries.add(p_country);
		}
	}

	/**
	 * Remove a country from the list of countries in the continent.
	 *
	 * @param p_country The country to be removed.
	 * @throws MapValidationException If the country does not exist.
	 */
	public void countryRemove(ModelCountry p_country) throws MapValidationException {
		if (d_countries == null) {
			System.out.println("No such Country Exists");
		} else {
			d_countries.remove(p_country);
		}
	}

	/**
	 * Remove the given country's neighbors from all countries in the continent.
	 *
	 * @param p_countryId The ID of the country to be removed from neighbors.
	 * @throws MapValidationException If the country does not exist.
	 */
	public void removeCountryNeighboursFromAll(Integer p_countryId) throws MapValidationException {
		if (null != d_countries && !d_countries.isEmpty()) {
			for (ModelCountry c : d_countries) {
				if (!CommonUtil.isNullObject(c.d_adjacentCountryIds)) {
					if (c.getD_adjacentCountryIds().contains(p_countryId)) {
						c.removeNeighbour(p_countryId);
					}
				}
			}
		}
	}
}
