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



}
