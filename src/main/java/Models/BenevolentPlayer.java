package Models;

import java.io.IOException;
import java.util.ArrayList;

public class BenevolentPlayer extends PlayerBehaviorStrategy{
    
    ArrayList<Country> d_deployCountriesList = new ArrayList<Country>();

    @Override
    public String getPlayerBehavior() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayerBehavior'");
    }

    @Override
    public String createOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createOrder'");
    }

    @Override
    public String createCardOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState, String p_currentCardName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createCardOrder'");
    }

    @Override
    public String createAdvanceOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAdvanceOrder'");
    }

    @Override
    public String createDeployOrder(ModelPlayer p_modelPlayer, GameState p_currentGameState) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createDeployOrder'");
    }
    
}
