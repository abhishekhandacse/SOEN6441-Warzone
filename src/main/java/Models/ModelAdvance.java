/**
 * The `Advance` class represents an "Advance" order in a strategy board game. An Advance order allows a player
 * to move armies from one of their owned countries to an adjacent target country. If the target country is owned
 * by another player and has no armies, it can be conquered.
 */
package Models;

import Services.PlayerService;
import Utils.CommonUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelAdvance implements Order {

    /**
     * The name of the target country.
     */
    String d_tCountryName;

    /**
     * The name of the source country.
     */
    String d_sCountryName;

    /**
     * The number of armies placed in the target country.
     */
    Integer d_placedArmiesCount;

    /**
     * The player who initiated the Advance order.
     */
    ModelPlayer d_initiatePlayer;

    /**
     * A log of the order's execution.
     */
    String d_orderExecutionLog;

    /**
     * Constructs an Advance order.
     *
     * @param p_playerInitiator The player who initiates the order.
     * @param p_sourceCountryName The name of the source country.
     * @param p_targetCountry The name of the target country.
     * @param p_countOfArmyPlaced The number of armies to be placed in the target country.
     */
    public ModelAdvance(ModelPlayer p_playerInitiator, String p_sourceCountryName, String p_targetCountry,
                        Integer p_countOfArmyPlaced) {
        this.d_tCountryName = p_targetCountry;
        this.d_sCountryName = p_sourceCountryName;
        this.d_initiatePlayer = p_playerInitiator;
        this.d_placedArmiesCount = p_countOfArmyPlaced;
    }

    /**
     * Conquer the target country, transferring ownership and armies to the initiating player.
     *
     * @param p_gameState The current game state.
     * @param p_playerOfTarCountry The player who owns the target country.
     * @param p_targetCountry The target country to be conquered.
     */
    private void conquerTargetCountry(GameState p_gameState, ModelPlayer p_playerOfTarCountry, ModelCountry p_targetCountry) {
        p_targetCountry.setD_armies(d_placedArmiesCount);
        p_playerOfTarCountry.getD_coutriesOwned().remove(p_targetCountry);
        this.d_initiatePlayer.getD_coutriesOwned().add(p_targetCountry);
        this.setD_orderExecutionLog(
                "Player : " + this.d_initiatePlayer.getPlayerName() + " is assigned with Country : "
                        + p_targetCountry.getD_countryName() + " and armies : " + p_targetCountry.getD_armies(),
                "default");
        p_gameState.updateLog(orderExecutionLog(), "effect");
        this.updateContinents(this.d_initiatePlayer, p_playerOfTarCountry, p_gameState);
    }

    /**
     * Generates a log of the current order for display.
     *
     * @return A string representing the current order.
     */
    private String currentOrder() {
        return "Advance Order : " + "advance" + " " + this.d_sCountryName + " " + this.d_tCountryName + " "
                + this.d_placedArmiesCount;
    }

    /**
     * Deploys armies to the target country.
     *
     * @param p_targetCountry The target country to receive the deployed armies.
     */
    public void deployArmiesToTarget(ModelCountry p_targetCountry) {
        Integer l_updatedTargetContArmies = p_targetCountry.getD_armies() + this.d_placedArmiesCount;
        p_targetCountry.setD_armies(l_updatedTargetContArmies);
    }

    /**
     * Executes the Advance order, including deploying armies, conquering, or resolving battles.
     *
     * @param p_gameState The current game state.
     */
    @Override
    public void execute(GameState p_gameState) {
        if (valid(p_gameState)) {
            ModelPlayer l_playerOfTargetCountry = getPlayerOfTargetCountry(p_gameState);
            ModelCountry l_targetCountry = p_gameState.getD_map().getCountryByName(d_tCountryName);
            ModelCountry l_sourceCountry = p_gameState.getD_map().getCountryByName(d_sCountryName);
            Integer l_sourceArmiesToUpdate = l_sourceCountry.getD_armies() - this.d_placedArmiesCount;
            l_sourceCountry.setD_armies(l_sourceArmiesToUpdate);

            if (l_playerOfTargetCountry.getPlayerName().equalsIgnoreCase(this.d_initiatePlayer.getPlayerName())) {
                deployArmiesToTarget(l_targetCountry);
            } else if (l_targetCountry.getD_armies() == 0) {
                conquerTargetCountry(p_gameState, l_playerOfTargetCountry, l_targetCountry);
                this.d_initiatePlayer.assignCard();
            } else {
                produceOrderResult(p_gameState, l_playerOfTargetCountry, l_targetCountry, l_sourceCountry);
            }
        } else {
            p_gameState.updateLog(orderExecutionLog(), "effect");
        }
    }

    /**
     * Retrieves the name of the order.
     *
     * @return The name of the order, which is "advance."
     */
    @Override
    public String getOrderName() {
        return "advance";
    }

    /**
     * Retrieves the player who owns the target country from the game state.
     *
     * @param p_gameState The current game state.
     * @return The player who owns the target country, or null if not found.
     */
    private ModelPlayer getPlayerOfTargetCountry(GameState p_gameState) {
        ModelPlayer l_playerOfTargetCountry = null;
        for (ModelPlayer l_player : p_gameState.getD_playersList()) {
            String l_cont = l_player.getCountryNames().stream()
                    .filter(l_country -> l_country.equalsIgnoreCase(this.d_tCountryName)).findFirst().orElse(null);
            if (!CommonUtil.isNullOrEmpty(l_cont)) {
                l_playerOfTargetCountry = l_player;
            }
        }
        return l_playerOfTargetCountry;
    }

    /**
     * Generates a list of random army units with specified size and role.
     *
     * @param p_size The size of the army unit list.
     * @param p_role The role of the army units (attacker or defender).
     * @return A list of random army units.
     */
    private List<Integer> generateRandomArmyUnits(int p_size, String p_role) {
        List<Integer> l_armyList = new ArrayList<>();
        double l_probability = "attacker".equalsIgnoreCase(p_role) ? 0.6 : 0.7;
        for (int l_i = 0; l_i < p_size; l_i++) {
            int l_randomNumber = getRandomInteger(10, 1);
            Integer l_armyUnit = (int) Math.round(l_randomNumber * l_probability);
            l_armyList.add(l_armyUnit);
        }
        return l_armyList;
    }

    /**
     * Generates a random integer within a specified range.
     *
     * @param p_maximum The maximum value of the random integer (exclusive).
     * @param p_minimum The minimum value of the random integer (inclusive).
     * @return A random integer within the specified range.
     */
    private static int getRandomInteger(int p_maximum, int p_minimum) {
        return ((int) (Math.random() * (p_maximum - p_minimum))) + p_minimum;
    }

    /**
     * Handles surviving armies after a battle, updating ownership and armies as needed.
     *
     * @param p_attackerArmiesLeft The number of surviving attacker armies.
     * @param p_defenderArmiesLeft The number of surviving defender armies.
     * @param p_sourceCountry The source country of the battle.
     * @param p_targetCountry The target country of the battle.
     * @param p_playerOfTarCountry The player who owns the target country.
     */
    public void handleSurvivingArmies(Integer p_attackerArmiesLeft, Integer p_defenderArmiesLeft,
                                      ModelCountry p_sourceCountry, ModelCountry p_targetCountry, ModelPlayer p_playerOfTarCountry) {
        if (p_defenderArmiesLeft == 0) {
            p_playerOfTarCountry.getD_coutriesOwned().remove(p_targetCountry);
            p_targetCountry.setD_armies(p_attackerArmiesLeft);
            this.d_initiatePlayer.getD_coutriesOwned().add(p_targetCountry);
            this.setD_orderExecutionLog(
                    "Player: " + this.d_initiatePlayer.getPlayerName() + " has taken control of Country: "
                            + p_targetCountry.getD_countryName() + " with an army of " + p_targetCountry.getD_armies(),
                    "default");

            this.d_initiatePlayer.assignCard();
        } else {
            p_targetCountry.setD_armies(p_defenderArmiesLeft);

            Integer l_sourceArmiesToUpdate = p_sourceCountry.getD_armies() + p_attackerArmiesLeft;
            p_sourceCountry.setD_armies(l_sourceArmiesToUpdate);
            String l_country1 = "Country: " + p_targetCountry.getD_countryName() + " now has "
                    + p_targetCountry.getD_armies() + " remaining armies and is still owned by Player: "
                    + p_playerOfTarCountry.getPlayerName();
            String l_country2 = "Country: " + p_sourceCountry.getD_countryName() + " now has "
                    + p_sourceCountry.getD_armies() + " armies and is still owned by Player: "
                    + this.d_initiatePlayer.getPlayerName();
            this.setD_orderExecutionLog(l_country1 + System.lineSeparator() + l_country2, "default");
        }
    }

    /**
     * Retrieves the log of the order's execution.
     *
     * @return The log of the order's execution.
     */
    @Override
    public String orderExecutionLog() {
        return this.d_orderExecutionLog;
    }

    /**
     * Prints the order's execution log.
     */
    @Override
    public void printOrder() {
        this.d_orderExecutionLog = "\n---------- Advance order issued by Player: " + this.d_initiatePlayer.getPlayerName()
                + " ----------\n" + System.lineSeparator() + "Move " + this.d_placedArmiesCount + " armies from "
                + this.d_sCountryName + " to " + this.d_tCountryName;
        System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
    }

    /**
     * Produces the result of the order, including battling and updating ownership.
     *
     * @param p_stateOfGame The current game state.
     * @param p_playerOfTarCountry The player who owns the target country.
     * @param p_targetCountry The target country.
     * @param p_sourceCountry The source country.
     */
    private void produceOrderResult(GameState p_stateOfGame, ModelPlayer p_playerOfTarCountry, ModelCountry p_targetCountry,
                                    ModelCountry p_sourceCountry) {
        Integer l_armiesInAttack = this.d_placedArmiesCount < p_targetCountry.getD_armies()
                ? this.d_placedArmiesCount
                : p_targetCountry.getD_armies();

        List<Integer> l_defenderArmies = generateRandomArmyUnits(l_armiesInAttack, "defender");
        List<Integer> l_attackerArmies = generateRandomArmyUnits(l_armiesInAttack, "attacker");
        this.produceBattleResult(p_sourceCountry, p_targetCountry, l_attackerArmies, l_defenderArmies,
                p_playerOfTarCountry);

        p_stateOfGame.updateLog(orderExecutionLog(), "effect");
        this.updateContinents(this.d_initiatePlayer, p_playerOfTarCountry, p_stateOfGame);
    }

    /**
     * Produces the result of a battle, determining surviving armies and ownership changes.
     *
     * @param p_sourceCountry The source country of the battle.
     * @param p_targetCountry The target country of the battle.
     * @param p_attackerArmies The attacker's army units.
     * @param p_defenderArmies The defender's army units.
     * @param p_playerOfTarCountry The player who owns the target country.
     */
    private void produceBattleResult(ModelCountry p_sourceCountry, ModelCountry p_targetCountry, List<Integer> p_attackerArmies,
                                     List<Integer> p_defenderArmies, ModelPlayer p_playerOfTarCountry) {
        Integer l_attackerArmiesLeft = this.d_placedArmiesCount > p_targetCountry.getD_armies()
                ? this.d_placedArmiesCount - p_targetCountry.getD_armies()
                : 0;
        Integer l_defenderArmiesLeft = this.d_placedArmiesCount < p_targetCountry.getD_armies()
                ? p_targetCountry.getD_armies() - this.d_placedArmiesCount
                : 0;
        for (int l_i = 0; l_i < p_attackerArmies.size(); l_i++) {
            if (p_attackerArmies.get(l_i) > p_defenderArmies.get(l_i)) {
                l_attackerArmiesLeft++;
            } else {
                l_defenderArmiesLeft++;
            }
        }
        this.handleSurvivingArmies(l_attackerArmiesLeft, l_defenderArmiesLeft, p_sourceCountry, p_targetCountry,
                p_playerOfTarCountry);
    }

    /**
     * Sets the order execution log and handles different log types (error or default).
     *
     * @param p_orderExecutionLog The log message to be set.
     * @param p_logType The type of the log (error or default).
     */
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
        this.d_orderExecutionLog = p_orderExecutionLog;
        if (p_logType.equals("error")) {
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }

    /**
     * Updates the continents of the players involved in the battle.
     *
     * @param p_playerOfSrcCountry The player who owns the source country.
     * @param p_playerOfTarCountry The player who owns the target country.
     * @param p_gameState The current game state.
     */
    private void updateContinents(ModelPlayer p_playerOfSrcCountry, ModelPlayer p_playerOfTarCountry,
                                  GameState p_gameState) {
        System.out.println("Updating continents of players involved in battle...");
        List<ModelPlayer> l_playersList = new ArrayList<>();
        p_playerOfSrcCountry.setD_continentsOwned(new ArrayList<>());
        p_playerOfTarCountry.setD_continentsOwned(new ArrayList<>());

        l_playersList.add(p_playerOfSrcCountry);
        l_playersList.add(p_playerOfTarCountry);

        PlayerService l_playerService = new PlayerService();
        l_playerService.performContinentAssignment(l_playersList, p_gameState.getD_map().getD_allContinents());
    }

    /**
     * Validates whether the Advance order is valid, including source country ownership and armies.
     *
     * @param p_gameState The current game state.
     * @return True if the order is valid; otherwise, false.
     */
    @Override
    public boolean valid(GameState p_gameState) {
        ModelCountry l_country = d_initiatePlayer.getD_coutriesOwned().stream()
                .filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_sCountryName.toString()))
                .findFirst().orElse(null);
        if (l_country == null) {
            this.setD_orderExecutionLog(this.currentOrder() + " cannot be executed because the source country: "
                    + this.d_sCountryName + " specified in the advance order does not belong to the player: "
                    + d_initiatePlayer.getPlayerName(), "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        if (this.d_placedArmiesCount > l_country.getD_armies()) {
            this.setD_orderExecutionLog(this.currentOrder()
                    + " cannot be executed because the number of armies specified in the advance order exceeds the armies of the source country: "
                    + this.d_sCountryName, "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        if (Objects.equals(this.d_placedArmiesCount, l_country.getD_armies())) {
            this.setD_orderExecutionLog(this.currentOrder() + " cannot be executed because the source country: "
                            + this.d_sCountryName + " has " + l_country.getD_armies()
                            + " army units, and all of them cannot be given an advance order; at least one army unit must remain to retain the territory.",
                    "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        if (!d_initiatePlayer.negotiationValidation(this.d_tCountryName)) {
            this.setD_orderExecutionLog(this.currentOrder() + " cannot be executed because " + d_initiatePlayer.getPlayerName() + " has a negotiation pact with the target country's player!", "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
    }
}
