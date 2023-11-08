package Constants;
import java.util.Arrays;
import java.util.List;

/**
 * This class initializes all the constants that are going to be used throughout the application.
 *
 */
public final class ApplicationConstantsHardcoding {

	/**
     * Message indicating that the loaded map is valid.
     */
	public static final String VALID_MAP_MESSAGE = "The loaded map is valid!";

	/**
     * Key for arguments passed.
     */
    public static final String ARGUMENTS_PASSED = "arguments";

    /**
     * Key for the operation requested.
     */
    public static final String OPERATION_REQUESTED = "operation";

    /**
     * File extension for map files.
     */
    public static final String EXTENSION_MAP_FILE = ".map";

    /**
     * ANSI escape code for red text color.
     */
    public static final String RED_COLOR = "\033[0;31m";

    /**
     * ANSI escape code for green text color.
     */
    public static final String GREEN_COLOR = "\033[0;32m";

    /**
     * ANSI escape code for yellow text color.
     */
    public static final String YELLOW_COLOR = "\033[0;33m";

    /**
     * ANSI escape code for blue text color.
     */
    public static final String BLUE_COLOR = "\033[0;34m";

    /**
     * ANSI escape code for purple text color.
     */
    public static final String PURPLE_COLOR = "\033[0;35m";

    /**
     * ANSI escape code for cyan text color.
     */
    public static final String CYAN_COLOR = "\033[0;36m";

    /**
     * ANSI escape code for white background color.
     */
    public static final String WHITE_COLOR = "\u001B[47m";

	/**
     * String representing the "[continents]" section in map files.
     */
    public static final String ALL_CONTINENTS = "[continents]";

    /**
     * String representing the "[countries]" section in map files.
     */
    public static final String ALL_COUNTRIES = "[countries]";

    /**
     * String representing the "[borders]" section in map files.
     */
    public static final String ALL_BORDERS = "[borders]";

    /**
     * String representing the "Armies" keyword.
     */
    public static final String ALL_ARMIES = "Armies";

    /**
     * String representing the "Control Value" keyword.
     */
    public static final String CONTINENT_CONTROL_VALUE = "Control Value";

    /**
     * String representing the "Connections" keyword.
     */
    public static final String GRAPH_CONNECTIONS = "Connections";

    /**
     * Default classpath for resources.
     */
    public static final String CLASSPATH_SRC_MAIN_RESOURCES = "src/main/resources";

    /**
     * Width for displaying messages.
     */
    public static final int DISPLAY_WIDTH = 80;

	/**
     * List of ANSI escape codes for various colors.
     */
    public static final List<String> ALL_COLORS = Arrays.asList(RED_COLOR, GREEN_COLOR, YELLOW_COLOR, BLUE_COLOR, PURPLE_COLOR, CYAN_COLOR);

    /**
     * List of valid orders for the blockade action.
     */
    public static final List<String> BLOCKADEVALIDATION = Arrays.asList("bomb", "advance", "airlift", "negotiate");

    /**
     * List of all available card types.
     */
    public static final List<String> ALL_CARDS = Arrays.asList("bomb", "blockade", "airlift", "negotiate");

    /**
     * Size of the card collection.
     */
    public static final int COLLECTION_SIZE = ALL_CARDS.size();

	/**
     * Error message for an invalid "editmap" command.
     */
    public static final String COMMAND_INVALID_ERROR_EDITMAP = "Invalid command. Kindly provide command in Format of : editmap filename";

    /**
     * Error message for an invalid "editcontinent" command.
     */
    public static final String COMMAND_INVALID_ERROR_EDITCONTINENT = "Invalid command. Kindly provide command in Format of : editcontinent -add continentID continentvalue -remove continentID";

    /**
     * Error message for an invalid "editcountry" command.
     */
    public static final String COMMAND_INVALID_ERROR_EDITCOUNTRY = "Invalid command. Kindly provide command in Format of : editcountry -add countrytID continentID -remove countryID";

    /**
     * Error message for an invalid "editneighbor" command.
     */
    public static final String COMMAND_INVALID_ERROR_EDITNEIGHBOUR = "Invalid command. Kindly provide command in Format of : editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID";

    /**
     * Error message for an invalid "savemap" command.
     */
    public static final String COMMAND_INVALID_ERROR_SAVEMAP = "Invalid command. Kindly provide command in Format of : savemap filename";

    /**
     * Error message when no map is found for validation.
     */
    public static final String VALIDATION_FAILD_MAP_ERROR_EMPTY = "No Map found! Please load a valid map to check!";

    /**
     * Error message for an invalid "loadmap" command.
     */
    public static final String VALIDATION_FAILED_ERROR_LOADMAP = "Invalid command. Kindly provide command in Format of : loadmap filename";

    /**
     * Error message for an invalid "validatemap" command.
     */
    public static final String VALIDATION_FAILED_ERROR_VALIDATEMAP = "Invalid command! validatemap is not supposed to have any arguments";

    /**
     * Error message for an invalid "gameplayer" command.
     */
    public static final String VALIDATION_FAILED_ERROR_GAMEPLAYER = "Invalid command. Kindly provide command in Format of : gameplayer -add playername -remove playername";

    /**
     * Error message for a failed map load.
     */
    public static final String VALIDATION_FAILED_MAP_LOADED = "Map cannot be loaded, as it is invalid. Kindly provide a valid map";

    /**
     * Error message for an invalid "assigncountries" command.
     */
    public static final String COMMAND_INVALID_ERROR_ASSIGNCOUNTRIES = "Invalid command. Kindly provide command in Format of : assigncountries";

    /**
     * Error message for an invalid "deploy" order command.
     */
    public static final String COMMAND_INVALID_ERROR_DEPLOY_ORDER = "Invalid command. Kindly provide command in Format of : deploy countryID <CountryName> <num> (until all reinforcements have been placed)";
}
