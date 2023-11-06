package Services;

import java.util.ArrayList;
import java.util.List;

import Models.GameState;
import Models.ModelPlayer;
import Utils.CommonUtil;

public class PlayerService {
    String d_logAssignment = "Country/Continent Assignment:";
	
	String d_logPlayer;


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

	public List<ModelPlayer> addOrRemovePlayers(List<ModelPlayer> p_existingPlayerList, String p_operation, String p_argument) {
		List<ModelPlayer> l_updatedPlayers = new ArrayList<>();
		if (!CommonUtil.isNullOrEmptyCollection(p_existingPlayerList))
			l_updatedPlayers.addAll(p_existingPlayerList);

		String l_enteredPlayerName = p_argument.split(" ")[0];
		boolean l_playerNameAlreadyExist = !isNameUnique(p_existingPlayerList, l_enteredPlayerName);

		switch (p_operation.toLowerCase()) {
		case "add":
			addGamePlayer(l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExist);
			break;
		case "remove":
			removeGamePlayer(p_existingPlayerList, l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExist);
			break;
		default:
			setD_logPlayer("Invalid Operation on Players list");
		}
		return l_updatedPlayers;
	}

    public void setD_logPlayer(String p_playerLog) {
		this.d_logPlayer = p_playerLog;
		System.out.println(p_playerLog);
	}
}
