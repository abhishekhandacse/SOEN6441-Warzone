package Utils;

import java.util.*;


/**
 * The type Command handler.
 */
public class CommandHandler {


    /**
     * The D command. This variable is used to store the command string.
     */
    public String d_command;


    /**
     * Instantiates a new Command handler. This is a contructor for commandhandler class
     *
     * @param p_command the p command
     */
    public CommandHandler(String p_command) {
        this.d_command = p_command.trim().replaceAll(" +", " ");
    }


    /**
     * Getter method to get the root command string.
     *
     * @return the string
     */
    public String getRootCommand() {
        return d_command.split(" ")[0];
    }


    /**
     * Get operations and arguments list. This method returns the list of arguments to check if the command is available in the list of arguments
     *
     *
     * @return the list of operations that are valid and can be performed
     */
    public List<Map<String, String>> getOperationsAndArguments() {
        String l_rootCommand = getRootCommand();
        String l_operationsString = d_command.replace(l_rootCommand, "").trim();

        if (null == l_operationsString || l_operationsString.isEmpty()) {
            return new ArrayList<Map<String, String>>();
        }
        boolean l_isFlagLessCommand = !l_operationsString.contains("-") && !l_operationsString.contains(" ");

        // handle commands to load files, ex: loadmap filename
        if (l_isFlagLessCommand) {
            l_operationsString = "-filename " + l_operationsString;
        }

        List<Map<String, String>> l_operations_list = new ArrayList<Map<String, String>>();
        String[] l_operations = l_operationsString.split("-");

        Arrays.stream(l_operations).forEach((operation) -> {
            if (operation.length() > 1) {
                l_operations_list.add(getOperationAndArgumentsMap(operation));
            }
        });

        return l_operations_list;
    }

    /**
     * Private function to get the  list of operations that can be used on the map.
     *
     * @param p_operation
     * @return the valid list of operations.
     */

    private Map<String, String> getOperationAndArgumentsMap(String p_operation) {
        Map<String, String> l_operationMap = new HashMap<String, String>();

        String[] l_split_operation = p_operation.split(" ");
        String l_arguments = "";

        l_operationMap.put("operation", l_split_operation[0]);

        if (l_split_operation.length > 1) {
            String[] l_arguments_values = Arrays.copyOfRange(l_split_operation, 1, l_split_operation.length);
            l_arguments = String.join(" ", l_arguments_values);
        }

        l_operationMap.put("arguments", l_arguments);

        return l_operationMap;
    }


    /**
     * Check required keys present boolean.
     *
     * @param p_key      the p key
     * @param p_inputMap the p input map
     * @return the boolean
     */
    public boolean checkRequiredKeysPresent(String p_key, Map<String, String> p_inputMap) {
        if (p_inputMap.containsKey(p_key) && null != p_inputMap.get(p_key)
                && !p_inputMap.get(p_key).isEmpty())
            return true;
        return false;
    }
}
