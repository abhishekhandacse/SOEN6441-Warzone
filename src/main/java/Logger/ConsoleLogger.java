package Logger;

/**
 * The type Console logger.
 * 
 * @author Anurag Teckchandani
 */
public class ConsoleLogger implements Logger {
    /**
     * 
     */
    @Override
    public void writeLog(String p_message) {
        System.out.println(p_message);
    }
}