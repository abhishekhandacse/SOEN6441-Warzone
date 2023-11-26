package Views;

import java.util.List;

import Constants.ApplicationConstants;
import Models.*;
import org.davidmoten.text.utils.WordWrap;

import Exceptions.MapValidationException;
import Utils.CommonUtil;

/**
 * This is the MapView Class.
 */
public class MapView {

	/**
	 * List of Players
	 */
	List<ModelPlayer> d_playersList;

	/**
	 * Current game state
	 */
	GameState d_gameState;

	/**
	 * Map object
	 */
	Map d_map;

	/**
	 * List if countries
	 */
	List<Country> d_countriesList;

	/**
	 * List of Continents
	 */
	List<Continent> d_continentsList;

    /**
     * Reset Color ANSI Code.
     */
	public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Constructor to initialise MapView.
     *
     * @param p_gameState Current GameState.
     */
	public MapView(GameState p_gameState){
		d_gameState = p_gameState;
		d_map = p_gameState.getD_map();
		d_playersList = p_gameState.getD_players();
		d_countriesList = d_map.getD_countries();
		d_continentsList = d_map.getD_continents();
	}

	public void showMap() {

		if(d_playersList != null){
			renderPlayers();
		}

		// renders the continent if any
		if (!CommonUtil.isNull(d_continentsList)) {
			d_continentsList.forEach(l_continent -> {
				renderContinentName(l_continent.getD_continentName());

				List<Country> l_continentCountries = l_continent.getD_countriesList();
				final int[] l_countryIndex = {1};

				// renders the country if any
				if (!CommonUtil.isCollectionEmpty(l_continentCountries)) {
					l_continentCountries.forEach((l_country) -> {
						String l_formattedCountryName = getFormattedCountryName(l_countryIndex[0]++, l_country.getD_countryName());
						System.out.println(l_formattedCountryName);
						try {
							List<Country> l_adjCountries = d_map.getAdjacentCountry(l_country);

							renderFormattedAdjacentCountryName(l_country.getD_countryName(), l_adjCountries);
						} catch (MapValidationException l_invalidMap) {
							System.out.println(l_invalidMap.getMessage());
						}
					});
				} else {
					System.out.println("No countries are present in the continent!");
				}
			});
		} else {
			System.out.println("No continents to display!");
		}
	}

    /**
     * Returns the Colored String.
     *
     * @param p_color Color to be changed to.
     * @param p_s String to be changed color of.
     * @return colored string.
     */
	private String getColorizedString(String p_color, String p_s) {
		if(p_color == null) return p_s;

		return p_color + p_s + ANSI_RESET;
	}

	 /**
     * Gets the Color of continent based on Player.
     *
     * @param p_continentName Continent Name to be rendered.
     * @return Color of continent.
     */
	private String getContinentColor(String p_continentName){
		if(getContinentOwner(p_continentName) != null){
			return getContinentOwner(p_continentName).getD_color();
		}else{
			return null;
		}
	}

	private ModelPlayer getContinentOwner(String p_continentName){
		if (d_playersList != null) {
			for (ModelPlayer p: d_playersList){
				if(!CommonUtil.isNull(p.getContinentNames()) && p.getContinentNames().contains(p_continentName)){
					return p;
				}
			}
		}
		return null;
	}

	private String getCountryColor(String p_countryName){
		if(getCountryOwner(p_countryName) != null){
			return getCountryOwner(p_countryName).getD_color();
		}else{
			return null;
		}
	}

	private ModelPlayer getCountryOwner(String p_countryName){
		if (d_playersList != null) {
			for (ModelPlayer p: d_playersList){
				if(p.getCountryNames().contains(p_countryName)){
					return p;
				}
			}
		}
		return null;
	}

	private Integer getCountryArmies(String p_countryName){
		Integer l_armies = d_gameState.getD_map().getCountryByName(p_countryName).getD_armies();

		if(l_armies == null)
			return 0;
		return l_armies;
	}

	private String getPlayerArmies(ModelPlayer p_player){
		return "(Unallocated Armies: "+p_player.getD_noOfUnallocatedArmies()+")";
	}

