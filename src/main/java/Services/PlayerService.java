package Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Constants.ApplicationConstantsHardcoding;
import Models.Continent;
import Models.GameState;
import Models.ModelCountry;
import Models.ModelPlayer;
import Utils.CommonUtil;

/**
 * PlayerService class
 */
public class PlayerService {
    String d_logAssignment = "Country/Continent Assignment:";
	
    /**
     * The Console logger.
     */
	String d_logPlayer;

    /**
     * Add a new player to the list of updated players.
     *
     * @param p_updatedPlayers         The list of players to which the new player is added.
     * @param p_enteredPlayerName      The name of the player to be added.
     * @param p_playerNameAlreadyExist Whether the player's name already exists.
     */
	private void addGamePlayer(List<ModelPlayer> p_updatedPlayers, String p_enteredPlayerName,
							   boolean p_playerNameAlreadyExist) {

		if (p_playerNameAlreadyExist) {
			setD_logPlayer("Player with name : " + p_enteredPlayerName + " already Exists. Changes are not made.");
		} else {
			ModelPlayer l_addNewPlayer = new ModelPlayer(p_enteredPlayerName);
			p_updatedPlayers.add(l_addNewPlayer);
			setD_logPlayer("Player with name : " + p_enteredPlayerName + " has been added successfully.");
		}
	}

    /**
     * Remove a player from the list of existing players.
     *
     * @param p_existingPlayerList     List of existing players.
     * @param p_updatedPlayers         The list of players with the player removed.
     * @param p_enteredPlayerName      The name of the player to be removed.
     * @param p_playerNameAlreadyExist Whether the player's name already exists.
     */
	private void removeGamePlayer(List<ModelPlayer> p_existingPlayerList, List<ModelPlayer> p_updatedPlayers,
								  String p_enteredPlayerName, boolean p_playerNameAlreadyExist) {
		if (p_playerNameAlreadyExist) {
			for (ModelPlayer l_player : p_existingPlayerList) {
				if (l_player.getPlayerName().equalsIgnoreCase(p_enteredPlayerName)) {
					p_updatedPlayers.remove(l_player);
					setD_logPlayer("Player with name : " + p_enteredPlayerName + " has been removed successfully.");
				}
			}
		} else {
			setD_logPlayer("Player with name : " + p_enteredPlayerName + " does not Exist. Changes are not made.");
		}
	}

    /**
     * Update players in the game state.
     *
     * @param p_gameState The game state in which players are updated.
     * @param p_operation The operation to perform (add or remove).
     * @param p_argument  The player name to add or remove.
     */
	public void updatePlayers(GameState p_gameState, String p_operation, String p_argument) {
		if (!isMapLoaded(p_gameState)) {
			this.setD_logPlayer("Kindly load the map first to add player: " + p_argument);
			p_gameState.updateLog(this.d_logPlayer, "effect");
			return;
		}
		List<ModelPlayer> l_updatedPlayers = this.addOrRemovePlayers(p_gameState.getD_playersList(), p_operation, p_argument);

		if (!CommonUtil.isNullObject(l_updatedPlayers)) {
			p_gameState.setD_playersList(l_updatedPlayers);
			p_gameState.updateLog(d_logPlayer, "effect");
		}
	}

     /**
     * Check if a player name is unique among existing players.
     *
     * @param p_existingPlayerList List of existing players.
     * @param p_playerName         The player name to check for uniqueness.
     * @return True if the player name is unique; false otherwise.
     */
	public boolean isNameUnique(List<ModelPlayer> p_existingPlayerList, String p_playerName) {
		boolean l_isUnique = true;
		if (!CommonUtil.isNullOrEmptyCollection(p_existingPlayerList)) {
			for (ModelPlayer l_player : p_existingPlayerList) {
				if (l_player.getPlayerName().equalsIgnoreCase(p_playerName)) {
					l_isUnique = false;
					break;
				}
			}
		}
		return l_isUnique;
	}

    /**
     * Add or remove players from the list of existing players.
     *
     * @param p_existingPlayerList List of existing players.
     * @param p_operation          The operation to perform (add or remove).
     * @param p_argument           The player name to add or remove.
     * @return The updated list of players.
     */
	public List<ModelPlayer> addOrRemovePlayers(List<ModelPlayer> p_existingPlayerList, String p_operation, String p_argument) {
		// Create a new list to store the updated players.
        List<ModelPlayer> l_updatedPlayers = new ArrayList<>();

        // Check if the input list of existing players is not empty.
		if (!CommonUtil.isNullOrEmptyCollection(p_existingPlayerList))
			l_updatedPlayers.addAll(p_existingPlayerList);

        // Split the entered argument to get the player's name.
		String l_enteredPlayerName = p_argument.split(" ")[0];
        // Check if the player's name already exists in the existing player list.
		boolean l_playerNameAlreadyExist = !isNameUnique(p_existingPlayerList, l_enteredPlayerName);

        // Check the operation requested (add or remove).
		switch (p_operation.toLowerCase()) {
		case "add":
            // If the operation is 'add', add the player to the updated list.
			addGamePlayer(l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExist);
			break;
		case "remove":
            // If the operation is 'remove', remove the player from the updated list.
			removeGamePlayer(p_existingPlayerList, l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExist);
			break;
		default:
            // If an invalid operation is provided, log an error message.
			setD_logPlayer("Invalid Operation on Players list");
		}

        // Return the updated list of players.
		return l_updatedPlayers;
	}

