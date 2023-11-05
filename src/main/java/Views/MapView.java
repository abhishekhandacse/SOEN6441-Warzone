package Views;

import Exceptions.MapValidationException;
import Models.*;
import Utils.CommonUtil;

import java.util.List;
import java.util.Objects;

import org.davidmoten.text.utils.WordWrap;

import Constants.ApplicationConstantsHardcoding;


/**
 * The type Map view.
 */
public class MapView {

    /**
     * The constant CONSOLE_WIDTH.
     */
    public static final int CONSOLE_WIDTH = 80;
    /**
     * The constant ANSI_RESET.
     */
    public static final String ANSI_RESET = "\u001B[0m";
    /**
     * The D players.
     */
    List<ModelPlayer> d_playersList;
    /**
     * The D game state.
     */
    GameState d_gameState;
    /**
     * The D map.
     */
    Map d_map;
    /**
     * The D countries.
     */
    List<Country> d_countriesList;
    /**
     * The D continents.
     */
    List<Continent> d_continentsList;


    /**
     * Instantiates a new Map view.
     *
     * @param p_gameState the p game state
     */
    public MapView(GameState p_gameState) {
		d_gameState = p_gameState;
		d_map = p_gameState.getD_map();
		d_playersList = p_gameState.getD_playersList();
		d_countriesList = d_map.getD_allCountries();
		d_continentsList = d_map.getD_allContinents();
	}


    /**
     * Instantiates a new Map view.
     *
     * @param p_gameState the p game state
     * @param p_players   the p players
     */
    public MapView(GameState p_gameState, List<ModelPlayer> p_players) {
        d_gameState = p_gameState;
        d_playersList = p_players;
        d_map = p_gameState.getD_map();
        d_countriesList = d_map.getD_countries();
        d_continentsList = d_map.getD_continents();
    }


    /**
     * Private method to get colorized string.
     *
     * @param p_color the color
     * @param p_s     the input string
     * @return the colorized string
     */
    private String getColorizedString(String p_color, String p_s) {
        if (p_color == null) return p_s;

        return p_color + p_s + ANSI_RESET;
    }

    /**
     * Private method to render a centered string.
     *
     * @param p_width the width of the console
     * @param p_s     the string to be centered
     */
    private void renderCenteredString(int p_width, String p_s) {
        String l_centeredString = String.format("%-" + p_width + "s", String.format("%" + (p_s.length() + (p_width - p_s.length()) / 2) + "s", p_s));

        System.out.format(l_centeredString + "\n");
    }

    /**
     * Private method to render a separator.
     */
    private void renderSeparator() {
        StringBuilder l_separator = new StringBuilder();

        for (int i = 0; i < CONSOLE_WIDTH - 2; i++) {
            l_separator.append("=");
        }
        System.out.format("+%s+%n", l_separator);
    }


    /**
     * Private method to render the name of a continent.
     *
     * @param p_continentName the name of the continent
     */
    private void renderContinentName(String p_continentName) {
        String l_continentName = p_continentName + " [ " + "Control Value" + " : " + d_gameState.getD_map().getContinent(p_continentName).getD_continentValue() + " ]";

        renderSeparator();
        if (d_playersList != null) {
            l_continentName = getColorizedString(getContinentColor(p_continentName), l_continentName);
        }
        renderCenteredString(CONSOLE_WIDTH, l_continentName);
        renderSeparator();
    }

    /**
     * Private method to get the color of a country.
     *
     * @param p_countryName the name of the country
     * @return the color of the country
     */
    private String getCountryColor(String p_countryName) {
        if (getCountryOwner(p_countryName) != null) {
            return Objects.requireNonNull(getCountryOwner(p_countryName)).getD_color();
        } else {
            return null;
        }
    }

    /**
     * Private method to get the color of a continent.
     *
     * @param p_continentName the name of the continent
     * @return the color of the continent
     */
    private String getContinentColor(String p_continentName) {
        if (getContinentOwner(p_continentName) != null) {
            return Objects.requireNonNull(getContinentOwner(p_continentName)).getD_color();
        } else {
            return null;
        }
    }


