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
}
