package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import Constants.ApplicationConstantsHardcoding;
import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Utils.CommonUtil;

/**
 * Represents a player in the Risk game.
 */
public class ModelPlayer {
    /**
     * The color assigned to the player.
     */
    private String d_color;

    /**
     * The name of the player.
     */
    private String d_name;

    /**
     * List of countries owned by the player.
     */
    List<ModelCountry> d_coutriesOwned;

    /**
     * List of continents owned by the player.
     */
    List<ModelContinent> d_continentsOwned;

    /**
     * List of orders to execute for the player.
     */
    List<Order> order_list;

    /**
     * The number of unallocated armies the player has.
     */
    Integer d_noOfUnallocatedArmies;

    /**
     * Indicates if the player has more orders to give.
     */
    boolean d_moreOrders;

    /**
     * Indicates if the player can receive only one card per turn.
     */
    boolean d_oneCardPerTurn = false;

    /**
     * Log for player actions and messages.
     */
    String d_playerLog;

    /**
     * List of cards owned by the player.
     */
    List<String> d_cardsOwnedByPlayer = new ArrayList<String>();

    /**
     * List of players the current player has negotiated with.
     */
    List<ModelPlayer> d_negotiatedWith = new ArrayList<ModelPlayer>();

    /**
     * Creates a new player with the given name.
     *
     * @param p_playerName The name of the player.
     */
    public ModelPlayer(String p_playerName) {
        this.d_name = p_playerName;
        this.d_noOfUnallocatedArmies = 0;
        this.d_coutriesOwned = new ArrayList<ModelCountry>();
        this.order_list = new ArrayList<Order>();
        this.d_moreOrders = true;
    }

    /**
     * Default constructor for ModelPlayer.
     */
    public ModelPlayer() {
    }

    /**
     * Get the name of the player.
     *
     * @return The name of the player.
     */
    public String getPlayerName() {
        return d_name;
    }

