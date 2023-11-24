
package Models;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Services.PlayerService;

public class CheaterPlayer extends PlayerBehaviorStrategy {


    @Override
    public String createOrder(ModelPlayer p_player, GameState p_stateOfGame) throws IOException {

        if(p_player.getD_noOfUnallocatedArmies() != 0) {
            while(p_player.getD_noOfUnallocatedArmies() > 0) {
                Random l_randomNum = new Random();
                Country l_randomCountry = getRandomCountry(p_player.getD_coutriesOwned());
                int l_armiesToBeDeploy = l_randomNum.nextInt(p_player.getD_noOfUnallocatedArmies()) + 1;

                l_randomCountry.setD_armies(l_armiesToBeDeploy);
                p_player.setD_noOfUnallocatedArmies(p_player.getD_noOfUnallocatedArmies() - l_armiesToBeDeploy);

                String l_logMessage = "Cheating Player: " + p_player.getPlayerName() +
                        " assigned " + l_armiesToBeDeploy +
                        " armies to  " + l_randomCountry.getD_countryName();

                p_stateOfGame.updateLog(l_logMessage, "effect");
            }
        }

        conquerNeighboringEnemies(p_player, p_stateOfGame);
        doubleArmyOnEnemyNeighboredCounties(p_player, p_stateOfGame);

        p_player.checkForMoreOrders(true);
        return null;
    }


    private void doubleArmyOnEnemyNeighboredCounties(ModelPlayer p_player, GameState p_gameState){
        List<Country> l_countriesOwned = p_player.getD_coutriesOwned();

        for(Country l_ownedCountry : l_countriesOwned) {
            ArrayList<Integer> l_countryEnemies = getEnemies(p_player, l_ownedCountry);

            if(l_countryEnemies.size() == 0) continue;

            Integer l_armiesInTerritory = l_ownedCountry.getD_armies();

            if(l_armiesInTerritory == 0) continue;

            l_ownedCountry.setD_armies(l_armiesInTerritory*2);

            String l_logMessage = "Cheater Player: " + p_player.getPlayerName() +
                    " doubled the armies ( Now: " + l_armiesInTerritory*2 +
                    ") in " + l_ownedCountry.getD_countryName();

            p_gameState.updateLog(l_logMessage, "effect");

        }
    }

    private Country getRandomCountry(List<Country> p_listOfCountries){
        Random l_random = new Random();
        return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
    }


    private ModelPlayer getCountryOwner(GameState p_gameState, Integer p_countryId){
        List<ModelPlayer> l_players = p_gameState.getD_players();
        ModelPlayer l_owner = null;

        for(ModelPlayer l_player: l_players){
            List<Integer> l_countriesOwned = l_player.getCountryIDs();
            if(l_countriesOwned.contains(p_countryId)){
                l_owner = l_player;
                break;
            }
        }

        return l_owner;
    }

    private void conquerTargetCountry(GameState p_gameState, ModelPlayer p_targetCPlayer, ModelPlayer p_cheaterPlayer, Country p_targetCountry) {
        p_targetCPlayer.getD_coutriesOwned().remove(p_targetCountry);
        p_targetCPlayer.getD_coutriesOwned().add(p_targetCountry);
        // Add Log Here
        this.updateContinents(p_cheaterPlayer, p_targetCPlayer, p_gameState);
    }

    private void conquerNeighboringEnemies(ModelPlayer p_player, GameState p_gameState){
        List<Country> l_countriesOwned = p_player.getD_coutriesOwned();

        for(Country l_ownedCountry : l_countriesOwned) {
            ArrayList<Integer> l_countryEnemies = getEnemies(p_player, l_ownedCountry);

            for(Integer l_enemyId: l_countryEnemies) {
                Map l_loadedMap =  p_gameState.getD_map();
                ModelPlayer l_enemyCountryOwner = this.getCountryOwner(p_gameState, l_enemyId);
                Country l_enemyCountry = l_loadedMap.getCountryByID(l_enemyId);
                this.conquerTargetCountry(p_gameState, l_enemyCountryOwner ,p_player, l_enemyCountry);

                String l_logMessage = "Cheater Player: " + p_player.getPlayerName() +
                        " Now owns " + l_enemyCountry.getD_countryName();

                p_gameState.updateLog(l_logMessage, "effect");
            }

        }
    }



    @Override
    public String createDeployOrder(ModelPlayer p_player, GameState p_gameState) {
        return null;
    }

    @Override
    public String createAdvanceOrder(ModelPlayer p_player, GameState p_gameState) {
        return null;
    }


    @Override
    public String createCardOrder(ModelPlayer p_player, GameState p_gameState, String p_cardName) {
        return null;
    }

    @Override
    public String getPlayerBehavior() {
        return "Cheater";
    }

    private void updateContinents(ModelPlayer p_cheaterPlayer, ModelPlayer p_targetCPlayer,
                                  GameState p_gameState) {
        List<ModelPlayer> l_listOfPlayers = new ArrayList<>();
        p_cheaterPlayer.setD_continentsOwned(new ArrayList<>());
        p_targetCPlayer.setD_continentsOwned(new ArrayList<>());
        l_listOfPlayers.add(p_cheaterPlayer);
        l_listOfPlayers.add(p_targetCPlayer);

        PlayerService l_playerService = new PlayerService();
        l_playerService.performContinentAssignment(l_listOfPlayers, p_gameState.getD_map().getD_continents());
    }

    private ArrayList<Integer> getEnemies(ModelPlayer p_player, Country p_country){
        ArrayList<Integer> l_enemyNeighbors = new ArrayList<>();

        for(Integer l_countryID : p_country.getD_adjacentCountryIds()){
            if(!p_player.getCountryIDs().contains(l_countryID))
                l_enemyNeighbors.add(l_countryID);
        }
        return l_enemyNeighbors;
    }
}
