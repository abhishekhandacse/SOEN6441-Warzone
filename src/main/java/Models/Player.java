package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Controllers.GamePlayerController;
import Logger.ConsoleLogger;
import Utils.CommandHandler;
import Utils.CommonUtil;


/**
 * The type Player.
 */
public class Player {

    /**
     * The Console logger.
     */
    ConsoleLogger consoleLogger = new ConsoleLogger();

    private String d_color;
    private String d_name;
    /**
     * The D coutries owned.
     */
    List<Country> d_coutriesOwned;
    /**
     * The D continents owned.
     */
    List<Continent> d_continentsOwned;
    /**
     * The D orders to execute.
     */
    List<Order> d_ordersToExecute;
    /**
     * The D no of unallocated armies.
     */
    Integer d_noOfUnallocatedArmies;


    /**
     * Instantiates a new Player.
     *
     * @param p_playerName the p player name
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
     * Sets player name.
     *
     * @param p_name the p name
     */
    public void setPlayerName(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Sets d color.
     *
     * @param p_color the p color
     */
    public void setD_color(String p_color) {
        d_color = p_color;
    }

    /**
     * Sets d no of unallocated armies.
     *
     * @param p_numberOfArmies the p number of armies
     */
    public void setD_noOfUnallocatedArmies(Integer p_numberOfArmies) {
        this.d_noOfUnallocatedArmies = p_numberOfArmies;
    }

    /**
     * Sets d coutries owned.
     *
     * @param p_coutriesOwned the p coutries owned
     */
    public void setD_coutriesOwned(List<Country> p_coutriesOwned) {
        this.d_coutriesOwned = p_coutriesOwned;
    }

    /**
     * Sets d continents owned.
     *
     * @param p_continentsOwned the p continents owned
     */
    public void setD_continentsOwned(List<Continent> p_continentsOwned) {
        this.d_continentsOwned = p_continentsOwned;
    }

    /**
     * Sets d orders to execute.
     *
     * @param p_ordersToExecute the p orders to execute
     */
    public void setD_ordersToExecute(List<Order> p_ordersToExecute) {
        this.d_ordersToExecute = p_ordersToExecute;
    }

    /**
     * Gets d color.
     *
     * @return the d color
     */
    public String getD_color() {
        return d_color;
    }

    /**
     * Gets player name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return d_name;
    }

    /**
     * Gets d coutries owned.
     *
     * @return the d coutries owned
     */
    public List<Country> getD_coutriesOwned() {
        return d_coutriesOwned;
    }

    /**
     * Gets d continents owned.
     *
     * @return the d continents owned
     */
    public List<Continent> getD_continentsOwned() {
        return d_continentsOwned;
    }

    /**
     * Gets d orders to execute.
     *
     * @return the d orders to execute
     */
    public List<Order> getD_ordersToExecute() {
        return d_ordersToExecute;
    }

    /**
     * Gets d no of unallocated armies.
     *
     * @return the d no of unallocated armies
     */
    public Integer getD_noOfUnallocatedArmies() {
        return d_noOfUnallocatedArmies;
    }

    /**
     * Get continent names list.
     *
     * @return the list
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
     * Get country names list.
     *
     * @return the list
     */
    public List<String> getCountryNames() {
        List<String> l_countryNames = new ArrayList<String>();
        for (Country c : d_coutriesOwned) {
            l_countryNames.add(c.getD_countryName());
        }
        return l_countryNames;
    }


    /**
     * Issue order.
     *
     * @throws IOException the io exception
     */
    public void issueOrder() throws IOException {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        GamePlayerController l_Game_playerController = new GamePlayerController();
        consoleLogger.writeLog("\nPlease enter command to deploy reinforcement armies on the map for player : "
                + this.getPlayerName());
        String l_commandEntered = l_reader.readLine();
        CommandHandler l_command = new CommandHandler(l_commandEntered);

        if (l_command.getRootCommand().equalsIgnoreCase("deploy") && l_commandEntered.split(" ").length == 3) {
            l_Game_playerController.createDeployOrder(l_commandEntered, this);
        } else {
            consoleLogger.writeLog("Invalid command. To deploy armies, use the 'deploy' command in the format: deploy countryID <CountryName> <num> (until all reinforcements have been placed)");
        }
    }

    /**
     * Next order order.
     *
     * @return the order
     */
    public Order nextOrder() {
        if (CommonUtil.isCollectionEmpty(this.d_ordersToExecute)) {
            return null;
        }
        Order l_order = this.d_ordersToExecute.get(0);
        this.d_ordersToExecute.remove(l_order);
        return l_order;
    }
}
