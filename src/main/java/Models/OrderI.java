package Models;

public interface OrderI {
    public void execute(GameState p_gameState);
    public boolean valid(GameState p_gameState);
    public void printOrder();
    public String orderExecutionLog();
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType);
    public String getOrderName();
}
