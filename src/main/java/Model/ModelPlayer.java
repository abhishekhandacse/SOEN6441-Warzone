package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Controllers.GamePlayerController;
import Logger.ConsoleLogger;
import Utils.Command;
import Utils.CommonUtil;


public class ModelPlayer {

	ConsoleLogger consoleLogger = new ConsoleLogger();

	private String d_color;
	private String d_name;
	List<Country> d_coutriesOwned;
	List<Continent> d_continentsOwned;
	List<Order> d_ordersToExecute;
	Integer d_noOfUnallocatedArmies;

	
	public ModelPlayer(String p_playerName) {
		this.d_name = p_playerName;
		this.d_noOfUnallocatedArmies = 0;
		this.d_ordersToExecute = new ArrayList<>();
	}

	
	public ModelPlayer() {

	}

	public void setPlayerName(String p_name) {
		this.d_name = p_name;
	}

	public void setD_color(String p_color) {
		d_color = p_color;
	}

	public void setD_noOfUnallocatedArmies(Integer p_numberOfArmies) {
		this.d_noOfUnallocatedArmies = p_numberOfArmies;
	}

	public void setD_coutriesOwned(List<Country> p_coutriesOwned) {
		this.d_coutriesOwned = p_coutriesOwned;
	}

	public void setD_continentsOwned(List<Continent> p_continentsOwned) {
		this.d_continentsOwned = p_continentsOwned;
	}

	public void setD_ordersToExecute(List<Order> p_ordersToExecute) {
		this.d_ordersToExecute = p_ordersToExecute;
	}

	public String getD_color() {
		return d_color;
	}

	public String getPlayerName() {
		return d_name;
	}

	public List<Country> getD_coutriesOwned() {
		return d_coutriesOwned;
	}

	public List<Continent> getD_continentsOwned() {
		return d_continentsOwned;
	}

	public List<Order> getD_ordersToExecute() {
		return d_ordersToExecute;
	}

	public Integer getD_noOfUnallocatedArmies() {
		return d_noOfUnallocatedArmies;
	}

	public List<String> getContinentNames(){
		List<String> l_continentNames = new ArrayList<String>();
		if (d_continentsOwned != null) {
			for(Continent c: d_continentsOwned){
				l_continentNames.add(c.getD_continentName());
			}
			return l_continentNames;
		}
		return null;
	}

	public List<String> getCountryNames(){
		List<String> l_countryNames=new ArrayList<String>();
		for(Country c: d_coutriesOwned){
			l_countryNames.add(c.getD_countryName());
		}
		return l_countryNames;
	}

	
	
}