    /**
     * Private method to get the owner of a country.
     *
     * @param p_countryName the name of the country
     * @return the owner of the country
     */
    private ModelPlayer getCountryOwner(String p_countryName) {
        if (d_playersList != null) {
            for (ModelPlayer p : d_playersList) {
                if (p.getCountryNames().contains(p_countryName)) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Private method to render information about players in the game.
     */
    private void renderPlayers() {
        int l_counter = 0;

        renderSeparator();
        renderCenteredString(CONSOLE_WIDTH, "PLAYERS IN THE GAME");
        renderSeparator();

        for (ModelPlayer p : d_playersList) {
            l_counter++;
            renderPlayerInfo(l_counter, p);
        }
    }

    /**
     * Private method to get the owner of a continent.
     *
     * @param p_continentName the name of the continent
     * @return the owner of the continent
     */
    private ModelPlayer getContinentOwner(String p_continentName) {
        if (d_playersList != null) {
            for (ModelPlayer p : d_playersList) {
                if (!CommonUtil.isNull(p.getContinentNames()) && p.getContinentNames().contains(p_continentName)) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Private method to get the number of armies in a country.
     *
     * @param p_countryName the name of the country
     * @return the number of armies in the country
     */
    private Integer getCountryArmies(String p_countryName) {
        Integer l_armies = d_gameState.getD_map().getCountryByName(p_countryName).getD_armies();

        if (l_armies == null)
            return 0;
        return l_armies;
    }

    /**
     * Public method to show the map.
     */
    public void showMap() {

		if (d_playersList != null) {
			createPlayers();
		}

		// renders the continent if any
		if (!CommonUtil.isNullObject(d_continentsList)) {
			d_continentsList.forEach(l_continent -> {
				createContinentName(l_continent.getD_continentName());

				List<ModelCountry> l_continentCountriesList = l_continent.getD_countries();
				final int[] l_countryIndex = { 1 };

				// renders the country if any
				if (!CommonUtil.isNullOrEmptyCollection(l_continentCountriesList)) {
					l_continentCountriesList.forEach((l_country) -> {
						String l_formattedCountryName = getFormattedCountryName(l_countryIndex[0]++,
								l_country.getD_countryName());
						System.out.println(l_formattedCountryName);
						try {
							List<ModelCountry> l_adjCountries = d_map.getAdjacentCountry(l_country);

							createFormattedAdjacentCountryName(l_country.getD_countryName(), l_adjCountries);
						} catch (InvalidMap l_invalidMap) {
							System.out.println(l_invalidMap.getMessage());
						}
					});
				} else {
					System.out.println("The Continent has no countries!");
				}
			});
		} else {
			System.out.println("No continents to display!");
		}
	}

    private void createCenteredString(int p_width, String p_s) {
		String l_centeredString = String.format("%-" + p_width + "s",
				String.format("%" + (p_s.length() + (p_width - p_s.length()) / 2) + "s", p_s));

		System.out.format(l_centeredString + "\n");
	}

	private void createSeparator() {
		StringBuilder l_separator = new StringBuilder();

		for (int i = 0; i < ApplicationConstantsHardcoding.DISPLAY_WIDTH - 2; i++) {
			l_separator.append("-");
		}
		System.out.format("+%s+%n", l_separator.toString());
	}

	private void createContinentName(String p_continentName) {
		String l_continentName = p_continentName + " ( " + ApplicationConstantsHardcoding.CONTINENT_CONTROL_VALUE
				+ " : " + d_gameState.getD_map().getContinent(p_continentName).getD_continentValue() + " )";

		createSeparator();
		if (d_playersList != null) {
			l_continentName = getColorizedString(getContinentColor(p_continentName), l_continentName);
		}
		createCenteredString(ApplicationConstantsHardcoding.DISPLAY_WIDTH, l_continentName);
		createSeparator();
	}

    private void createCardsOwnedByPlayers(ModelPlayer p_player) {
		StringBuilder l_cards = new StringBuilder();

		for (int i = 0; i < p_player.getD_cardsOwnedByPlayer().size(); i++) {
			l_cards.append(p_player.getD_cardsOwnedByPlayer().get(i));
			if (i < p_player.getD_cardsOwnedByPlayer().size() - 1)
				l_cards.append(", ");
		}

		String l_cardsOwnedByPlayer = "Cards Owned : "
				+ WordWrap.from(l_cards.toString()).maxWidth(ApplicationConstantsHardcoding.DISPLAY_WIDTH).wrap();
		System.out.println(getColorizedString(p_player.getD_color(), l_cardsOwnedByPlayer));
		System.out.println();
	}

	private void createPlayerInfo(Integer p_index, ModelPlayer p_player) {
		String l_playerInfo = String.format("%02d. %s %-10s %s", p_index, p_player.getPlayerName(),
				getPlayerArmies(p_player), " -> " + getColorizedString(p_player.getD_color(), " COLOR "));
		System.out.println(l_playerInfo);
	}

	private void createPlayers() {
		int l_counter = 0;

		createSeparator();
		createCenteredString(ApplicationConstantsHardcoding.DISPLAY_WIDTH, "GAME PLAYERS");
		createSeparator();

		for (ModelPlayer p : d_playersList) {
			l_counter++;
			createPlayerInfo(l_counter, p);
			createCardsOwnedByPlayers(p);
		}
	}

    /**
     * Private method to format the name of a country with an index.
     *
     * @param p_index       the index of the country
     * @param p_countryName the name of the country
     * @return the formatted country name
     */
    private String getFormattedCountryName(int p_index, String p_countryName) {
        String l_indexedString = String.format("%02d. %s", p_index, p_countryName);

        if (d_playersList != null) {
            // Remove "Armies" part from the formatted string
            l_indexedString = String.format("%s", p_countryName);
        }
        return getColorizedString(getCountryColor(p_countryName), String.format("%-30s", l_indexedString));
    }

    private String getColorizedString(String p_color, String p_s) {
		if (p_color == null)
			return p_s;

		return p_color + p_s + ANSI_RESET;
	}

	private Integer getCountryArmies(String p_countryName) {
		Integer l_armies = d_gameState.getD_map().getCountryByName(p_countryName).getD_armies();

		if (l_armies == null)
			return 0;
		return l_armies;
	}

	private String getPlayerArmies(ModelPlayer p_player) {
		return "(Unallocated Armies: " + p_player.getD_noOfUnallocatedArmies() + ")";
	}

    /**
     * Private method to format the names of adjacent countries.
     *
     * @param p_adjCountries the list of adjacent countries
     * @return the formatted string containing the names of adjacent countries
     */
    private void createFormattedAdjacentCountryName(String p_countryName, List<ModelCountry> p_adjCountries) {
		StringBuilder l_commaSeparatedCountries = new StringBuilder();

		for (int i = 0; i < p_adjCountries.size(); i++) {
			l_commaSeparatedCountries.append(p_adjCountries.get(i).getD_countryName());
			if (i < p_adjCountries.size() - 1)
				l_commaSeparatedCountries.append(", ");
		}
		String l_adjacentCountry = ApplicationConstantsHardcoding.GRAPH_CONNECTIONS + " : "
				+ WordWrap.from(l_commaSeparatedCountries.toString())
						.maxWidth(ApplicationConstantsHardcoding.DISPLAY_WIDTH).wrap();
		System.out.println(getColorizedString(getCountryColor(p_countryName), l_adjacentCountry));
		System.out.println();
	}

    /**
     * Private method to render player information.
     *
     * @param p_index the index of the player
     * @param p_player the player object
     */
    private void renderPlayerInfo(Integer p_index, ModelPlayer p_player) {
        String l_playerInfo = String.format("%s", getColorizedString(p_player.getD_color(), String.format("%02d. %s", p_index, p_player.getPlayerName())));
        System.out.println(l_playerInfo);
    }

    private String getCountryColor(String p_countryName) {
		if (getCountryOwner(p_countryName) != null) {
			return getCountryOwner(p_countryName).getD_color();
		} else {
			return null;
		}
	}

	private String getContinentColor(String p_continentName) {
		if (getContinentOwner(p_continentName) != null) {
			return getContinentOwner(p_continentName).getD_color();
		} else {
			return null;
		}
	}

	private ModelPlayer getCountryOwner(String p_countryName) {
		if (d_playersList != null) {
			for (ModelPlayer p : d_playersList) {
				if (p.getCountryNames().contains(p_countryName)) {
					return p;
				}
			}
		}
		return null;
	}

	private ModelPlayer getContinentOwner(String p_continentName) {
		if (d_playersList != null) {
			for (ModelPlayer p : d_playersList) {
				if (!CommonUtil.isNullObject(p.getContinentNames())
						&& p.getContinentNames().contains(p_continentName)) {
					return p;
				}
			}
		}
		return null;
	}

}