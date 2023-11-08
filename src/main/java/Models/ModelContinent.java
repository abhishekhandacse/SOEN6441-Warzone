package Models;

import Exceptions.MapValidationException;
import Utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class ModelContinent {

	Integer d_continentID;
	String d_continentName;
	Integer d_continentValue;
	List<ModelCountry> d_countries;

	public ModelContinent(){
	}

	public ModelContinent(Integer p_continentID, String p_continentName, int p_continentValue) {
		this.d_continentID=p_continentID;
		this.d_continentName=p_continentName;
		this.d_continentValue=p_continentValue;
	}

	public ModelContinent(String p_continentName) {
		this.d_continentName = p_continentName;
	}

	public void setD_countries(List<ModelCountry> p_countries) {
		this.d_countries = p_countries;
	}

	public void setD_continentValue(Integer p_continentValue) {
		this.d_continentValue = p_continentValue;
	}

	public void setD_continentID(Integer p_continentID) {
		this.d_continentID = p_continentID;
	}

	public void setD_continentName(String p_continentName) {
		this.d_continentName = p_continentName;
	}

	public Integer getD_continentValue() {
		return d_continentValue;
	}

	public Integer getD_continentID() {
		return d_continentID;
	}

	public String getD_continentName() {
		return d_continentName;
	}

	public List<ModelCountry> getD_countries() {
		return d_countries;
	}

	public void addingCountry(ModelCountry p_country){
		if (d_countries!=null){
			d_countries.add(p_country);
		}
		else{
			d_countries=new ArrayList<ModelCountry>();
			d_countries.add(p_country);
		}
	}

	public void countryRemove(ModelCountry p_country) throws MapValidationException{
		if(d_countries==null){
			System.out.println("No such Country Exists");
		}else {
			d_countries.remove(p_country);
		}
	}

	public void removeCountryNeighboursFromAll(Integer p_countryId) throws MapValidationException {
		if (null!=d_countries && !d_countries.isEmpty()) {
			for (ModelCountry c: d_countries){
				if (!CommonUtil.isNullObject(c.d_adjacentCountryIds)) {
					if (c.getD_adjacentCountryIds().contains(p_countryId)){
						c.removeNeighbour(p_countryId);
					}
				}
			}
		}
	}
}
