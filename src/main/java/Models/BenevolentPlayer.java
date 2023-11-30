package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import Utils.CommonUtil;

/**
 * This is the class of Benevolent ModelPlayer who focuses only on defending his own
 * countries and will never attack.
 *
 */
public class BenevolentPlayer extends PlayerBehaviorStrategy {

	/**
	 * List containing deploy order countries.
	 */
	ArrayList<Country> d_deployCountries = new ArrayList<Country>();

	/**
	 * This method creates a new order.
	 * 
	 * @param p_player    object of ModelPlayer class
	 * @param p_gameState object of GameState class
	 * 
	 * @return Order object of order class
	 */
	@Override
	public String createOrder(ModelPlayer p_player, GameState p_gameState) {
		System.out.println("Creating order for : " + p_player.getPlayerName());
		String l_command;
		if (!checkIfArmiesDepoyed(p_player)) {
			if(p_player.getD_noOfUnallocatedArmies()>0) {
				l_command = createDeployOrder(p_player, p_gameState);
			}else{
				l_command = createAdvanceOrder(p_player, p_gameState);
			}
		} else {
			if(p_player.getD_cardsOwnedByPlayer().size()>0){
				System.out.println("Enters Card Logic");
				int l_index = (int) (Math.random() * 3) +1;
				switch (l_index) {
					case 1:
						System.out.println("Deploy!");
						l_command = createDeployOrder(p_player, p_gameState);
						break;
					case 2:
						System.out.println("Advance!");
						l_command = createAdvanceOrder(p_player, p_gameState);
						break;
					case 3:
						if (p_player.getD_cardsOwnedByPlayer().size() == 1) {
							System.out.println("Cards!");
							l_command = createCardOrder(p_player, p_gameState, p_player.getD_cardsOwnedByPlayer().get(0));
							break;
						} else {
							Random l_random = new Random();
							int l_randomIndex = l_random.nextInt(p_player.getD_cardsOwnedByPlayer().size());
							l_command = createCardOrder(p_player, p_gameState, p_player.getD_cardsOwnedByPlayer().get(l_randomIndex));
							break;
						}
					default:
						l_command = createAdvanceOrder(p_player, p_gameState);
						break;
				}
			} else{
				Random l_random = new Random();
				Boolean l_randomBoolean = l_random.nextBoolean();
				if(l_randomBoolean){
					System.out.println("Without Card Deploy Logic");
					l_command = createDeployOrder(p_player, p_gameState);
				}else{
					System.out.println("Without Card Advance Logic");
					l_command = createAdvanceOrder(p_player, p_gameState);
				}
			}
		}
		return l_command;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createDeployOrder(ModelPlayer p_player, GameState p_gameState) {
		if (p_player.getD_noOfUnallocatedArmies()>0) {
			Country l_weakestCountry = getWeakestCountry(p_player);
			d_deployCountries.add(l_weakestCountry);

			Random l_random = new Random();
			int l_armiesToDeploy = 1;
			if (p_player.getD_noOfUnallocatedArmies()>1) {
				l_armiesToDeploy = l_random.nextInt(p_player.getD_noOfUnallocatedArmies() - 1) + 1;
			}
			System.out.println("deploy " + l_weakestCountry.getD_countryName() + " " + l_armiesToDeploy);
			return String.format("deploy %s %d", l_weakestCountry.getD_countryName(), l_armiesToDeploy);
		}else{
			return createAdvanceOrder(p_player, p_gameState);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createAdvanceOrder(ModelPlayer p_player, GameState p_gameState) {
		// advance on weakest country
		int l_armiesToSend;
		Random l_random = new Random();

		Country l_randomSourceCountry = getRandomCountry(d_deployCountries);
		System.out.println("Source country : "+ l_randomSourceCountry.getD_countryName());
		
		Country l_weakestTargetCountry = getWeakestNeighbor(l_randomSourceCountry, p_gameState, p_player);
		if(l_weakestTargetCountry == null)
			return null;
		
		System.out.println("Target Country : "+l_weakestTargetCountry.getD_countryName());
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createCardOrder(ModelPlayer p_player, GameState p_gameState, String p_cardName) {
		int l_armiesToSend;
		Random l_random = new Random();
		Country l_randomOwnCountry = getRandomCountry(p_player.getD_coutriesOwned());

		if (l_randomOwnCountry.getD_armies() > 1) {
			l_armiesToSend = l_random.nextInt(l_randomOwnCountry.getD_armies() - 1) + 1;
		} else {
			l_armiesToSend = 1;
		}

		switch (p_cardName) {
		case "bomb":
			System.err.println("I am benevolent player, I don't hurt anyone.");
			return "bomb" + " " + "false";
		case "blockade":
			return "blockade " + l_randomOwnCountry.getD_countryName();
		case "airlift":
			return "airlift " + l_randomOwnCountry.getD_countryName() + " "
					+ getRandomCountry(p_player.getD_coutriesOwned()).getD_countryName() + " " + l_armiesToSend;
		case "negotiate":
			return "negotiate " + getRandomEnemyPlayer(p_player, p_gameState).getPlayerName();
		}
		return null;
	}

	/**
	 * This method returns the player behavior.
	 * 
	 * @return String player behavior
	 */
	@Override
	public String getPlayerBehavior() {
		return "Benevolent";
	}

	/**
	 * This method returns random country.
	 * 
	 * @param p_listOfCountries list of countries
	 * @return return country
	 */
	private Country getRandomCountry(List<Country> p_listOfCountries) {
		Random l_random = new Random();
		return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
	}

	/**
	 * This method return weakest Country where benevolent player can deploy armies.
	 * 
	 * @param p_player ModelPlayer
	 * @return weakest country
	 */
	public Country getWeakestCountry(ModelPlayer p_player) {
		List<Country> l_countriesOwnedByPlayer = p_player.getD_coutriesOwned();
		Country l_Country = calculateWeakestCountry(l_countriesOwnedByPlayer);
		return l_Country;
	}

	/**
	 * This method return weakest neighbor where Source country can advance armies
	 * to this weakest country.
	 * 
	 * @param l_randomSourceCountry Source country
	 * @param p_gameState           GameState
	 * @param p_player benevolent player
	 * @return weakest neighbor
	 */
	public Country getWeakestNeighbor(Country l_randomSourceCountry, GameState p_gameState, ModelPlayer p_player) {
		List<Integer> l_adjacentCountryIds = l_randomSourceCountry.getD_adjacentCountryIds();
		List<Country> l_listOfNeighbors = new ArrayList<Country>();
		for (int l_index = 0; l_index < l_adjacentCountryIds.size(); l_index++) {
			Country l_country = p_gameState.getD_map()
					.getCountry(l_randomSourceCountry.getD_adjacentCountryIds().get(l_index));
			if(p_player.getD_coutriesOwned().contains(l_country))
				l_listOfNeighbors.add(l_country);
		}
		if(!CommonUtil.isCollectionEmpty(l_listOfNeighbors))
			return calculateWeakestCountry(l_listOfNeighbors);

		return null;
	}

	/**
	 * This method calculates weakest country.
	 * 
	 * @param l_listOfCountries list of countries
	 * @return weakest country
	 */
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
	
	/**
	 * Get random enemy player.
	 * 
	 * @param p_player    ModelPlayer
	 * @param p_gameState Gamestate
	 * @return ModelPlayer
	 */
	private ModelPlayer getRandomEnemyPlayer(ModelPlayer p_player, GameState p_gameState) {
		ArrayList<ModelPlayer> l_playerList = new ArrayList<ModelPlayer>();
		Random l_random = new Random();

		for (ModelPlayer l_player : p_gameState.getD_players()) {
			if (!l_player.equals(p_player))
				l_playerList.add(p_player);
		}
		return l_playerList.get(l_random.nextInt(l_playerList.size()));
	}

	/**
	 * Check if it is first turn.
	 *
	 * @param p_player player instance
	 * @return boolean
	 */
	private Boolean checkIfArmiesDepoyed(ModelPlayer p_player){
		if(p_player.getD_coutriesOwned().stream().anyMatch(l_country -> l_country.getD_armies()>0)){
			return true;
		}
		return false;
	}
}