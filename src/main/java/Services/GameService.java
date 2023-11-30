package Services;

import Constants.ApplicationConstants;
import Models.Phase;

import java.io.*;

/**
 * The GameService class load and save game file.
 */
public class GameService {

    /**
     * Serialize and save current game to specific file.
     *
     * @param p_phase instance of current game phase
     * @param p_nameOfFile name of the file
     */
    public static void saveGame(Phase p_phase, String p_nameOfFile){
        try {
            FileOutputStream l_gameSaveFile =new FileOutputStream(ApplicationConstants.SRC_MAIN_RESOURCES + "/" + p_nameOfFile);
            ObjectOutputStream l_gameSaveFileObjectStream=new ObjectOutputStream(l_gameSaveFile);
            l_gameSaveFileObjectStream.writeObject(p_phase);
            l_gameSaveFileObjectStream.flush();
            l_gameSaveFileObjectStream.close();
        } catch (Exception l_e) {
            l_e.printStackTrace();
        }
    }

    /**
     * De-Serialize the Phase stored in specified file.
     *
     * @param p_nameOfFile name of file to load phase from
     * @return the Phase saved in file
     * @throws IOException input output exception when file not found
     * @throws ClassNotFoundException if phase Phase class not found
     */
    public static Phase loadGame(String p_nameOfFile) throws IOException, ClassNotFoundException {
        ObjectInputStream l_inpStream = new ObjectInputStream(new FileInputStream(ApplicationConstants.SRC_MAIN_RESOURCES + "/" + p_nameOfFile));
        Phase l_phase = (Phase) l_inpStream.readObject();

        l_inpStream.close();
        return l_phase;
    }
}
