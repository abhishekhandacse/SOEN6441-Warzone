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
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
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

    public static final String VALID_MAP = "The loaded map is valid!";
	public static final String INVALID_COMMAND_TOURNAMENT_MODE = "Invalid Command. Kindly provide command in format of : tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D maxnumberofturns";
    public static final String ARGUMENTS = "arguments";
	public static final String OPERATION = "operation";

    public static final String INVALID_COMMAND_ERROR_SAVEGAME = "Invalid command. Kindly provide command in Format of : savegame filename";
	public static final String INVALID_COMMAND_ERROR_LOADGAME = "Invalid command. Kindly provide command in Format of : loadgame filename";
	public static final String INVALID_COMMAND_ERROR_EDITMAP = "Invalid command. Kindly provide command in Format of : editmap filename";
	public static final String INVALID_COMMAND_ERROR_EDITCONTINENT = "Invalid command. Kindly provide command in Format of : editcontinent -add continentID continentvalue -remove continentID";
	public static final String INVALID_COMMAND_ERROR_EDITCOUNTRY = "Invalid command. Kindly provide command in Format of : editcountry -add countrytID continentID -remove countryID";
	public static final String INVALID_COMMAND_ERROR_EDITNEIGHBOUR = "Invalid command. Kindly provide command in Format of : editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID";
	public static final String INVALID_COMMAND_ERROR_SAVEMAP = "Invalid command. Kindly provide command in Format of : savemap filename";
	public static final String INVALID_MAP_ERROR_EMPTY = "No Map found! Please load a valid map to check!";
	public static final String INVALID_COMMAND_ERROR_LOADMAP = "Invalid command. Kindly provide command in Format of : loadmap filename";
	public static final String INVALID_COMMAND_ERROR_VALIDATEMAP = "Invalid command! validatemap is not supposed to have any arguments";
	public static final String INVALID_COMMAND_ERROR_GAMEPLAYER = "Invalid command. Kindly provide command in Format of : gameplayer -add playername -remove playername";
	public static final String INVALID_MAP_LOADED = "Map cannot be loaded, as it is invalid. Kindly provide valid map";
	public static final String INVALID_COMMAND_ERROR_ASSIGNCOUNTRIES = "Invalid command. Kindly provide command in Format of : assigncountries";
	public static final String INVALID_COMMAND_ERROR_DEPLOY_ORDER = "Invalid command. Kindly provide command in Format of : deploy countryID <CountryName> <num> (until all reinforcements have been placed)";

	public static final String CONTINENTS = "[continents]";
	public static final String COUNTRIES = "[countries]";
	public static final String BORDERS = "[borders]";
	public static final String ARMIES = "Armies";
	public static final String CONTROL_VALUE = "Control Value";
	public static final String CONNECTIVITY = "Connections";
	public static final String SRC_MAIN_RESOURCES = "src/main/resources";
}
