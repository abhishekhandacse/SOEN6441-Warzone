package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

public class BenevolentPlayer extends PlayerBehaviorStrategy{

    ArrayList<Country> d_deployCountriesList = new ArrayList<Country>();

    @Override
    public String getPlayerBehavior() {
        return "Benevolent";
    }

    @Override
    public String createOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
		String l_command;
		if (!checkIfArmiesDepoyed(p_modelPlayer)) {
			if(p_modelPlayer.getD_noOfUnallocatedArmies()>0) {
				l_command = createDeployOrder(p_modelPlayer, p_currentGameState);
			}else{
				l_command = createAdvanceOrder(p_modelPlayer, p_currentGameState);
			}
		} else {
			if(p_modelPlayer.getD_cardsOwnedByPlayer().size() > 0){
				System.out.println("Enter Card Logic");
				int l_index = (int) (Math.random() * 3) +1;

				switch (l_index) {
					case 1:
						System.out.println("Deploy!");
						l_command = createDeployOrder(p_modelPlayer, p_currentGameState);
						break;
					case 2:
						System.out.println("Advance!");
						l_command = createAdvanceOrder(p_modelPlayer, p_currentGameState);
						break;
					case 3:
						if (p_modelPlayer.getD_cardsOwnedByPlayer().size() == 1) {
							System.out.println("Cards!");
							l_command = createCardOrder(p_modelPlayer, p_currentGameState, p_modelPlayer.getD_cardsOwnedByPlayer().get(0));
							break;
						} else {
							Random l_random = new Random();
							int l_randomIndex = l_random.nextInt(p_modelPlayer.getD_cardsOwnedByPlayer().size());
							l_command = createCardOrder(p_modelPlayer, p_currentGameState, p_modelPlayer.getD_cardsOwnedByPlayer().get(l_randomIndex));
							break;
						}
					default:
						l_command = createAdvanceOrder(p_modelPlayer, p_currentGameState);
						break;
				}
			} else{
				Random l_random = new Random();
				Boolean l_randomBoolean = l_random.nextBoolean();
				if(l_randomBoolean){
					System.out.println("Without Deploy Card Logic");
					l_command = createDeployOrder(p_modelPlayer, p_currentGameState);
				}else{
					System.out.println("Without Advance Card Logic");
					l_command = createAdvanceOrder(p_modelPlayer, p_currentGameState);
				}
			}
		}
		return l_command;
	}

    @Override
    public String createCardOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState, String p_currentCardName) {
        int l_armiesToSend;
		Random l_random = new Random();
		Country l_randomOwnCountry = getRandomCountry(p_modelPlayer.getD_coutriesOwned());
		Country l_randomEnemyNeighbor = p_currentGameState.getD_map()
				.getCountry(randomEnemyNeighbor(p_modelPlayer, l_randomOwnCountry)
						.get(l_random.nextInt(randomEnemyNeighbor(p_modelPlayer, l_randomOwnCountry).size())));

		if (l_randomOwnCountry.getD_armies() > 1) {
			l_armiesToSend = l_random.nextInt(l_randomOwnCountry.getD_armies() - 1) + 1;
		} else {
			l_armiesToSend = 1;
		}

		switch (p_currentCardName) {
		case "bomb":
			System.err.println("I am benevolent player, I don't hurt anyone.");
			return "bomb" + " " + "false";
		case "blockade":
			return "blockade " + l_randomOwnCountry.getD_countryName();
		case "airlift":
			return "airlift " + l_randomOwnCountry.getD_countryName() + " "
					+ getRandomCountry(p_modelPlayer.getD_coutriesOwned()).getD_countryName() + " " + l_armiesToSend;
		case "negotiate":
			return "negotiate " + p_modelPlayer.getPlayerName();
		}
		return null;
    }

    @Override
    public String createAdvanceOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
        // advance on weakest country
		int l_armiesToSend;
		Random l_random = new Random();

		Country l_randomSourceCountry = getRandomCountry(d_deployCountriesList);
		System.out.println("Source country"+ l_randomSourceCountry.getD_countryName());
		Country l_weakestTargetCountry = getWeakestNeighbor(l_randomSourceCountry, p_currentGameState);
		System.out.println("Target Country"+l_weakestTargetCountry.getD_countryName());
		if (l_randomSourceCountry.getD_armies() > 1) {
			l_armiesToSend = l_random.nextInt(l_randomSourceCountry.getD_armies() - 1) + 1;
		} else {
			l_armiesToSend = 1;
		}

		System.out.println("advance " + l_randomSourceCountry.getD_countryName() + " "
				+ l_weakestTargetCountry.getD_countryName() + " " + l_armiesToSend);
		return "advance " + l_randomSourceCountry.getD_countryName() + " " + l_weakestTargetCountry.getD_countryName()
				+ " " + l_armiesToSend;
    }

    @Override
    public String createDeployOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
        if (p_modelPlayer.getD_noOfUnallocatedArmies()>0) {
			Country l_weakestCountry = getWeakestCountry(p_modelPlayer);
			d_deployCountriesList.add(l_weakestCountry);

			Random l_random = new Random();
			int l_armiesToDeploy = l_random.nextInt(p_modelPlayer.getD_noOfUnallocatedArmies()) + 1;

			System.out.println("deploy " + l_weakestCountry.getD_countryName() + " " + l_armiesToDeploy);
			return String.format("deploy %s %d", l_weakestCountry.getD_countryName(), l_armiesToDeploy);
		}else{
			return createAdvanceOrder(p_modelPlayer, p_currentGameState);
		}
    }

    private Boolean checkIfArmiesDepoyed(ModelPlayer p_player){
		if(p_player.getD_coutriesOwned().stream().anyMatch(l_country -> l_country.getD_armies()>0)){
			return true;
		}
		return false;
	}

    private Country getRandomCountry(List<Country> p_listOfCountries) {
		Random l_random = new Random();
		return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
	}

    public Country getWeakestNeighbor(Country l_randomSourceCountry, GameState p_gameState) {
		List<Integer> l_adjacentCountryIds = l_randomSourceCountry.getD_adjacentCountryIds();
		List<Country> l_listOfNeighbors = new ArrayList<Country>();
		for (int l_index = 0; l_index < l_adjacentCountryIds.size(); l_index++) {
			Country l_country = p_gameState.getD_map()
					.getCountry(l_randomSourceCountry.getD_adjacentCountryIds().get(l_index));
			l_listOfNeighbors.add(l_country);
		}
		Country l_Country = calculateWeakestCountry(l_listOfNeighbors);

		return l_Country;
	}

	private ArrayList<Integer> randomEnemyNeighbor(ModelPlayer p_player, Country p_country) {
		ArrayList<Integer> l_enemyNeighbors = new ArrayList<Integer>();

		for (Integer l_countryID : p_country.getD_adjacentCountryIds()) {
			if (!p_player.getCountryIDs().contains(l_countryID))
				l_enemyNeighbors.add(l_countryID);
		}
		return l_enemyNeighbors;
	}

    public Country getWeakestCountry(ModelPlayer p_player) {
		List<Country> l_countriesOwnedByPlayer = p_player.getD_coutriesOwned();
		Country l_Country = calculateWeakestCountry(l_countriesOwnedByPlayer);
		return l_Country;
	}

    public Country calculateWeakestCountry(List<Country> l_listOfCountries) {
		LinkedHashMap<Country, Integer> l_CountryWithArmies = new LinkedHashMap<Country, Integer>();

		int l_smallestNoOfArmies;
		Country l_Country = null;

		// return weakest country from owned countries of player.
		for (Country l_country : l_listOfCountries) {
			l_CountryWithArmies.put(l_country, l_country.getD_armies());
		}
		l_smallestNoOfArmies = Collections.min(l_CountryWithArmies.values());
		for (Entry<Country, Integer> entry : l_CountryWithArmies.entrySet()) {
			if (entry.getValue().equals(l_smallestNoOfArmies)) {
				return entry.getKey();
			}
		}
		return l_Country;

	}
    
}
