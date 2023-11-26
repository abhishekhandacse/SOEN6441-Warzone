package Models;

import Exceptions.MapValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * The Country class represents a country in the game.
 * It implements Serializable to support serialization.
 */
public class Country {

    /**
     * The number of armies in the country.
     */
    Integer d_armies;

    /**
     * The ID of the country.
     */
    Integer d_countryId;

    /**
     * The ID of the continent to which the country belongs.
     */
    Integer d_continentId;

    /**
     * The name of the country.
     */
    String d_countryName;

    /**
     * The list of IDs of adjacent countries.
     */
    List<Integer> d_adjacentCountryIds = new ArrayList<>();

    /**
     * Constructor for the Country class with full information.
     *
     * @param p_countryId    The ID of the country
     * @param p_countryName  The name of the country
     * @param p_continentId  The ID of the continent to which the country belongs
     */
	public Country(int p_countryId, String p_countryName, int p_continentId) {
		d_countryId = p_countryId;
		d_countryName = p_countryName;
		d_continentId = p_continentId;
		d_adjacentCountryIds = new ArrayList<>();
		d_armies = 0;
	}
    /**
     * Constructor for the Country class with limited information.
     *
     * @param p_countryId    The ID of the country
     * @param p_continentId  The ID of the continent to which the country belongs
     */
	public Country(int p_countryId, int p_continentId) {
		d_countryId = p_countryId;
		d_continentId = p_continentId;
	}
    /**
     * Constructor for the Country class with only the country name.
     *
     * @param p_countryName  The name of the country
     */
	public Country(String p_countryName) {
		d_countryName = p_countryName;
	}

	public Integer getD_armies() {
		return d_armies;
	}

	public Integer getD_countryId() {
		return d_countryId;
	}

	public Integer getD_continentId() {
		return d_continentId;
	}

	public String getD_countryName() {
		return d_countryName;
	}

	public List<Integer> getD_adjacentCountryIds() {
		if(d_adjacentCountryIds==null){
			d_adjacentCountryIds=new ArrayList<Integer>();
		}
		return d_adjacentCountryIds;
	}

	public void setD_countryName(String p_countryName) {
		this.d_countryName = p_countryName;
	}

	public void setD_armies(Integer p_armies) {
		this.d_armies = p_armies;
	}

	public void setD_countryId(Integer p_countryId) {
		this.d_countryId = p_countryId;
	}

	public void setD_continentId(Integer p_continentId) {
		this.d_continentId = p_continentId;
	}

	public void setD_adjacentCountryIds(List<Integer> p_adjacentCountryIds) {
		this.d_adjacentCountryIds = p_adjacentCountryIds;
	}
    /**
     * Adds a neighboring country to the list of adjacent countries.
     *
     * @param p_countryId The ID of the neighboring country to be added
     */
	public void addNeighbours(Integer p_countryId){
		if(!d_adjacentCountryIds.contains(p_countryId))
			d_adjacentCountryIds.add(p_countryId);
	}
    /**
     * Removes a neighboring country from the list of adjacent countries.
     *
     * @param p_countryId The ID of the neighboring country to be removed
     * @throws MapValidationException  If the specified neighboring country does not exist
     */
	public void removeNeighbours(Integer p_countryId) throws MapValidationException {
		if(d_adjacentCountryIds.contains(p_countryId)){
			d_adjacentCountryIds.remove(d_adjacentCountryIds.indexOf(p_countryId));
		}else{
			throw new MapValidationException("No Such Neighbour Exists");
		}
	}
}
