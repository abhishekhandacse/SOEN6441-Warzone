
package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RandomPlayer extends PlayerBehaviorStrategy {


    ArrayList<Country> d_countriesToBeDeployed = new ArrayList<>();


    @Override
    public String createDeployOrder(ModelPlayer p_player, GameState p_gameState){
        if (p_player.getD_noOfUnallocatedArmies()>0) {
            Random l_random = new Random();
            System.out.println(p_player.getD_coutriesOwned().size());
            Country l_randomCountry = getRandomCountry(p_player.getD_coutriesOwned());
            d_countriesToBeDeployed.add(l_randomCountry);
            int l_armiesToDeploy = l_random.nextInt(p_player.getD_noOfUnallocatedArmies()) + 1;

            return String.format("deploy %s %d", l_randomCountry.getD_countryName(), l_armiesToDeploy);
        } else {
            return createAdvanceOrder(p_player,p_gameState);
        }
    }

    @Override
    public String createOrder(ModelPlayer p_player, GameState p_gameState) {
        String l_command;
        if (!checkIfArmiesDeployed(p_player)) {
            if(p_player.getD_noOfUnallocatedArmies()>0) {
                l_command = createDeployOrder(p_player, p_gameState);
            }else{
                l_command = createAdvanceOrder(p_player, p_gameState);
            }
        } else {
            if(p_player.getD_cardsOwnedByPlayer().size()>0){
                int l_index = (int) (Math.random() * 3) +1;
                switch (l_index) {
                    case 1:
                        l_command = createDeployOrder(p_player, p_gameState);
                        break;
                    case 3:
                        if (p_player.getD_cardsOwnedByPlayer().size() == 1) {
                            l_command = createCardOrder(p_player, p_gameState, p_player.getD_cardsOwnedByPlayer().get(0));
                        } else {
                            Random l_random = new Random();
                            int l_randomIndex = l_random.nextInt(p_player.getD_cardsOwnedByPlayer().size());
                            l_command = createCardOrder(p_player, p_gameState, p_player.getD_cardsOwnedByPlayer().get(l_randomIndex));
                        }
                        break;
                    default:
                        l_command = createAdvanceOrder(p_player, p_gameState);
                        break;
                }
            } else{
                Random l_random = new Random();
                boolean l_randomBoolean = l_random.nextBoolean();
                if(l_randomBoolean){
                    l_command = createDeployOrder(p_player, p_gameState);
                }else{
                    l_command = createAdvanceOrder(p_player, p_gameState);
                }
            }
        }
        return l_command;
    }

    @Override
    public String createAdvanceOrder(ModelPlayer p_player, GameState p_gameState){
        int l_armiesToSend;
        Random l_random = new Random();
        Country l_randomOwnCountry = getRandomCountry(d_countriesToBeDeployed);
        int l_randomIndex = l_random.nextInt(l_randomOwnCountry.getD_adjacentCountryIds().size());
        Country l_randomNeighbor;
        if (l_randomOwnCountry.getD_adjacentCountryIds().size()>1) {
            l_randomNeighbor = p_gameState.getD_map().getCountry(l_randomOwnCountry.getD_adjacentCountryIds().get(l_randomIndex));
        } else {
            l_randomNeighbor = p_gameState.getD_map().getCountry(l_randomOwnCountry.getD_adjacentCountryIds().get(0));
        }

        if (l_randomOwnCountry.getD_armies()>1) {
            l_armiesToSend = l_random.nextInt(l_randomOwnCountry.getD_armies() - 1) + 1;
        } else {
            l_armiesToSend = 1;
        }
        return "advance "+l_randomOwnCountry.getD_countryName()+" "+l_randomNeighbor.getD_countryName()+" "+ l_armiesToSend;
    }


    @Override
    public String createCardOrder(ModelPlayer p_player, GameState p_gameState, String p_cardName){
        int l_armiesToSend;
        Random l_random = new Random();
        Country l_randomOwnCountry = getRandomCountry(p_player.getD_coutriesOwned());

        Country l_randomNeighbour = p_gameState.getD_map().getCountry(l_randomOwnCountry.getD_adjacentCountryIds().get(l_random.nextInt(l_randomOwnCountry.getD_adjacentCountryIds().size())));
        ModelPlayer l_randomPlayer = getRandomPlayer(p_player, p_gameState);

        if (l_randomOwnCountry.getD_armies()>1) {
            l_armiesToSend = l_random.nextInt(l_randomOwnCountry.getD_armies() - 1) + 1;
        } else {
            l_armiesToSend = 1;
        }
        switch(p_cardName){
            case "bomb":
                return "bomb "+ l_randomNeighbour.getD_countryName();
            case "blockade":
                return "blockade "+ l_randomOwnCountry.getD_countryName();
            case "airlift":
                return "airlift "+ l_randomOwnCountry.getD_countryName()+" "+getRandomCountry(p_player.getD_coutriesOwned()).getD_countryName()+" "+l_armiesToSend;
            case "negotiate":
                return "negotiate"+" "+l_randomPlayer.getPlayerName();
        }
        return null;
    }

    @Override
    public String getPlayerBehavior() {
        return "Random";
    }


    private Country getRandomCountry(List<Country> p_listOfCountries){
        Random l_random = new Random();
        return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
    }


    private Boolean checkIfArmiesDeployed(ModelPlayer p_player){
        return p_player.getD_coutriesOwned().stream().anyMatch(l_country -> l_country.getD_armies() > 0);
    }


    private ModelPlayer getRandomPlayer(ModelPlayer p_player, GameState p_gameState){
        ArrayList<ModelPlayer> l_playerList = new ArrayList<>();
        Random l_random = new Random();

        for(ModelPlayer l_player : p_gameState.getD_players()){
            if(!l_player.equals(p_player))
                l_playerList.add(p_player);
        }
        return l_playerList.get(l_random.nextInt(l_playerList.size()));
    }
}
