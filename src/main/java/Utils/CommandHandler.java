package Utils;
import java.util.*;

/**
 * CommandHandler class maps the commands inputted by players with appropriate actions.
 */
public class CommandHandler {

    /**
     * d_terminalCommand stores the command entered by the player.
     */
    private String d_terminalCommand;

    /**
     *  Constructer: Trims the input command and Initailizes the value of data member d_terminalCommand
     * 
     * @param p_command Command entered by the player.
     */
    public CommandHandler(final String p_command){
        final String l_trimLeadingAndTrailingSpaces = p_command.trim();
        final String l_replaceAllMultipleWhiteSpacesWithSingleSpace = l_trimLeadingAndTrailingSpaces.replaceAll(" +", " ");
        this.d_terminalCommand = l_replaceAllMultipleWhiteSpacesWithSingleSpace;
    }

    /**
     * Getter Method
     * 
     * @return Root Command entered by the user
     */
    public String fetchRootCommand(){
        return d_terminalCommand.split(" ")[0];
    }


    public List<Map<String, String>> fetchAttemptedOperationAndPassedArguments(final String p_inputString){
        ArrayList<Map<String, String>> l_result = new ArrayList<>();
        final var l_mainRootCommand = fetchRootCommand();
        var l_variousOperationArguments = l_mainRootCommand.replace(l_mainRootCommand,"").trim();

        if(Objects.isNull(l_variousOperationArguments) || l_variousOperationArguments.isEmpty()){
            return l_result;
        }

        final var l_isKeyWithNoHifenAndNoValuePart = !l_variousOperationArguments.contains(" ") && !l_variousOperationArguments.contains(" ");

        if(l_isKeyWithNoHifenAndNoValuePart){
            l_variousOperationArguments = "-filename "+ l_variousOperationArguments;
        }

        var operationArguments = l_variousOperationArguments.split("-");

        for(String operationArgument : operationArguments){
            l_result.add(parseOperationArgument(operationArgument));
        }

        return l_result;
    }

    private Map<String, String> parseOperationArgument(String p_operationArgument){
        Map<String, String> l_result = new HashMap<>();

        var l_splitOperationArgument = p_operationArgument.split(" ");

        l_result.put("KEY_OPERATION",l_splitOperationArgument[0]);
        var l_argumentsParameters = "";
        if(l_splitOperationArgument.length > 1){
            String[] l_operationArgumentCombined = Arrays.copyOfRange(l_splitOperationArgument, 1, l_splitOperationArgument.length);
            l_argumentsParameters = String.join(" ",l_operationArgumentCombined );
        }

        l_result.put("KEY_OPERATION",l_argumentsParameters);

        return l_result;
    }

//    editcontinent -add continentID continentvalue -remove continentID
//    editcountry -add countryID continentID -remove countryID
//    editneighbor -add countryID neighborcountryID -remove countryID neighborcountryI


}
