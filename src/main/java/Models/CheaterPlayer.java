
package Models;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Services.PlayerService;

public class CheaterPlayer extends PlayerBehaviorStrategy {


    @Override
    public String createOrder(ModelPlayer p_player, GameState p_stateOfGame) throws IOException {

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

    }

}
