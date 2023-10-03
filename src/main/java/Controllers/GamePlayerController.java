package Controllers;


import Logger.ConsoleLogger;
import Model.Country;
import Models.*;
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

}
