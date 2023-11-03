package Models;

public class Bomb implements Card{
    Player d_player;

	String d_targetCountryID;

	String d_logOrderExecution;

	//Constructor
	public Bomb(Player p_player, String p_targetCountry) {
		this.d_player = p_player;
		this.d_targetCountryID = p_targetCountry;
	}

    @Override
    public void execute(State p_state) {
       
    }

    @Override
    public boolean valid(State p_state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'valid'");
    }

    @Override
    public void printOrder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'printOrder'");
    }

    @Override
    public String orderExecutionLog() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orderExecutionLog'");
    }

    @Override
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setD_orderExecutionLog'");
    }

    @Override
    public String getOrderName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrderName'");
    }

    
}
