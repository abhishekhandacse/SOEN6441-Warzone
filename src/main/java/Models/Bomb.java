package Models;

public class Bomb {
    Player d_player;

	String d_targetCountryID;

	String d_logOrderExecution;

	//Constructor
	public Bomb(Player p_player, String p_targetCountry) {
		this.d_player = p_player;
		this.d_targetCountryID = p_targetCountry;
	}
}
