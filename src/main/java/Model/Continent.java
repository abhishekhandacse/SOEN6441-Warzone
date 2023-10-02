package Model;

import java.util.List;

public class Continent {
    
    Integer d_continentValue;
	List<Country> d_countries;
	Integer d_continentID;
	String d_continentName;

	public Continent(){
	}

	public Continent(Integer p_continentID, String p_continentName, int p_continentValue) {
		this.d_continentID=p_continentID;
		this.d_continentName=p_continentName;
		this.d_continentValue=p_continentValue;
	}
}
