package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Controllers.GameEngine;
import Utils.CommonUtil;
import Views.MapView;

public class OrderExecutionPhase extends Phase{
    // Constructor
	public OrderExecutionPhase(GameEngine p_gameEngine, GameState p_gameState) {
		super(p_gameEngine, p_gameState);
	}

    @Override
	public void initPhase() {
		while (d_gameEngine.getD_CurrentPhase() instanceof OrderExecutionPhase) {
			executeOrders();

			MapView l_mapView = new MapView(d_gameState);
			l_mapView.showMap();

			if (this.checkEndGame(d_gameState))
				break;

			while (!CommonUtil.isNullOrEmptyCollection(d_gameState.getD_playersList())) {
				System.out.println("Press Y/y if you want to continue to next turn or else press N/n to exit");
				BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));

				try {
					String l_continue = l_reader.readLine();

					if (l_continue.equalsIgnoreCase("N")) {
						break;
					} else if (l_continue.equalsIgnoreCase("Y")) {
						d_playerService.assignArmies(d_gameState);
						d_gameEngine.setIssueOrderPhase();
					} else {
						System.out.println("Invalid Input");
					}
				} catch (IOException e) {
					System.out.println("Invalid Input");
				}
			}
		}
	}
}
