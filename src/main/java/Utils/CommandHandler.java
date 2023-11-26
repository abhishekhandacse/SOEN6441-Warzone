package Utils;

import java.io.Serializable;
import java.util.*;
/**
 * A utility class for handling and parsing commands entered by the user.
 * Provides methods to extract the root command, split commands into operations and arguments,
 * and check for the presence of required keys in a command.
 */

public class CommandHandler implements Serializable {

    public String d_command;
    /**
     * Constructs a new CommandHandler object with the specified command string.
     *
     * @param p_command The command string to be processed.
     */

    public CommandHandler(String p_command){
        this.d_command = p_command.trim().replaceAll(" +", " ");
    }
    /**
     * Private helper method to create a map containing operation and arguments based on the given operation string.
     *
     * @param p_operation The operation string to be processed.
     * @return A map containing the operation and its corresponding arguments.
     */
    private Map<String, String> getOperationAndArgumentsMap(String p_operation){
        Map<String, String> l_operationMap = new HashMap<String, String>();
        String[] l_split_operation = p_operation.split(" ");
        String l_arguments = "";
        l_operationMap.put("operation", l_split_operation[0]);

        if(l_split_operation.length > 1){
            String[] l_arguments_values = Arrays.copyOfRange(l_split_operation, 1, l_split_operation.length);
            l_arguments = String.join(" ",l_arguments_values);
        }

        l_operationMap.put("arguments", l_arguments);
        return l_operationMap;
    }

    public String getD_command() {
        return d_command;
    }

    public String getRootCommand(){
        return d_command.split(" ")[0];
    }
    /**
     * Parses the original command string into a list of maps, each representing an operation and its arguments.
     *
     * @return A list of maps containing operations and their corresponding arguments.
     */
    public List<Map<String , String>> getOperationsAndArguments(){
        String l_rootCommand = getRootCommand();
        String l_operationsString =  d_command.replace(l_rootCommand, "").trim();
        
        if(null == l_operationsString || l_operationsString.isEmpty()) {
            return new ArrayList<Map<String , String>>();
        }
        boolean l_isFlagLessCommand = !l_operationsString.contains("-") && !l_operationsString.contains(" ");

        // handle commands to load files, ex: loadmap filename
        if(l_isFlagLessCommand){
            l_operationsString = "-filename "+l_operationsString;
        }

        List<Map<String , String>> l_operations_list  = new ArrayList<Map<String,String>>();
        String[] l_operations = l_operationsString.split("-");

        Arrays.stream(l_operations).forEach((operation) -> {
            if(operation.length() > 1) {
                l_operations_list.add(getOperationAndArgumentsMap(operation));
            }
        });

        return l_operations_list;
    }
    /**
     * Checks whether the required key is present in the provided map and has a non-empty value.
     *
     * @param p_key      The key to be checked for presence.
     * @param p_inputMap The map in which the presence of the key is checked.
     * @return True if the key is present and has a non-empty value, otherwise false.
     */
    public boolean checkRequiredKeysPresent(String p_key, Map<String, String> p_inputMap) {
        if(p_inputMap.containsKey(p_key) && null != p_inputMap.get(p_key)
				&& !p_inputMap.get(p_key).isEmpty())
            return true;
        return false;
    }
}
