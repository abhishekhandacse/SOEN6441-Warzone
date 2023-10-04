package Views;

import java.util.*;

import Exceptions.MapValidationException;
import java.util.List;
import Models.Continent;
import Models.Country;
import Models.State;
import Models.Player;
import Models.Map;
import Utils.CommonUtil;


public class MapView {

	public static final int CONSOLE_WIDTH = 80;
	List<Player> d_players;
	State d_gameState;
	Map d_map;
	List<Country> d_countries;
	List<Continent> d_continents;

    
	public static final String ANSI_RESET = "\u001B[0m";

    
	public MapView(State p_gameState){
		d_gameState = p_gameState;
		d_map = p_gameState.getD_map();
		d_map = p_gameState.getD_map();
		d_countries = d_map.getD_countries();
		d_continents = d_map.getD_continents();
	}

    
	public MapView(State p_gameState, List<Player> p_players){
		d_gameState = p_gameState;
		d_players = p_players;
		d_map = p_gameState.getD_map();
		d_countries = d_map.getD_countries();
		d_continents = d_map.getD_continents();
	}

    
	private String getColorizedString(String p_color, String p_s) {
		if(p_color == null) return p_s;

		return p_color + p_s + ANSI_RESET;
	}


	private void renderCenteredString(int p_width, String p_s) {
		String l_centeredString = String.format("%-" + p_width + "s", String.format("%" + (p_s.length() + (p_width - p_s.length()) / 2) + "s", p_s));

		System.out.format(l_centeredString + "\n");
	}


	private void renderSeparator() {
		StringBuilder l_separator = new StringBuilder();

		for (int i = 0; i < CONSOLE_WIDTH - 2; i++) {
			l_separator.append("=");
		}
		System.out.format("+%s+%n", l_separator);
	}


	private void renderContinentName(String p_continentName) {
		String l_continentName = p_continentName + " [ " + "Control Value" + " : " + d_gameState.getD_map().getContinent(p_continentName).getD_continentValue() + " ]";

		renderSeparator();
		if (d_players != null) {
			l_continentName = getColorizedString(getContinentColor(p_continentName), l_continentName);
		}
		renderCenteredString(CONSOLE_WIDTH, l_continentName);
		renderSeparator();
	}



	private String getCountryColor(String p_countryName) {
		if (getCountryOwner(p_countryName) != null) {
			return Objects.requireNonNull(getCountryOwner(p_countryName)).getD_color();
		}else{
			return null;
		}
	}


	private String getContinentColor(String p_continentName) {
		if (getContinentOwner(p_continentName) != null) {
			return Objects.requireNonNull(getContinentOwner(p_continentName)).getD_color();
		} else {
			return null;
		}
	}


	private Player getCountryOwner(String p_countryName) {
		if (d_players != null) {
			for (Player p : d_players) {
				if (p.getCountryNames().contains(p_countryName)) {
					return p;
				}
			}
		}
		return null;
	}




	private void renderPlayers() {
		int l_counter = 0;

		renderSeparator();
		renderCenteredString(CONSOLE_WIDTH, "PLAYERS IN THE GAME");
		renderSeparator();

		for (Player p : d_players) {
			l_counter++;
			renderPlayerInfo(l_counter, p);
		}
	}


	private Player getContinentOwner(String p_continentName) {
		if (d_players != null) {
			for (Player p : d_players) {
				if (!CommonUtil.isNull(p.getContinentNames()) && p.getContinentNames().contains(p_continentName)) {
					return p;
				}
			}
		}
		return null;
	}


	private Integer getCountryArmies(String p_countryName) {
		Integer l_armies = d_gameState.getD_map().getCountryByName(p_countryName).getD_armies();

		if (l_armies == null)
			return 0;
		return l_armies;
	}

	public void showMap() {
		if (d_players != null) {
			renderPlayers();
		}

		// Renders the continent if any
		if (!CommonUtil.isNull(d_continents)) {
			d_continents.forEach(l_continent -> {
				String continentHeader = "+==============================================================================+%n" +
						"| %-75s|%n" +
						"+==============================================================================+%n" +
						"| %-4s | %-30s | %-10s | %-30s|%n" +
						"+==============================================================================+%n";
				System.out.format(getColorizedString(getContinentColor(l_continent.getD_continentName()), continentHeader),
						"COUNTRY DETAILS", "No.", "Country Name", "Armies", "Connections");

				List<Country> l_continentCountries = l_continent.getD_countries();
				final int[] l_countryIndex = { 1 };

				// Renders the country if any
				if (!CommonUtil.isCollectionEmpty(l_continentCountries)) {
					l_continentCountries.forEach((l_country) -> {
						String l_formattedCountryName = getFormattedCountryName(l_countryIndex[0]++, l_country.getD_countryName());
						Integer l_countryArmies = getCountryArmies(l_country.getD_countryName());
						List<Country> l_adjCountries = null;
						try {
							l_adjCountries = d_map.getAdjacentCountry(l_country);
						} catch (MapValidationException l_invalidMap) {
							System.out.println(l_invalidMap.getMessage());
						}
						assert l_adjCountries != null;
						String l_adjacentCountries = getFormattedAdjacentCountryName(l_adjCountries);

						// Apply color to all columns
						String countryRow = "| %-4s | %-30s | %-20s | %-30s|%n";
						System.out.format(getColorizedString(getCountryColor(l_country.getD_countryName()), countryRow),
								l_countryIndex[0] - 1,
								getColorizedString(getCountryColor(l_country.getD_countryName()), l_formattedCountryName),
								getColorizedString(getCountryColor(l_country.getD_countryName()), String.valueOf(l_countryArmies)),
								getColorizedString(getCountryColor(l_country.getD_countryName()), l_adjacentCountries));
					});

					// Close the country table with color
					System.out.format(getColorizedString(getContinentColor(l_continent.getD_continentName()), "+==============================================================================+%n"));
				} else {
					System.out.println("No countries are present in the continent!");
				}
			});
		} else {
			System.out.println("No continents to display!");
		}
	}


	private String getFormattedCountryName(int p_index, String p_countryName){
		String l_indexedString = String.format("%02d. %s", p_index, p_countryName);

		if(d_players != null){
			// Remove "Armies" part from the formatted string
			l_indexedString = String.format("%s", p_countryName);
		}
		return getColorizedString(getCountryColor(p_countryName), String.format("%-30s", l_indexedString));
	}


	private String getFormattedAdjacentCountryName(List<Country> p_adjCountries) {
		StringBuilder l_commaSeparatedCountries = new StringBuilder();

		for (int i = 0; i < p_adjCountries.size(); i++) {
			l_commaSeparatedCountries.append(p_adjCountries.get(i).getD_countryName());
			if (i < p_adjCountries.size() - 1)
				l_commaSeparatedCountries.append(", ");
		}
		return l_commaSeparatedCountries.toString();
	}

	private void renderPlayerInfo(Integer p_index, Player p_player){
		String l_playerInfo = String.format("%s", getColorizedString(p_player.getD_color(), String.format("%02d. %s", p_index, p_player.getPlayerName())));
		System.out.println(l_playerInfo);
	}

}