    /**
     * Set the name of the player.
     *
     * @param p_name The new name for the player.
     */
    public void setPlayerName(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Get the color assigned to the player.
     *
     * @return The color assigned to the player.
     */
    public String getD_color() {
        return d_color;
    }

    /**
     * Set the color for the player.
     *
     * @param p_color The color to assign to the player.
     */
    public void setD_color(String p_color) {
        d_color = p_color;
    }

    /**
     * Get the list of countries owned by the player.
     *
     * @return The list of countries owned by the player.
     */
    public List<ModelCountry> getD_coutriesOwned() {
        return d_coutriesOwned;
    }

    /**
     * Set the list of countries owned by the player.
     *
     * @param p_coutriesOwned The new list of countries owned by the player.
     */
    public void setD_coutriesOwned(List<ModelCountry> p_coutriesOwned) {
        this.d_coutriesOwned = p_coutriesOwned;
    }

    /**
     * Get the list of continents owned by the player.
     *
     * @return The list of continents owned by the player.
     */
    public List<ModelContinent> getD_continentsOwned() {
        return d_continentsOwned;
    }

    /**
     * Set the list of continents owned by the player.
     *
     * @param p_continentsOwned The new list of continents owned by the player.
     */
    public void setD_continentsOwned(List<ModelContinent> p_continentsOwned) {
        this.d_continentsOwned = p_continentsOwned;
    }

    /**
     * Get the list of orders to execute for the player.
     *
     * @return The list of orders to execute for the player.
     */
    public List<Order> getD_ordersToExecute() {
        return order_list;
    }

    /**
     * Set the list of orders to execute for the player.
     *
     * @param p_ordersToExecute The new list of orders to execute for the player.
     */
    public void setD_ordersToExecute(List<Order> p_ordersToExecute) {
        this.order_list = p_ordersToExecute;
    }

    /**
     * Get the number of unallocated armies the player has.
     *
     * @return The number of unallocated armies.
     */
    public Integer getD_noOfUnallocatedArmies() {
        return d_noOfUnallocatedArmies;
    }

    /**
     * Set the number of unallocated armies for the player.
     *
     * @param p_numberOfArmies The new number of unallocated armies.
     */
    public void setD_noOfUnallocatedArmies(Integer p_numberOfArmies) {
        this.d_noOfUnallocatedArmies = p_numberOfArmies;
    }

    /**
     * Add a player to the list of players the current player has negotiated with.
     *
     * @param p_playerNegotiation The player to add to the list.
     */
    public void addPlayerNegotiation(ModelPlayer p_playerNegotiation) {
        this.d_negotiatedWith.add(p_playerNegotiation);
    }

	 /**
     * Get the value indicating whether the player can make more orders.
     *
     * @return True if the player can make more orders, false otherwise.
     */
    public boolean getD_moreOrders() {
        return d_moreOrders;
    }

    /**
     * Set the value indicating whether the player can make more orders.
     *
     * @param p_moreOrders True if the player can make more orders, false otherwise.
     */
    public void setD_moreOrders(boolean p_moreOrders) {
        this.d_moreOrders = p_moreOrders;
    }

    /**
     * Get the list of cards owned by the player.
     *
     * @return A list of card names owned by the player.
     */
    public List<String> getD_cardsOwnedByPlayer() {
        return this.d_cardsOwnedByPlayer;
    }

    /**
     * Set whether the player can play only one card per turn.
     *
     * @param p_value True if the player can play only one card per turn, false otherwise.
     */
    public void setD_oneCardPerTurn(Boolean p_value) {
        this.d_oneCardPerTurn = p_value;
    }

    /**
     * Get the names of the countries owned by the player.
     *
     * @return A list of country names owned by the player.
     */
    public List<String> getCountryNames() {
        List<String> l_countryNames = new ArrayList<String>();
        for (ModelCountry c : d_coutriesOwned) {
            l_countryNames.add(c.getD_countryName());
        }
        return l_countryNames;
    }

    /**
     * Get the names of the continents owned by the player.
     *
     * @return A list of continent names owned by the player, or null if no continents are owned.
     */
    public List<String> getContinentNames() {
        List<String> l_continentNames = new ArrayList<String>();
        if (d_continentsOwned != null) {
            for (ModelContinent c : d_continentsOwned) {
                l_continentNames.add(c.getD_continentName());
            }
            return l_continentNames;
        }
        return null;
    }

    /**
     * Set the player's log message and optionally print it to the console.
     *
     * @param p_playerLog The log message to set.
     * @param p_typeLog   The type of log message ("error" or "log").
     */
    public void setD_playerLog(String p_playerLog, String p_typeLog) {
        this.d_playerLog = p_playerLog;
        if (p_typeLog.equals("error"))
            System.err.println(p_playerLog);
        else if (p_typeLog.equals("log"))
            System.out.println(p_playerLog);
    }

    /**
     * Get the player's log message.
     *
     * @return The player's log message.
     */
    public String getD_playerLog() {
        return this.d_playerLog;
    }

	
    /**
     * Check if the player has more orders to give.
     *
     * @return true if the player has more orders, false otherwise.
     * @throws IOException If there is an input/output error.
     */
    void checkForMoreOrders() throws IOException {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nDo you still want to give order for player : " + this.getPlayerName()
                + " in the next turn ? \nPress Y for Yes or N for No");
        String l_nextOrderCheck = l_reader.readLine();
        if (l_nextOrderCheck.equalsIgnoreCase("Y") || l_nextOrderCheck.equalsIgnoreCase("N")) {
            this.setD_moreOrders(l_nextOrderCheck.equalsIgnoreCase("Y"));
        } else {
            System.err.println("Invalid Input Passed.");
            this.checkForMoreOrders();
        }
    }

        /**
     * Creates a deploy order based on the provided command and adds it to the order list.
     *
     * @param p_commandEntered The command for creating a deploy order.
     */
    public void createDeployOrder(String p_commandEntered) {
        String l_targetCountry;
        String l_noOfArmies;
        try {
            l_targetCountry = p_commandEntered.split(" ")[1];
            l_noOfArmies = p_commandEntered.split(" ")[2];
            if (validateDeployOrderArmies(this, l_noOfArmies)) {
                this.setD_playerLog("Given deploy order can't be executed as armies in deploy order exceed player's unallocated armies.", "error");
            } else {
                this.order_list.add(new Deploy(this, l_targetCountry, Integer.parseInt(l_noOfArmies)));
                Integer l_unallocatedArmies = this.getD_noOfUnallocatedArmies() - Integer.parseInt(l_noOfArmies);
                this.setD_noOfUnallocatedArmies(l_unallocatedArmies);
                this.setD_playerLog("Deploy order has been added to the queue for execution. For player: " + this.d_name, "log");
            }
        } catch (Exception l_e) {
            this.setD_playerLog("Invalid deploy order entered", "error");
        }
    }

    /**
     * Validates if the deploy order armies exceed the player's unallocated armies.
     *
     * @param p_player      The player.
     * @param p_noOfArmies  The number of armies in the deploy order.
     * @return True if the deploy order armies exceed the player's unallocated armies, false otherwise.
     */
    public boolean validateDeployOrderArmies(ModelPlayer p_player, String p_noOfArmies) {
        return p_player.getD_noOfUnallocatedArmies() < Integer.parseInt(p_noOfArmies);
    }