    /**
     * Assign countries to players in the game state.
     *
     * @param p_gameState The game state in which countries are assigned to players.
     */
    public void assignCountries(GameState p_gameState) {
		if (!checkAvailability(p_gameState)){
			p_gameState.updateLog("Kindly add players before assigning countries",  "effect");
			return;
		}

		List<ModelCountry> l_countries = p_gameState.getD_map().getD_allCountries();
		int l_playerSize = p_gameState.getD_playersList().size();
		ModelPlayer l_neutralPlayer = p_gameState.getD_playersList().stream()
				.filter(l_player -> l_player.getPlayerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);
		if (l_neutralPlayer != null)
			l_playerSize = l_playerSize - 1;
		int l_countriesPerPlayer = Math.floorDiv(l_countries.size(), l_playerSize);

		this.performCountryAssignmentRandomly(l_countriesPerPlayer, l_countries, p_gameState.getD_playersList(), p_gameState);
		this.performAssignContinent(p_gameState.getD_playersList(), p_gameState.getD_map().getD_allContinents());
		p_gameState.updateLog(d_logAssignment, "effect");
		System.out.println("Countries have been assigned to Players.");

	}

    /**
     * Perform assignment of continents to players based on the countries they own.
     *
     * @param p_players    The list of players for whom continents are assigned.
     * @param p_continents The list of continents available for assignment.
     */
    public void performAssignContinent(List<ModelPlayer> p_players, List<Continent> p_continents) {
		for (ModelPlayer l_pl : p_players) {
			List<String> l_countriesOwned = new ArrayList<>();
			if (!CommonUtil.isNullOrEmptyCollection(l_pl.getD_coutriesOwned())) {
				l_pl.getD_coutriesOwned().forEach(l_country -> l_countriesOwned.add(l_country.getD_countryName()));

				for (Continent l_cont : p_continents) {
					List<String> l_countriesOfContinent = new ArrayList<>();
					l_cont.getD_countries().forEach(l_count -> l_countriesOfContinent.add(l_count.getD_countryName()));
					if (l_countriesOwned.containsAll(l_countriesOfContinent)) {
						if (l_pl.getD_continentsOwned() == null)
							l_pl.setD_continentsOwned(new ArrayList<>());

						l_pl.getD_continentsOwned().add(l_cont);
						System.out.println("Player : " + l_pl.getPlayerName() + " is assigned with continent : "
								+ l_cont.getD_continentName());
						d_logAssignment += "\n Player : " + l_pl.getPlayerName() + " is assigned with continent : "
								+ l_cont.getD_continentName();
					}
				}
			}
		}
	}

    /**
     * Perform a random assignment of countries to players based on a specified number of countries per player.
     *
     * @param p_countriesPerPlayer The number of countries to assign to each player.
     * @param p_countries          The list of countries available for assignment.
     * @param p_players            The list of players to whom countries are assigned.
     */
	private void performCountryAssignmentRandomly(int p_countriesPerPlayer, List<ModelCountry> p_countries,
												List<ModelPlayer> p_players, GameState p_gameState) {
		List<ModelCountry> l_unassignedCountries = new ArrayList<>(p_countries);
		for (ModelPlayer l_pl : p_players) {
			if(!l_pl.getPlayerName().equalsIgnoreCase("Neutral")) {
				if (l_unassignedCountries.isEmpty())
					break;
				// Based on number of countries to be assigned to player, it generates random
				// country and assigns to player
				for (int i = 0; i < p_countriesPerPlayer; i++) {
					Random l_random = new Random();
					int l_randomIndex = l_random.nextInt(l_unassignedCountries.size());
					ModelCountry l_randomCountry = l_unassignedCountries.get(l_randomIndex);

					if (l_pl.getD_coutriesOwned() == null)
						l_pl.setD_coutriesOwned(new ArrayList<>());
					l_pl.getD_coutriesOwned().add(p_gameState.getD_map().getCountryByName(l_randomCountry.getD_countryName()));
					System.out.println("Player : " + l_pl.getPlayerName() + " is assigned with country : "
							+ l_randomCountry.getD_countryName());
					d_logAssignment += "\n Player : " + l_pl.getPlayerName() + " is assigned with country : "
							+ l_randomCountry.getD_countryName();
					l_unassignedCountries.remove(l_randomCountry);
				}
			}	
		}
		// If any countries are still left for assignment, it will redistribute those
		// among players
		if (!l_unassignedCountries.isEmpty()) {
			performCountryAssignmentRandomly(1, l_unassignedCountries, p_players, p_gameState);
		}
	}
	
