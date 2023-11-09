package Utils;

import java.util.*;

/**
 * The CommandHandler class is responsible for handling and parsing commands used in the game.
 */

public class CommandHandler {

    /**
     * the command
     */
    public String d_command;

    /**
     * Retrieves the operation and its associated arguments from the provided operation string.
     *
     * @param p_operation The operation string to extract operation and arguments
     * @return A map containing the extracted operation and its associated arguments
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

    /**
     * Gets the root command from the provided command.
     *
     * @return The root command
     */
    public String getRootCommand(){
        return d_command.split(" ")[0];
    }

    /**
     * Retrieves the original command string.
     *
     * @return The original command string
     */
    public String getD_command() {
        return d_command;
    }

    /**
     * Constructor for the CommandHandler class.
     *
     * @param p_command The command string to handle
     */
    public CommandHandler(String p_command){
        this.d_command = p_command.trim().replaceAll(" +", " ");
    }

    /**
     * Gets a list of operations and their associated arguments from the command.
     *
     * @return A list of maps containing operations and their arguments
     */
    public List<Map<String , String>> getOperationsAndArguments(){
        String l_rootCommand = getRootCommand();
        String l_operationsString =  d_command.replace(l_rootCommand, "").trim();
        if(null == l_operationsString || l_operationsString.isEmpty()) {
            return new ArrayList<Map<String , String>>();
        }
        boolean l_isFlagLessCommand = !l_operationsString.contains("-") && !l_operationsString.contains(" ");

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
     * Checks if the required keys are present in the input map.
     *
     * @param p_key      The key to check for in the input map
     * @param p_inputMap The input map to check against
     * @return True if the required key is present and not empty, false otherwise
     */
    public boolean checkRequiredKeysPresent(String p_key, Map<String, String> p_inputMap) {
        if(p_inputMap.containsKey(p_key) && null != p_inputMap.get(p_key)
				&& !p_inputMap.get(p_key).isEmpty())
        return true;
    return false;
    }

}
