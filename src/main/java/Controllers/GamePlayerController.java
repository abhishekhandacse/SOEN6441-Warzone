package Controllers;


import Logger.ConsoleLogger;
import Models.*;
import Utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlayerController {
    ConsoleLogger consoleLogger = new ConsoleLogger();
	public boolean isPlayerNameUnique(List<Player> p_existingPlayerList, String p_playerName) {
		boolean l_isUnique = true;
		if (!CommonUtil.isCollectionEmpty(p_existingPlayerList)) {
			for (Player l_player : p_existingPlayerList) {
				if (l_player.getPlayerName().equalsIgnoreCase(p_playerName)) {
					l_isUnique = false;
					break;
				}
			}
		}
		return l_isUnique;
	}
}
