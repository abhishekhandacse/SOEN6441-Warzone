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
}
