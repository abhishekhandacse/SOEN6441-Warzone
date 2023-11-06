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
	
	public boolean checkAvailability(GameState p_gameState) {
		if (p_gameState.getD_playersList() == null || p_gameState.getD_playersList().isEmpty()) {
			return false;
		}
		return true;
	}

	public void assignColors(GameState p_gameState) {
		if (!checkAvailability(p_gameState))
			return;

		List<ModelPlayer> l_players = p_gameState.getD_playersList();

		for (int i = 0; i < l_players.size(); i++) {
			l_players.get(i).setD_color(ApplicationConstantsHardcoding.ALL_COLORS.get(i));
		}
	}

	public boolean unexecutedOrdersExists(List<ModelPlayer> p_playersList) {
		int l_totalUnexecutedOrders = 0;
		for (ModelPlayer l_player : p_playersList) {
			l_totalUnexecutedOrders = l_totalUnexecutedOrders + l_player.getD_ordersToExecute().size();
		}
		return l_totalUnexecutedOrders != 0;
	}

	public boolean unassignedArmiesExists(List<ModelPlayer> p_playersList) {
		int l_unassignedArmies = 0;
		for (ModelPlayer l_player : p_playersList) {
			l_unassignedArmies = l_unassignedArmies + l_player.getD_noOfUnallocatedArmies();
		}
		return l_unassignedArmies != 0;
	}

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