    /**
     * Issues orders during the issue order phase.
     *
     * @param p_issueOrderPhase The issue order phase object.
     * @throws CommandValidationException If an invalid command is provided.
     * @throws IOException If there is an input/output error.
     * @throws MapValidationException If the map is invalid.
     */
	public void issue_order(IssueOrderPhase p_issueOrderPhase) throws CommandValidationException, IOException, MapValidationException {
        p_issueOrderPhase.askForOrder(this);
    }

    /**
     * Retrieves the next order to execute from the order list.
     *
     * @return The next order to execute or null if there are no more orders.
     */
    public Order next_order() {
        if (CommonUtil.isNullOrEmptyCollection(this.order_list)) {
            return null;
        }
        Order l_order = this.order_list.get(0);
        this.order_list.remove(l_order);
        return l_order;
    }

    /**
     * Creates an advance order based on the provided command and adds it to the order list.
     *
     * @param p_commandEntered The command for creating an advance order.
     * @param p_gameState      The current game state.
     */
    public void createAdvanceOrder(String p_commandEntered, GameState p_gameState) {
        try {
            if (p_commandEntered.split(" ").length == 4) {
                String l_sourceCountry = p_commandEntered.split(" ")[1];
                String l_targetCountry = p_commandEntered.split(" ")[2];
                String l_noOfArmies = p_commandEntered.split(" ")[3];
                if (this.checkCountryExists(l_sourceCountry, p_gameState)
                        && this.checkCountryExists(l_targetCountry, p_gameState)
                        && !checkZeroArmiesInOrder(l_noOfArmies)
                        && checkAdjacency(p_gameState, l_sourceCountry, l_targetCountry)) {
                    this.order_list
                            .add(new ModelAdvance(this, l_sourceCountry, l_targetCountry, Integer.parseInt(l_noOfArmies)));
                    this.setD_playerLog("Advance order has been added to the queue for execution. For player: " + this.d_name, "log");
                }
            } else {
                this.setD_playerLog("Invalid Arguments Passed For Advance Order", "error");
            }

        } catch (Exception l_e) {
            this.setD_playerLog("Invalid Advance Order Given", "error");
        }
    }

    /**
     * Checks if the given country exists in the game map.
     *
     * @param p_countryName The name of the country to check.
     * @param p_gameState   The current game state.
     * @return True if the country exists, false otherwise.
     */
    private Boolean checkCountryExists(String p_countryName, GameState p_gameState) {
        if (p_gameState.getD_map().getCountryByName(p_countryName) == null) {
            this.setD_playerLog("Country : " + p_countryName
                    + " given in advance order doesn't exist in the map. Order given is ignored.", "error");
            return false;
        }
        return true;
    }

    /**
     * Checks if the number of armies in the order is zero.
     *
     * @param p_noOfArmies The number of armies in the order.
     * @return True if the number of armies is zero, false otherwise.
     */
    private Boolean checkZeroArmiesInOrder(String p_noOfArmies) {
        if (Integer.parseInt(p_noOfArmies) == 0) {
            this.setD_playerLog("Advance order with 0 armies to move can't be issued.", "error");
            return true;
        }
        return false;
    }

	/**
 * Checks if two countries are adjacent to each other on the game map.
 *
 * @param p_gameState          The current game state containing the game map.
 * @param p_sourceCountryName  The name of the source country.
 * @param p_targetCountryName  The name of the target country.
 * @return True if the target country is adjacent to the source country, false otherwise.
 */

	@SuppressWarnings("unlikely-arg-type")
	public boolean checkAdjacency(GameState p_gameState, String p_sourceCountryName, String p_targetCountryName) {
		// Get the source and target countries from the game state.
		ModelCountry l_sourceCountry = p_gameState.getD_map().getCountryByName(p_sourceCountryName);
		ModelCountry l_targetCountry = p_gameState.getD_map().getCountryByName(p_targetCountryName);

		// Check if the target country's ID exists in the list of adjacent country IDs of the source country.
		Integer l_targetCountryId = l_sourceCountry.getD_adjacentCountryIds().stream()
				.filter(l_adjCountry -> Objects.equals(l_adjCountry, l_targetCountry.getD_countryId()))
				.findFirst()
				.orElse(null);

		// If the target country is not adjacent to the source country, log an error message and return false.
		if (l_targetCountryId == null) {
			this.setD_playerLog("Advance order cant be issued since target country : " + p_targetCountryName
					+ " is not adjacent to source country : " + p_sourceCountryName, "error");
			return false;
		}

		// The target country is adjacent to the source country.
		return true;
	}