    /**
     * Renders the Center String for Heading.
     *
     * @param p_width Defined width in formatting.
     * @param p_s String to be rendered.
     */
	private void renderCenteredString (int p_width, String p_s) {
		String l_centeredString = String.format("%-" + p_width  + "s", String.format("%" + (p_s.length() + (p_width - p_s.length()) / 2) + "s", p_s));

		System.out.format(l_centeredString+"\n");
	}

/**
     * Renders the Separator for heading.
     *
     */
	private void renderSeparator(){
		StringBuilder l_separator = new StringBuilder();

		for (int i = 0; i< ApplicationConstants.CONSOLE_WIDTH -2; i++){
			l_separator.append("-");
		}
		System.out.format("+%s+%n", l_separator.toString());
	}

	private void renderPlayerInfo(Integer p_index, ModelPlayer p_player){
		String l_playerInfo = String.format("%02d. %s %-10s %s", p_index,p_player.getPlayerName(), getPlayerArmies(p_player), " -> "+ getColorizedString(p_player.getD_color(), " COLOR "));
		System.out.println(l_playerInfo);
	}

	private void renderPlayers(){
		int l_counter = 0;

		renderSeparator();
		renderCenteredString(ApplicationConstants.CONSOLE_WIDTH, "GAME PLAYERS");
		renderSeparator();

		for(ModelPlayer p: d_playersList){
			if (!d_gameState.getD_playersFailed().contains(p)) {
				l_counter++;
				renderPlayerInfo(l_counter, p);
				renderCardsOwnedByPlayers(p);
			}
		}
	}

    /**
     * Renders the continent Name with formatted centered string and separator.
     *
     * @param p_continentName Continent Name to be rendered.
     */
	private void renderContinentName(String p_continentName){
		String l_continentName = p_continentName+" ( "+ApplicationConstants.CONTROL_VALUE+" : "+ d_gameState.getD_map().getContinent(p_continentName).getD_continentValue()+" )";

		renderSeparator();
		if(d_playersList != null){
			l_continentName = getColorizedString(getContinentColor(p_continentName), l_continentName);
		}
		renderCenteredString(ApplicationConstants.CONSOLE_WIDTH, l_continentName);
		renderSeparator();
	}

    /**
     * Renders the Country Name as Formatted.
     *
     * @param p_index Index of Countries.
     * @param p_countryName Country Name to be rendered.
     * @return Returns the Formatted String
     */
	private String getFormattedCountryName(int p_index, String p_countryName){
		String l_indexedString = String.format("%02d. %s", p_index, p_countryName);

		if(d_playersList != null){
			String l_armies = "( "+ApplicationConstants.ARMIES+" : "+ getCountryArmies(p_countryName)+" )";
			l_indexedString = String.format("%02d. %s %s", p_index, p_countryName, l_armies);
		}
		return getColorizedString(getCountryColor(p_countryName), String.format("%-30s", l_indexedString));
	}

    /**
     * Renders Adjacent Countries in Formatted Settings.
     *
     * @param p_countryName Country Name to be rendered.
     * @param p_adjCountries List of adjacent countries to be rendered.
     */
	private void renderFormattedAdjacentCountryName(String p_countryName, List<Country> p_adjCountries){
		StringBuilder l_commaSeparatedCountries = new StringBuilder();

		for(int i=0; i<p_adjCountries.size(); i++) {
			l_commaSeparatedCountries.append(p_adjCountries.get(i).getD_countryName());
			if(i<p_adjCountries.size()-1)
				l_commaSeparatedCountries.append(", ");
		}
		String l_adjacentCountry = ApplicationConstants.CONNECTIVITY+" : "+ WordWrap.from(l_commaSeparatedCountries.toString()).maxWidth(ApplicationConstants.CONSOLE_WIDTH).wrap();
		System.out.println(getColorizedString(getCountryColor(p_countryName),l_adjacentCountry));
		System.out.println();
	}

	/**
	 * Method that renders the number of cards owned by the player.
	 *
	 * @param p_player Player Instance
	 */
	private void renderCardsOwnedByPlayers(ModelPlayer p_player){
		StringBuilder l_cards = new StringBuilder();

		for(int i=0; i<p_player.getD_cardsOwnedByPlayer().size(); i++) {
			l_cards.append(p_player.getD_cardsOwnedByPlayer().get(i));
			if(i<p_player.getD_cardsOwnedByPlayer().size()-1)
				l_cards.append(", ");
		}

		String l_cardsOwnedByPlayer = "Cards Owned : "+ WordWrap.from(l_cards.toString()).maxWidth(ApplicationConstants.CONSOLE_WIDTH).wrap();
		System.out.println(getColorizedString(p_player.getD_color(),l_cardsOwnedByPlayer));
		System.out.println();
	}

}

