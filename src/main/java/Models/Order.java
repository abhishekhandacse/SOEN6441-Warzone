package Models;

import Logger.ConsoleLogger;

public class Order {

    ConsoleLogger consoleLogger = new ConsoleLogger();
    
    String d_orderAction;
	String d_targetCountryName;
	String d_sourceCountryName;
	Integer d_numberOfArmiesToPlace;
	Order orderObj;

	
	public Order() {
	}

	public Order(String p_orderAction, String p_targetCountryName, Integer p_numberOfArmiesToPlace) {
		this.d_orderAction = p_orderAction;
		this.d_targetCountryName = p_targetCountryName;
		this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
	}

    public void setD_orderAction(String p_orderAction) {
		this.d_orderAction = p_orderAction;
	}

	public void setD_targetCountryName(String p_targetCountryName) {
		this.d_targetCountryName = p_targetCountryName;
	}

	public void setD_sourceCountryName(String p_sourceCountryName) {
		this.d_sourceCountryName = p_sourceCountryName;
	}

	public void setD_numberOfArmiesToPlace(Integer p_numberOfArmiesToPlace) {
		this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
	}

    public String getD_targetCountryName() {
		return d_targetCountryName;
	}

	public String getD_orderAction() {
		return d_orderAction;
	}

	public String getD_sourceCountryName() {
		return d_sourceCountryName;
	}

	public Integer getD_numberOfArmiesToPlace() {
		return d_numberOfArmiesToPlace;
	}
}
