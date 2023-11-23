package Utils;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import Constants.ApplicationConstants;

public class CommonUtil implements Serializable {

		public static boolean isEmpty(String p_str) {
		return (p_str == null || p_str.trim().isEmpty());
	}

		public static boolean isNotEmpty(String p_str) {
		return !isEmpty(p_str);
	}

				public static boolean isNull(Object p_object) {
		return (p_object == null);
	}

		public static boolean isCollectionEmpty(Collection<?> p_collection) {
		return (p_collection == null || p_collection.isEmpty());
	}

				public static boolean isMapEmpty(Map<?, ?> p_map) {
		return (p_map == null || p_map.isEmpty());
	}

		public static String getMapFilePath(String p_fileName) {
		String l_absolutePath = new File("").getAbsolutePath();
		return l_absolutePath + File.separator + ApplicationConstants.SRC_MAIN_RESOURCES + File.separator + p_fileName;
	}
}