    /**
     * Assigns a card to the player as a reward for a successful conquest.
     */
    public void assignCard() {
        if (!d_oneCardPerTurn) {
            Random l_random = new Random();
            this.d_cardsOwnedByPlayer.add(ApplicationConstantsHardcoding.ALL_CARDS.get(l_random.nextInt(ApplicationConstantsHardcoding.COLLECTION_SIZE)));
            this.setD_playerLog("Player: " + this.d_name + " has earned a card as a reward for the successful conquest - " + this.d_cardsOwnedByPlayer.get(this.d_cardsOwnedByPlayer.size() - 1), "log");
            this.setD_oneCardPerTurn(true);
        } else {
            this.setD_playerLog("Player: " + this.d_name + " has already earned the maximum cards that can be allotted in a turn", "error");
        }
    }


    /**
     * Removes a card from the player's list of owned cards.
     *
     * @param p_cardName The name of the card to remove.
     */
    public void removeCard(String p_cardName) {
        this.d_cardsOwnedByPlayer.remove(p_cardName);
    }

    /**
     * Validates if the player can attack a target country based on negotiation history.
     *
     * @param p_targetCountryName The name of the target country.
     * @return True if the player can attack the target country, false otherwise.
     */
    public boolean negotiationValidation(String p_targetCountryName) {
        boolean l_canAttack = true;
        for (ModelPlayer p : d_negotiatedWith) {
            if (p.getCountryNames().contains(p_targetCountryName))
                l_canAttack = false;
        }
        return l_canAttack;
    }

    /**
     * Resets the negotiation history by clearing the list of players negotiated with.
     */
    public void resetNegotiation() {
        d_negotiatedWith.clear();
    }

    /**
     * Checks the arguments for card commands and validates their format.
     *
     * @param p_commandEntered The entered card command.
     * @return True if the arguments are in the correct format, false otherwise.
     */
    public boolean checkCardArguments(String p_commandEntered) {
        if (p_commandEntered.split(" ")[0].equalsIgnoreCase("airlift")) {
            return p_commandEntered.split(" ").length == 4;
        } else if (p_commandEntered.split(" ")[0].equalsIgnoreCase("blockade")
                || p_commandEntered.split(" ")[0].equalsIgnoreCase("bomb")
                || p_commandEntered.split(" ")[0].equalsIgnoreCase("negotiate")) {
            return p_commandEntered.split(" ").length == 2;
        } else {
            return false;
        }
    }

    /**
     * Handles card commands and adds them to the order list for execution.
     *
     * @param p_commandEntered The entered card command.
     * @param p_gameState      The current game state.
     */
    public void handleCardCommands(String p_commandEntered, GameState p_gameState) {
        if (checkCardArguments(p_commandEntered)) {
            switch (p_commandEntered.split(" ")[0]) {
                case "airlift":
                    Card l_newOrder = new Airlift(p_commandEntered.split(" ")[1], p_commandEntered.split(" ")[2],
                            Integer.parseInt(p_commandEntered.split(" ")[3]), this);
                    if (l_newOrder.validOrderCheck(p_gameState)) {
                        this.order_list.add(l_newOrder);
                        this.setD_playerLog("Card Command Added to Queue for Execution Successfully!", "log");
                        p_gameState.updateLog(getD_playerLog(), "effect");
                    }
                    break;
                case "blockade":
                    Card l_blockadeOrder = new Blockade(this, p_commandEntered.split(" ")[1]);
                    if (l_blockadeOrder.validOrderCheck(p_gameState)) {
                        this.order_list.add(l_blockadeOrder);
                        this.setD_playerLog("Card Command Added to Queue for Execution Successfully!", "log");
                        p_gameState.updateLog(getD_playerLog(), "effect");
                    }
                    break;
                case "bomb":
                    Card l_bombOrder = new ModelBomb(this, p_commandEntered.split(" ")[1]);
                    if (l_bombOrder.validOrderCheck(p_gameState)) {
                        this.order_list.add(l_bombOrder);
                        this.setD_playerLog("Card Command Added to Queue for Execution Successfully!", "log");
                        p_gameState.updateLog(getD_playerLog(), "effect");
                    }
                    break;
                case "negotiate":
                    Card l_negotiateOrder = new Diplomacy(p_commandEntered.split(" ")[1], this);
                    if (l_negotiateOrder.validOrderCheck(p_gameState)) {
                        this.order_list.add(l_negotiateOrder);
                        this.setD_playerLog("Card Command Added to Queue for Execution Successfully!", "log");
                        p_gameState.updateLog(getD_playerLog(), "effect");
                    }
                    break;
                default:
                    this.setD_playerLog("Invalid Command!", "error");
                    p_gameState.updateLog(getD_playerLog(), "effect");
                    break;
            }
        } else {
            this.setD_playerLog("Invalid Card Command Passed! Check Arguments!", "error");
        }
    }


}
