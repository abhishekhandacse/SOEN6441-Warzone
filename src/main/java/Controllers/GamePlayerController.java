package Controllers;


import Logger.ConsoleLogger;
import Models.Continent;
import Models.Country;
import Modelss.*;
import Utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlayerController {
    ConsoleLogger consoleLogger = new ConsoleLogger();
	public boolean isPlayerNameUnique(List<Player> p_existingPlayerList, String p_playerName) {
		boolean l_isUnique = true;
		if (!CommonUtil.isCollectionEmpty(p_existingPlayerList)) {
			for (Player l_player : p_existingPlayerList) {
				if (l_player.getPlayerName().equalsIgnoreCase(p_playerName)) {
					l_isUnique = false;
					break;
				}
			}
		}
		return l_isUnique;
	}
    public List<Player> addRemovePlayers(List<Player> p_existingPlayerList, String p_operation, String p_argument) {
		// Create a new list to store the updated players.
		List<Player> l_updatedPlayers = new ArrayList<>();

		// Check if the input list of existing players is not empty.
		if (!CommonUtil.isCollectionEmpty(p_existingPlayerList)) {
			// If not empty, copy all players from the existing list to the updated list.
			l_updatedPlayers.addAll(p_existingPlayerList);
		}

		// Split the entered argument to get the player's name.
		String l_enteredPlayerName = p_argument.split(" ")[0];

		// Check if the player's name already exists in the existing player list.
		boolean l_playerNameAlreadyExist = !isPlayerNameUnique(p_existingPlayerList, l_enteredPlayerName);

		// Check the operation requested (add or remove).
		if ("add".equalsIgnoreCase(p_operation)) {
			// If the operation is 'add', add the player to the updated list.
			addGamePlayer(l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExist);
		} else if ("remove".equalsIgnoreCase(p_operation)) {
			// If the operation is 'remove', remove the player from the updated list.
			removeGamePlayer(p_existingPlayerList, l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExist);
		} else {
			// If an invalid operation is provided, log an error message.
			consoleLogger.writeLog("Invalid Operation on Players list");
		}

		// Return the updated list of players.
		return l_updatedPlayers;
	}



	private void removeGamePlayer(List<Player> p_existingPlayerList, List<Player> p_updatedPlayers,
								  String p_enteredPlayerName, boolean p_playerNameAlreadyExist) {
		if (p_playerNameAlreadyExist) {
			for (Player l_player : p_existingPlayerList) {
				if (l_player.getPlayerName().equalsIgnoreCase(p_enteredPlayerName)) {
					p_updatedPlayers.remove(l_player);
					consoleLogger.writeLog("Player with name : " + p_enteredPlayerName + " has been removed successfully.");
				}
			}
		} else {
			consoleLogger.writeLog("Player with name : " + p_enteredPlayerName + " does not Exist. Changes are not made.");
		}
	}


	private void addGamePlayer(List<Player> p_updatedPlayers, String p_enteredPlayerName,
							   boolean p_playerNameAlreadyExist) {
		if (p_playerNameAlreadyExist) {
			consoleLogger.writeLog("Player with name : " + p_enteredPlayerName + " already Exists. Changes are not made.");
		} else {
			Player l_addNewPlayer = new Player(p_enteredPlayerName);
			p_updatedPlayers.add(l_addNewPlayer);
			consoleLogger.writeLog("Player with name : " + p_enteredPlayerName + " has been added successfully.");
		}
	}

    public boolean checkPlayersAvailability(GameState p_gameState) {
		if (p_gameState.getD_players() == null || p_gameState.getD_players().isEmpty()) {
			consoleLogger.writeLog("Kindly add players before assigning countries");
			return false;
		}
		return true;
	}

	public static final String WHITE = "\u001B[47m";

	public void assignColors(GameState p_gameState){
		if (!checkPlayersAvailability(p_gameState)) return;

		List<Player> l_players = p_gameState.getD_players();

        for (Player lPlayer : l_players) {
            lPlayer.setD_color(WHITE);
        }
	}


	public void assignCountries(GameState p_gameState) {
		if (!checkPlayersAvailability(p_gameState))
			return;

		List<Country> l_countries = p_gameState.getD_map().getD_countries();
		int l_countriesPerPlayer = Math.floorDiv(l_countries.size(), p_gameState.getD_players().size());

		this.performRandomCountryAssignment(l_countriesPerPlayer, l_countries, p_gameState.getD_players());
		this.performContinentAssignment(p_gameState.getD_players(), p_gameState.getD_map().getD_continents());
		consoleLogger.writeLog("Countries have been assigned to Players.");

	}
    private void performRandomCountryAssignment(int p_countriesPerPlayer, List<Country> p_countries,
												List<Player> p_players) {
		// Create a list to store unassigned countries, initially containing all countries.
		List<Country> l_unassignedCountries = new ArrayList<>(p_countries);

		// Loop through each player to assign countries.
		for (Player l_pl : p_players) {
			// Check if there are no unassigned countries left, and exit the loop if so.
			if (l_unassignedCountries.isEmpty())
				break;

			// Iterate for the specified number of countries per player.
			for (int i = 0; i < p_countriesPerPlayer; i++) {
				// Generate a random index to pick a random country from the unassigned list.
				Random l_random = new Random();
				int l_randomIndex = l_random.nextInt(l_unassignedCountries.size());
				Country l_randomCountry = l_unassignedCountries.get(l_randomIndex);

				// Check if the player's list of owned countries is null, and create it if needed.
				if (l_pl.getD_coutriesOwned() == null)
					l_pl.setD_coutriesOwned(new ArrayList<>());

				// Add the randomly selected country to the player's list of owned countries.
				l_pl.getD_coutriesOwned().add(l_randomCountry);

				// Log the assignment of the country to the player.
				consoleLogger.writeLog("Player : " + l_pl.getPlayerName() + " is assigned with country : "
						+ l_randomCountry.getD_countryName());

				// Remove the assigned country from the list of unassigned countries.
				l_unassignedCountries.remove(l_randomCountry);
			}
		}

		// If there are still unassigned countries left, redistribute them among players.
		if (!l_unassignedCountries.isEmpty()) {
			// Recursively call the function to distribute one country to each player.
			performRandomCountryAssignment(1, l_unassignedCountries, p_players);
		}
	}

    private void performContinentAssignment(List<Player> p_players, List<Continent> p_continents) {
		// Iterate through each player.
		for (Player l_pl : p_players) {
			// Create a list to store the names of countries owned by the player.
			List<String> l_countriesOwned = new ArrayList<>();

			// Check if the player owns any countries.
			if (!CommonUtil.isCollectionEmpty(l_pl.getD_coutriesOwned())) {
				// Iterate through the countries owned by the player and add their names to the list.
				l_pl.getD_coutriesOwned().forEach(l_country -> l_countriesOwned.add(l_country.getD_countryName()));

				// Iterate through each continent.
				for (Continent l_cont : p_continents) {
					// Create a list to store the names of countries in the current continent.
					List<String> l_countriesOfContinent = new ArrayList<>();

					// Iterate through the countries in the continent and add their names to the list.
					l_cont.getD_countries().forEach(l_count -> l_countriesOfContinent.add(l_count.getD_countryName()));

					// Check if the player owns all countries in the current continent.
					if (l_countriesOwned.containsAll(l_countriesOfContinent)) {
						// If the player owns all countries in the continent, assign the continent to the player.
						if (l_pl.getD_continentsOwned() == null)
							l_pl.setD_continentsOwned(new ArrayList<>());

						// Add the continent to the player's list of owned continents.
						l_pl.getD_continentsOwned().add(l_cont);

						// Log the assignment of the continent to the player.
						consoleLogger.writeLog("Player : " + l_pl.getPlayerName() + " is assigned with continent : "
								+ l_cont.getD_continentName());
					}
				}
			}
		}
	}
    public void createDeployOrder(String p_commandEntered, Player p_player) {
		// Retrieve the list of orders from the player or create a new list if it doesn't exist.
		List<Order> l_orders = CommonUtil.isCollectionEmpty(p_player.getD_ordersToExecute()) ? new ArrayList<>()
				: p_player.getD_ordersToExecute();

		// Split the input command to extract relevant information.
		String l_countryName = p_commandEntered.split(" ")[1];
		String l_noOfArmies = p_commandEntered.split(" ")[2];

		// Check if the deploy order has valid armies that can be executed.
		if (validateDeployOrderArmies(p_player, l_noOfArmies)) {
			// Log an error message if the deploy order cannot be executed due to insufficient armies.
			consoleLogger.writeLog("Given deploy order can't be executed as armies in deploy order exceed player's unallocated armies");
		} else {
			// Create an Order object based on the command and add it to the list of orders.
			Order l_orderObject = new Order(p_commandEntered.split(" ")[0], l_countryName,
					Integer.parseInt(l_noOfArmies));
			l_orders.add(l_orderObject);

			// Update the player's list of orders to execute.
			p_player.setD_ordersToExecute(l_orders);

			// Deduct the allocated armies from the player's unallocated armies.
			Integer l_unallocatedArmies = p_player.getD_noOfUnallocatedArmies() - Integer.parseInt(l_noOfArmies);
			p_player.setD_noOfUnallocatedArmies(l_unallocatedArmies);

			// Log a success message indicating the order has been added for execution.
			consoleLogger.writeLog("Order has been added to queue for execution.");
		}
	}



	public boolean validateDeployOrderArmies(Player p_player, String p_noOfArmies) {
		return p_player.getD_noOfUnallocatedArmies() < Integer.parseInt(p_noOfArmies);
	}


	public int calculateArmiesForPlayer(Player p_player) {
		int l_armies = 0;
		if (!CommonUtil.isCollectionEmpty(p_player.getD_coutriesOwned())) {
			l_armies = Math.max(3, Math.round((float) (p_player.getD_coutriesOwned().size()) / 3));
		}
		if (!CommonUtil.isCollectionEmpty(p_player.getD_continentsOwned())) {
			int l_continentCtrlValue = 0;
			for (Continent l_continent : p_player.getD_continentsOwned()) {
				l_continentCtrlValue = l_continentCtrlValue + l_continent.getD_continentValue();
			}
			l_armies = l_armies + l_continentCtrlValue;
		}
		return l_armies;
	}


	public void assignArmies(GameState p_gameState) {
		for (Player l_pl : p_gameState.getD_players()) {
			Integer l_armies = this.calculateArmiesForPlayer(l_pl);
			consoleLogger.writeLog("Player : " + l_pl.getPlayerName() + " has been assigned with " + l_armies + " armies");
			l_pl.setD_noOfUnallocatedArmies(l_armies);
		}
	}


	public boolean unexecutedOrdersExists(List<Player> p_playersList) {
		int l_totalUnexecutedOrders = 0;
		for (Player l_player : p_playersList) {
			l_totalUnexecutedOrders = l_totalUnexecutedOrders + l_player.getD_ordersToExecute().size();
		}
		return l_totalUnexecutedOrders != 0;
	}


	public boolean unassignedArmiesExists(List<Player> p_playersList) {
		int l_unassignedArmies = 0;
		for (Player l_player : p_playersList) {
			l_unassignedArmies = l_unassignedArmies + l_player.getD_noOfUnallocatedArmies();
		}
		return l_unassignedArmies != 0;
	}


	public void updatePlayers(GameState p_gameState, String p_operation, String p_argument) {
		if (!isMapLoaded(p_gameState)) {
			consoleLogger.writeLog("Kindly load the map first to add player: " + p_argument);
			return;
		}
		List<Player> l_updatedPlayers = this.addRemovePlayers(p_gameState.getD_players(), p_operation, p_argument);

		if (!CommonUtil.isNull(l_updatedPlayers)) {
			p_gameState.setD_players(l_updatedPlayers);
		}
	}


	public boolean isMapLoaded(GameState p_gameState) {
		return !CommonUtil.isNull(p_gameState.getD_map());
	}
}
