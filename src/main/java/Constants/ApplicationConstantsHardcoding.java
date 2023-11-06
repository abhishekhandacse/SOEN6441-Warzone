package Constants;

import java.util.Arrays;
import java.util.List;

public class ApplicationConstantsHardcoding {

    public static final String RED_COLOR = "\033[0;31m";
    public static final String GREEN_COLOR = "\033[0;32m";
    public static final String YELLOW_COLOR = "\033[0;33m";
    public static final String BLUE_COLOR = "\033[0;34m";
    public static final String PURPLE_COLOR = "\033[0;35m";
    public static final String CYAN_COLOR = "\033[0;36m";
    public static final String VALID_MAP_MESSAGE = "The loaded map is valid!";

    public static final String ARGUMENTS_PASSED = "arguments";
    public static final String OPERATION_REQUESTED = "operation";

    public static final String EXTENSION_MAP_FILE = ".map";


    public static final String WHITE_COLOR = "\u001B[47m";

    public static final String ALL_CONTINENTS = "[continents]";
    public static final String ALL_COUNTRIES = "[countries]";
    public static final String ALL_BORDERS = "[borders]";
    public static final String ALL_ARMIES = "Armies";
    public static final String CONTINENT_CONTROL_VALUE = "Control Value";
    public static final String GRAPH_CONNECTIONS = "Connections";
    public static final String CLASSPATH_SRC_MAIN_RESOURCES = "src/main/resources";
    public static final int DISPLAY_WIDTH = 80;

    public static final List<String> ALL_COLORS = Arrays.asList(RED_COLOR, GREEN_COLOR, YELLOW_COLOR, BLUE_COLOR, PURPLE_COLOR, CYAN_COLOR);
    public static final List<String> BLOCKADEVALIDATION = Arrays.asList("bomb", "advance", "airlift", "negotiate");

    public static final List<String> ALL_CARDS = Arrays.asList("bomb", "blockade", "airlift", "negotiate");
    public static final int COLLECTION_SIZE = ALL_CARDS.size();

    public static final String COMMAND_INVALID_ERROR_EDITMAP = "Invalid command. Kindly provide command in Format of : editmap filename";
    public static final String COMMAND_INVALID_ERROR_EDITCONTINENT = "Invalid command. Kindly provide command in Format of : editcontinent -add continentID continentvalue -remove continentID";
    public static final String COMMAND_INVALID_ERROR_EDITCOUNTRY = "Invalid command. Kindly provide command in Format of : editcountry -add countrytID continentID -remove countryID";
    public static final String COMMAND_INVALID_ERROR_EDITNEIGHBOUR = "Invalid command. Kindly provide command in Format of : editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID";
    public static final String COMMAND_INVALID_ERROR_SAVEMAP = "Invalid command. Kindly provide command in Format of : savemap filename";

    String VALIDATION_FAILD_MAP_ERROR_EMPTY = "No Map found! Please load a valid map to check!";
    public static final String VALIDATION_FAILED_ERROR_LOADMAP = "Invalid command. Kindly provide command in Format of : loadmap filename";
    public static final String VALIDATION_FAILED_ERROR_VALIDATEMAP = "Invalid command! validatemap is not supposed to have any arguments";
    public static final String VALIDATION_FAILED_ERROR_GAMEPLAYER = "Invalid command. Kindly provide command in Format of : gameplayer -add playername -remove playername";
    public static final String VALIDATION_FAILED_MAP_LOADED = "Map cannot be loaded, as it is invalid. Kindly provide valid map";
    public static final String COMMAND_INVALID_ERROR_ASSIGNCOUNTRIES = "Invalid command. Kindly provide command in Format of : assigncountries";
    public static final String COMMAND_INVALID_ERROR_DEPLOY_ORDER = "Invalid command. Kindly provide command in Format of : deploy countryID <CountryName> <num> (until all reinforcements have been placed)";
}
