package Controllers;

public class MapController {


    private void writeCountryAndBoarderMetaData(GameState p_gameState, FileWriter p_writer) throws IOException {
        String l_countryMetaData;
        String l_bordersMetaData;
        List<String> l_bordersList = new ArrayList<>();

        // Writes Country Objects to File And Organizes Border Data for each of them
        p_writer.write(System.lineSeparator() + "[countries]" + System.lineSeparator());
        for (Country l_country : p_gameState.getD_map().getD_countries()) {
            l_countryMetaData = l_country.getD_countryId().toString().concat(" ").concat(l_country.getD_countryName())
                    .concat(" ").concat(l_country.getD_continentId().toString());
            p_writer.write(l_countryMetaData + System.lineSeparator());

            if (null != l_country.getD_adjacentCountryIds() && !l_country.getD_adjacentCountryIds().isEmpty()) {
                l_bordersMetaData = l_country.getD_countryId().toString();
                for (Integer l_adjCountry : l_country.getD_adjacentCountryIds()) {
                    l_bordersMetaData = l_bordersMetaData.concat(" ").concat(l_adjCountry.toString());
                }
                l_bordersList.add(l_bordersMetaData);
            }
        }

        //Writes Border data to the File
        if (!l_bordersList.isEmpty()) {
            p_writer.write(System.lineSeparator() + "[borders]" + System.lineSeparator());
            for (String l_borderStr : l_bordersList) {
                p_writer.write(l_borderStr + System.lineSeparator());
            }
        }
    }

    private void writeContinentMetadata(GameState p_gameState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + "[continents]" + System.lineSeparator());
        for (Continent l_continent : p_gameState.getD_map().getD_continents()) {
            p_writer.write(
                    l_continent.getD_continentName().concat(" ").concat(l_continent.getD_continentValue().toString())
                            + System.lineSeparator());
        }
    }

    public void resetMap(GameState p_gameState) {
        consoleLogger.writeLog("Map cannot be loaded, as it is invalid. Kindly provide valid map");
        p_gameState.setD_map(new Models.Map());
    }
}
