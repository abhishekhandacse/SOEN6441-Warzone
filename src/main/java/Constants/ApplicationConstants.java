package Constants;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Constants class for storing various application constants.
 */
public final class ApplicationConstants implements Serializable {

    /**
     * ANSI color codes for console output.
     */
    public static final String RED = "\033[0;31m";

    /**
     * ANSI color codes for console output.
     */
    public static final String GREEN = "\033[0;32m";

    /**
     * ANSI color codes for console output.
     */
    public static final String YELLOW = "\033[0;33m";

    /**
     * ANSI color codes for console output.
     */
    public static final String BLUE = "\033[0;34m";

    /**
     * ANSI color codes for console output.
     */
    public static final String PURPLE = "\033[0;35m";

    /**
     * ANSI color codes for console output.
     */
    public static final String CYAN = "\033[0;36m";

    /**
     * ANSI color codes for console output.
     */
    public static final String WHITE = "\u001B[47m";

    // Messages for different scenarios/errors
    // ... (Previous constants remain unchanged)

    /**
     * File extension for map files.
     */
    public static final String MAPFILEEXTENSION = ".map";

    // ... (Previous constants remain unchanged)

    /**
     * List of available colors for players.
     */
    public static final List<String> COLORS = Arrays.asList(RED, GREEN, YELLOW, BLUE, PURPLE, CYAN);

    /**
     * List of valid commands for blockade validation.
     */
    public static final List<String> BLOCKADEVALIDATION = Arrays.asList("bomb", "advance", "airlift", "negotiate");

    /**
     * List of available cards.
     */
    public static final List<String> CARDS = Arrays.asList("bomb", "blockade", "airlift", "negotiate");

    /**
     * Number of cards available.
     */
    public static final int SIZE = CARDS.size();

    // ... (Previous constants remain unchanged)

    /**
     * Conquest map continents section identifier.
     */
    public static final String CONQUEST_CONTINENTS = "[Continents]";

    /**
     * Conquest map territories section identifier.
     */
    public static final String CONQUEST_TERRITORIES = "[Territories]";

    /**
     * List of available player behaviors.
     */
    public static final List<String> PLAYER_BEHAVIORS = Arrays.asList("Human", "Aggressive", "Random", "Benevolent", "Cheater");

    /**
     * List of available player behaviors for tournament mode.
     */
    public static final List<String> TOURNAMENT_PLAYER_BEHAVIORS = Arrays.asList("Aggressive", "Random", "Benevolent", "Cheater");

    /**
     * Width of the console display.
     */
    public static final int CONSOLE_WIDTH = 80;

    /**
     * VALID_MAP
     */
    public static final String VALID_MAP = "The loaded map is valid!";

    /**
     * INVALID_COMMAND_TOURNAMENT_MODE
     */
	public static final String INVALID_COMMAND_TOURNAMENT_MODE = "Invalid Command. Kindly provide command in format of : tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D maxnumberofturns";
    
    /**
     * ARGUMENTS
     */
    public static final String ARGUMENTS = "arguments";
	
    /**
     * OPERATION
     */
    public static final String OPERATION = "operation";

    /**
     * INVALID_COMMAND_ERROR_SAVEGAME
     */
    public static final String INVALID_COMMAND_ERROR_SAVEGAME = "Invalid command. Kindly provide command in Format of : savegame filename";
	
    /**
     * INVALID_COMMAND_ERROR_LOADGAME
     */
    public static final String INVALID_COMMAND_ERROR_LOADGAME = "Invalid command. Kindly provide command in Format of : loadgame filename";
	
    /**
     * INVALID_COMMAND_ERROR_EDITMAP
     */
    public static final String INVALID_COMMAND_ERROR_EDITMAP = "Invalid command. Kindly provide command in Format of : editmap filename";
	
    /**
     * INVALID_COMMAND_ERROR_EDITCONTINENT
     */
    public static final String INVALID_COMMAND_ERROR_EDITCONTINENT = "Invalid command. Kindly provide command in Format of : editcontinent -add continentID continentvalue -remove continentID";
	
    /**
     * INVALID_COMMAND_ERROR_EDITCOUNTRY
     */
    public static final String INVALID_COMMAND_ERROR_EDITCOUNTRY = "Invalid command. Kindly provide command in Format of : editcountry -add countrytID continentID -remove countryID";
	
    /**
     * INVALID_COMMAND_ERROR_EDITNEIGHBOUR
     */
    public static final String INVALID_COMMAND_ERROR_EDITNEIGHBOUR = "Invalid command. Kindly provide command in Format of : editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID";
	
    /**
     * INVALID_COMMAND_ERROR_SAVEMAP
     */
    public static final String INVALID_COMMAND_ERROR_SAVEMAP = "Invalid command. Kindly provide command in Format of : savemap filename";
	
    /**
     * INVALID_MAP_ERROR_EMPTY
     */
    public static final String INVALID_MAP_ERROR_EMPTY = "No Map found! Please load a valid map to check!";
	
    /**
     * INVALID_COMMAND_ERROR_LOADMAP
     */
    public static final String INVALID_COMMAND_ERROR_LOADMAP = "Invalid command. Kindly provide command in Format of : loadmap filename";
	
    /**
     * INVALID_COMMAND_ERROR_VALIDATEMAP
     */
    public static final String INVALID_COMMAND_ERROR_VALIDATEMAP = "Invalid command! validatemap is not supposed to have any arguments";
	
    /**
     * INVALID_COMMAND_ERROR_GAMEPLAYER
     */
    public static final String INVALID_COMMAND_ERROR_GAMEPLAYER = "Invalid command. Kindly provide command in Format of : gameplayer -add playername -remove playername";
	
    /**
     * INVALID_MAP_LOADED
     */
    public static final String INVALID_MAP_LOADED = "Map cannot be loaded, as it is invalid. Kindly provide valid map";
	
    /**
     * INVALID_COMMAND_ERROR_ASSIGNCOUNTRIES
     */
    public static final String INVALID_COMMAND_ERROR_ASSIGNCOUNTRIES = "Invalid command. Kindly provide command in Format of : assigncountries";
	
    /**
     * INVALID_COMMAND_ERROR_DEPLOY_ORDER
     */
    public static final String INVALID_COMMAND_ERROR_DEPLOY_ORDER = "Invalid command. Kindly provide command in Format of : deploy countryID <CountryName> <num> (until all reinforcements have been placed)";

	/**
     * CONTINENTS
     */
    public static final String CONTINENTS = "[continents]";
	
    /**
     * COUNTRIES
     */
    public static final String COUNTRIES = "[countries]";
	
    /**
     * BORDERS
     */
    public static final String BORDERS = "[borders]";
	
    /**
     * ARMIES
     */
    public static final String ARMIES = "Armies";
	
    /**
     * CONTROL_VALUE
     */
    public static final String CONTROL_VALUE = "Control Value";
	
    
    /**
     * CONNECTIVITY
     */
    public static final String CONNECTIVITY = "Connections";
	
    /**
     * SRC_MAIN_RESOURCES
     */
    public static final String SRC_MAIN_RESOURCES = "src/main/resources";
}