    /**
     * Check if there are players available in the game state.
     *
     * @param p_gameState The game state to check.
     * @return True if players are available; false otherwise.
     */
	public boolean checkAvailability(GameState p_gameState) {
		if (p_gameState.getD_playersList() == null || p_gameState.getD_playersList().isEmpty()) {
			return false;
		}
		return true;
	}

    /**
     * Assign colors to players in the game state.
     *
     * @param p_gameState The game state in which players' colors are assigned.
     */
	public void assignColors(GameState p_gameState) {
		if (!checkAvailability(p_gameState))
			return;

		List<ModelPlayer> l_players = p_gameState.getD_playersList();

		for (int i = 0; i < l_players.size(); i++) {
			l_players.get(i).setD_color(ApplicationConstantsHardcoding.ALL_COLORS.get(i));
		}
	}

    /**
     * Check if unexecuted orders exist for players.
     *
     * @param p_playersList The list of players to check for unexecuted orders.
     * @return True if unexecuted orders exist; false otherwise.
     */
	public boolean unexecutedOrdersExists(List<ModelPlayer> p_playersList) {
		int l_totalUnexecutedOrders = 0;
		for (ModelPlayer l_player : p_playersList) {
			l_totalUnexecutedOrders = l_totalUnexecutedOrders + l_player.getD_ordersToExecute().size();
		}
		return l_totalUnexecutedOrders != 0;
	}

    /**
     * Check if unassigned armies exist for players.
     *
     * @param p_playersList The list of players to check for unassigned armies.
     * @return True if unassigned armies exist; false otherwise.
     */
	public boolean unassignedArmiesExists(List<ModelPlayer> p_playersList) {
		int l_unassignedArmies = 0;
		for (ModelPlayer l_player : p_playersList) {
			l_unassignedArmies = l_unassignedArmies + l_player.getD_noOfUnallocatedArmies();
		}
		return l_unassignedArmies != 0;
	}

    /**
     * Check if a map is loaded in the game state.
     *
     * @param p_gameState The game state to check for a loaded map.
     * @return True if a map is loaded; false otherwise.
     */
	public boolean isMapLoaded(GameState p_gameState) {
		return !CommonUtil.isNullObject(p_gameState.getD_map()) ? true : false;
	}

	public boolean checkForMoreOrders(List<ModelPlayer> p_playersList) {
		for (ModelPlayer l_player : p_playersList) {
			if(l_player.getD_moreOrders())
				return true;
		}
		return false;
	}

	public void resetPlayersFlag(List<ModelPlayer> p_playersList) {
		for (ModelPlayer l_player : p_playersList) {
			if (!l_player.getPlayerName().equalsIgnoreCase("Neutral"))
				l_player.setD_moreOrders(true);
			l_player.setD_oneCardPerTurn(false);
			l_player.resetNegotiation();
		}
	}
	
	public Integer calculatePlayerArmies(ModelPlayer p_player) {
		Integer l_armies = null != p_player.getD_noOfUnallocatedArmies() ? p_player.getD_noOfUnallocatedArmies() : 0;
		if (!CommonUtil.isNullOrEmptyCollection(p_player.getD_coutriesOwned())) {
			l_armies = l_armies + Math.max(3, Math.round((p_player.getD_coutriesOwned().size()) / 3));
		}
		if (!CommonUtil.isNullOrEmptyCollection(p_player.getD_continentsOwned())) {
			int l_continentCtrlValue = 0;
			for (Continent l_continent : p_player.getD_continentsOwned()) {
				l_continentCtrlValue = l_continentCtrlValue + l_continent.getD_continentValue();
			}
			l_armies = l_armies + l_continentCtrlValue;
		}
		return l_armies;
	}

    /**
     * Assign armies to players in the game state.
     *
     * @param p_gameState The game state in which armies are assigned to players.
     */
	public void assignArmies(GameState p_gameState) {
		for (ModelPlayer l_pl : p_gameState.getD_playersList()) {
			Integer l_armies = this.calculatePlayerArmies(l_pl);
			this.setD_logPlayer("Player : " + l_pl.getPlayerName() + " has been assigned with " + l_armies + " armies");
			p_gameState.updateLog(this.d_logPlayer, "effect");

			l_pl.setD_noOfUnallocatedArmies(l_armies);
		}
	}

	public ModelPlayer findPlayerByName(String p_playerName, GameState p_gameState) {
		return p_gameState.getD_playersList().stream().filter(l_player -> l_player.getPlayerName().equals(p_playerName)).findFirst().orElse(null);
	}

    public void setD_logPlayer(String p_playerLog) {
		this.d_logPlayer = p_playerLog;
		System.out.println(p_playerLog);
	}
}
