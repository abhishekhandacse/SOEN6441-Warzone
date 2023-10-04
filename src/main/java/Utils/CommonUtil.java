package Utils;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;


public class CommonUtil {


    public static boolean isEmpty(String p_str) {
        return (p_str == null || p_str.trim().isEmpty());
    }


    public static boolean isNotEmpty(String p_str) {
        return !isEmpty(p_str);
    }


    public static boolean isNull(Object p_object) {
        return Objects.isNull(p_object);
    }


    public static boolean isCollectionEmpty(Collection<?> p_collection) {
        return (p_collection == null || p_collection.isEmpty());
    }


    public static boolean isMapEmpty(Map<?, ?> p_map) {
        return (p_map == null || p_map.isEmpty());
    }


    public static String getMapFilePath(String p_fileName) {
        String l_absolutePath = new File("").getAbsolutePath();
        return l_absolutePath + File.separator + "src/main/maps" + File.separator + p_fileName;
    }
}
