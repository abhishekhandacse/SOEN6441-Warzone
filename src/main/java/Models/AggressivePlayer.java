package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

/**
 * This is the class of Aggressive Player, who gathers all his armies, attacks
 * from his strongest territory and deploys armies to maximize his forces on one
 * country.
 *
 */
public class AggressivePlayer extends PlayerBehaviorStrategy {

    /**
     * List containing deploy order countries.
     */
    ArrayList<Country> d_countriesToDeploy = new ArrayList<Country>();

    /**
     * This method creates a new order.
     *
     * @param p_modelPlayer    object of Player class
     * @param p_currentGameState object of GameState class
     *
     * @return String form of order
     */
    @Override
    public String createOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
        String l_command;

        if (p_modelPlayer.getD_noOfUnallocatedArmies() > 0) {
            l_command = createDeployOrder(p_modelPlayer, p_currentGameState);
        } else {
            if (p_modelPlayer.getD_cardsOwnedByPlayer().size() > 0) {
                Random l_random = new Random();
                int l_randomIndex = l_random.nextInt(p_modelPlayer.getD_cardsOwnedByPlayer().size() + 1);
                if (l_randomIndex == p_modelPlayer.getD_cardsOwnedByPlayer().size()) {
                    l_command = createAdvanceOrder(p_modelPlayer, p_currentGameState);
                } else {
                    l_command = createCardOrder(p_modelPlayer, p_currentGameState,
                            p_modelPlayer.getD_cardsOwnedByPlayer().get(l_randomIndex));
                }
            } else {
                l_command = createAdvanceOrder(p_modelPlayer, p_currentGameState);
            }
        }
        return l_command;
    }

    /**
     * Move armies from neighbor to maximize aggregation of forces.
     *
     * @param p_modelPlayer              Player
     * @param p_randomSourceCountry Source country
     * @param p_gameState           Game state
     */
    public void moveVariousArmiesFromItsNeighbors(ModelPlayer p_modelPlayer, Country p_randomSourceCountry, GameState p_gameState) {
        List<Integer> l_adjacentCountryIds = p_randomSourceCountry.getD_adjacentCountryIds();
        List<Country> l_listOfNeighbors = new ArrayList<Country>();
        for (int l_index = 0; l_index < l_adjacentCountryIds.size(); l_index++) {
            Country l_country = p_gameState.getD_map()
                    .getCountry(p_randomSourceCountry.getD_adjacentCountryIds().get(l_index));
            // check if neighbor belongs to player and then add to list
            if (p_modelPlayer.getD_coutriesOwned().contains(l_country)) {
                l_listOfNeighbors.add(l_country);
            }
        }

        int l_ArmiesToMove = 0;
        // send armies from neighbor to source country
        for (Country l_con : l_listOfNeighbors) {
            l_ArmiesToMove += p_randomSourceCountry.getD_armies() > 0
                    ? p_randomSourceCountry.getD_armies() + (l_con.getD_armies())
                    : (l_con.getD_armies());

        }
        p_randomSourceCountry.setD_armies(l_ArmiesToMove);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createDeployOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
        Random l_Random = new Random();
        // get strongest country then deploy
        Country l_strongestCountry = getStrongestCountry(p_modelPlayer, d_currentGameState);
        d_countriesToDeploy.add(l_strongestCountry);
        int l_armiesToDeploy = l_Random.nextInt(p_modelPlayer.getD_noOfUnallocatedArmies()) + 1;
        return String.format("deploy %s %d", l_strongestCountry.getD_countryName(), l_armiesToDeploy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createAdvanceOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
        // move armies from its neighbors to maximize armies on source country
        Country l_randomSourceCountry = getRandomCountry(d_countriesToDeploy);
        moveVariousArmiesFromItsNeighbors(p_modelPlayer, l_randomSourceCountry, p_currentGameState);

        Random l_random = new Random();
        Country l_randomTargetCountry = p_currentGameState.getD_map()
                .getCountry(l_randomSourceCountry.getD_adjacentCountryIds()
                        .get(l_random.nextInt(l_randomSourceCountry.getD_adjacentCountryIds().size())));

        int l_armiesToSend = l_randomSourceCountry.getD_armies() > 1 ? l_randomSourceCountry.getD_armies() : 1;

        // attacks with strongest country
        return "advance " + l_randomSourceCountry.getD_countryName() + " " + l_randomTargetCountry.getD_countryName()
                + " " + l_armiesToSend;

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
     * Get random enemy player.
     *
     * @param p_modelPlayer    Player
     * @param p_currentGameState Game state
     * @return random enemy player
     */
    private ModelPlayer getRandomEnemyPlayer(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
        ArrayList<ModelPlayer> l_playerList = new ArrayList<ModelPlayer>();
        Random l_random = new Random();

        for (ModelPlayer l_player : p_currentGameState.getD_players()) {
            if (!l_player.equals(p_modelPlayer))
                l_playerList.add(p_modelPlayer);
        }
        return l_playerList.get(l_random.nextInt(l_playerList.size()));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String createCardOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState, String p_currentCardName) {
        Random l_random = new Random();
        Country l_StrongestSourceCountry = getStrongestCountry(p_modelPlayer, d_currentGameState);

        Country l_randomTargetCountry = p_currentGameState.getD_map()
                .getCountry(l_StrongestSourceCountry.getD_adjacentCountryIds()
                        .get(l_random.nextInt(l_StrongestSourceCountry.getD_adjacentCountryIds().size())));

        ModelPlayer l_randomPlayer = getRandomEnemyPlayer(p_modelPlayer, p_currentGameState);

        int l_armiesToSend = l_StrongestSourceCountry.getD_armies() > 1 ? l_StrongestSourceCountry.getD_armies() : 1;

        switch (p_currentCardName) {
            case "bomb":
                return "bomb " + l_randomTargetCountry.getD_countryName();
            case "blockade":
                return "blockade " + l_StrongestSourceCountry.getD_countryName();
            case "airlift":
                return "airlift " + l_StrongestSourceCountry.getD_countryName() + " "
                        + getRandomCountry(p_modelPlayer.getD_coutriesOwned()).getD_countryName() + " " + l_armiesToSend;
            case "negotiate":
                return "negotiate" + " " + l_randomPlayer;
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
        return "Aggressive";
    }


    /**
     * This method calculates strongest country.
     *
     * @param l_listOfAllCountries List of countries
     * @return strongest country
     */
    public Country calculateStrongestCountry(List<Country> l_listOfAllCountries) {
        LinkedHashMap<Country, Integer> l_CountryWithArmies = new LinkedHashMap<Country, Integer>();

        int l_largestNoOfArmies;
        Country l_Country = null;
        // return strongest country from owned countries of player.
        for (Country l_country : l_listOfAllCountries) {
            l_CountryWithArmies.put(l_country, l_country.getD_armies());
        }
        l_largestNoOfArmies = Collections.max(l_CountryWithArmies.values());
        for (Entry<Country, Integer> entry : l_CountryWithArmies.entrySet()) {
            if (entry.getValue().equals(l_largestNoOfArmies)) {
                return entry.getKey();
            }
        }
        return l_Country;

    }

    /**
     * Get strongest country.
     *
     * @param p_modelPlayer    Player
     * @param p_currentGameState Game state
     * @return Strongest country
     */
    public Country getStrongestCountry(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
        List<Country> l_countriesOwnedByPlayer = p_modelPlayer.getD_coutriesOwned();
        Country l_Country = calculateStrongestCountry(l_countriesOwnedByPlayer);
        return l_Country;
    }


}