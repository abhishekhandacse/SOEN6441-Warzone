package Utils;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;


/**
 * The type Common util.
 */
public class CommonUtil {


    /**
     * Is empty boolean.
     *
     * @param p_str the p str
     * @return the boolean
     */
    public static boolean isEmpty(String p_str) {
        return (p_str == null || p_str.trim().isEmpty());
    }


    /**
     * Is not empty boolean.
     *
     * @param p_str the p str
     * @return the boolean
     */
    public static boolean isNotEmpty(String p_str) {
        return !isEmpty(p_str);
    }


    /**
     * Is null boolean.
     *
     * @param p_object the p object
     * @return the boolean
     */
    public static boolean isNull(Object p_object) {
        return Objects.isNull(p_object);
    }


    /**
     * Is collection empty boolean.
     *
     * @param p_collection the p collection
     * @return the boolean
     */
    public static boolean isCollectionEmpty(Collection<?> p_collection) {
        return (p_collection == null || p_collection.isEmpty());
    }


    /**
     * Is map empty boolean.
     *
     * @param p_map the p map
     * @return the boolean
     */
    public static boolean isMapEmpty(Map<?, ?> p_map) {
        return (p_map == null || p_map.isEmpty());
    }


    /**
     * Gets map file path.
     *
     * @param p_fileName the p file name
     * @return the map file path
     */
    public static String getMapFilePath(String p_fileName) {
        String l_absolutePath = new File("").getAbsolutePath();
        return l_absolutePath + File.separator + "src/main/maps" + File.separator + p_fileName;
    }
}
