package Models;

import Controllers.GamePlayerController;
import Logger.ConsoleLogger;
import Utils.CommandHandler;
import Utils.CommonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Player.
 */
public class Player {

    /**
     * The Console logger.
     */
    ConsoleLogger d_consoleLogger = new ConsoleLogger();
    /**
     * The D coutries owned represents the list of countries owned by the player.
     */
    List<Country> d_coutriesOwned;
    /**
     * The D continents owned represents the list of continents owned by the player.
     */
    List<Continent> d_continentsOwned;
    /**
     * The D orders to execute represents the list of orders to be executed by the player.
     */
    List<Deploy> d_ordersToExecute;
    /**
     * The D no of unallocated armies represents the number of unallocated armies for the player.
     */
    Integer d_noOfUnallocatedArmies;
    private String d_color;
    private String d_name;


    /**
     * Instantiates a new Player with the specified player name.
     *
     * @param p_playerName The name of the player.
     */
    public Player(String p_playerName) {
        this.d_name = p_playerName;
        this.d_noOfUnallocatedArmies = 0;
        this.d_ordersToExecute = new ArrayList<>();
    }


    /**
     * Instantiates a new Player.
     */
    public Player() {

    }

    /**
     * Gets the color of the player.
     *
     * @return The color of the player.
     */
    public String getD_color() {
        return d_color;
    }

    /**
     * Sets the color of the player.
     *
     * @param p_color The color of the player.
     */
    public void setD_color(String p_color) {
        d_color = p_color;
    }

    /**
     * Gets the name of the player.
     *
     * @return The name of the player.
     */
    public String getPlayerName() {
        return d_name;
    }

    /**
     * Sets the name of the player.
     *
     * @param p_name The name of the player.
     */
    public void setPlayerName(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Gets the list of countries owned by the player.
     *
     * @return The list of countries owned by the player.
     */
    public List<Country> getD_coutriesOwned() {
        return d_coutriesOwned;
    }

    /**
     * Sets the list of countries owned by the player.
     *
     * @param p_coutriesOwned The list of countries owned by the player.
     */
    public void setD_coutriesOwned(List<Country> p_coutriesOwned) {
        this.d_coutriesOwned = p_coutriesOwned;
    }

    /**
     * Gets the list of continents owned by the player.
     *
     * @return The list of continents owned by the player.
     */
    public List<Continent> getD_continentsOwned() {
        return d_continentsOwned;
    }

    /**
     * Sets the list of continents owned by the player.
     *
     * @param p_continentsOwned The list of continents owned by the player.
     */
    public void setD_continentsOwned(List<Continent> p_continentsOwned) {
        this.d_continentsOwned = p_continentsOwned;
    }

    /**
     * Gets the list of orders to be executed by the player.
     *
     * @return The list of orders to be executed.
     */
    public List<Deploy> getD_ordersToExecute() {
        return d_ordersToExecute;
    }

    /**
     * Sets the list of orders to be executed by the player.
     *
     * @param p_ordersToExecute The list of orders to be executed.
     */
    public void setD_ordersToExecute(List<Deploy> p_ordersToExecute) {
        this.d_ordersToExecute = p_ordersToExecute;
    }

    /**
     * Gets the number of unallocated armies for the player.
     *
     * @return The number of unallocated armies.
     */
    public Integer getD_noOfUnallocatedArmies() {
        return d_noOfUnallocatedArmies;
    }

    /**
     * Sets the number of unallocated armies for the player.
     *
     * @param p_numberOfArmies The number of unallocated armies.
     */
    public void setD_noOfUnallocatedArmies(Integer p_numberOfArmies) {
        this.d_noOfUnallocatedArmies = p_numberOfArmies;
    }

    /**
     * Get continent names owned by the player.
     *
     * @return List of continent names owned by the player.
     */
    public List<String> getContinentNames() {
        List<String> l_continentNames = new ArrayList<String>();
        if (d_continentsOwned != null) {
            for (Continent c : d_continentsOwned) {
                l_continentNames.add(c.getD_continentName());
            }
            return l_continentNames;
        }
        return null;
    }

    /**
     * Get country names owned by the player.
     *
     * @return List of country names owned by the player.
     */
    public List<String> getCountryNames() {
        List<String> l_countryNames = new ArrayList<String>();
        for (Country c : d_coutriesOwned) {
            l_countryNames.add(c.getD_countryName());
        }
        return l_countryNames;
    }


    /**
     * Issue an order for the player.
     *
     * @throws IOException Throws an exception if there is an input/output error.
     */
    public void issueOrder() throws IOException {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        GamePlayerController l_Game_playerController = new GamePlayerController();
        d_consoleLogger.writeLog("\nPlease enter command to deploy reinforcement armies on the map for player : "
                + this.getPlayerName());
        String l_commandEntered = l_reader.readLine();
        CommandHandler l_command = new CommandHandler(l_commandEntered);

        if (l_command.getRootCommand().equalsIgnoreCase("deploy") && l_commandEntered.split(" ").length == 3) {
            l_Game_playerController.createDeployOrder(l_commandEntered, this);
        } else {
            d_consoleLogger.writeLog("Invalid command. To deploy armies, use the 'deploy' command in the format: deploy countryID <CountryName> <num> (until all reinforcements have been placed)");
        }
    }

    /**
     * Get the next order to be executed by the player.
     *
     * @return The next order to be executed or null if there are no orders.
     */
    public Deploy nextOrder() {
        if (CommonUtil.isCollectionEmpty(this.d_ordersToExecute)) {
            return null;
        }
        Deploy l_order = this.d_ordersToExecute.get(0);
        this.d_ordersToExecute.remove(l_order);
        return l_order;
    }
}
