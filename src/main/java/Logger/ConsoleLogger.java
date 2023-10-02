package Logger;

public class ConsoleLogger implements Logger {
    @Override
    public void writeLog(String p_message) {
        System.out.println(p_message);
    }
}