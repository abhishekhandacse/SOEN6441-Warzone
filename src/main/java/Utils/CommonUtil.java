/**
 * The {@code CommonUtil} class provides utility methods for common operations.
 *
 * @version 1.0
 */
package Utils;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import Constants.ApplicationConstants;

/**
 * Provides utility methods for common operations.
 */
public class CommonUtil implements Serializable {

	/**
	 * Checks if the given string is empty or null.
	 *
	 * @param p_str The string to check.
	 * @return {@code true} if the string is empty or null, otherwise {@code false}.
	 */
	public static boolean isEmpty(String p_str) {
		return (p_str == null || p_str.trim().isEmpty());
	}

	/**
	 * Checks if the given string is not empty and not null.
	 *
	 * @param p_str The string to check.
	 * @return {@code true} if the string is not empty and not null, otherwise {@code false}.
	 */
	public static boolean isNotEmpty(String p_str) {
		return !isEmpty(p_str);
	}

	/**
	 * Checks if the given object is null.
	 *
	 * @param p_object The object to check.
	 * @return {@code true} if the object is null, otherwise {@code false}.
	 */
			public static boolean isNull(Object p_object) {
		return (p_object == null);
	}

	/**
	 * Checks if the given collection is empty or null.
	 *
	 * @param p_collection The collection to check.
	 * @return {@code true} if the collection is empty or null, otherwise {@code false}.
	 */
	public static boolean isCollectionEmpty(Collection<?> p_collection) {
		return (p_collection == null || p_collection.isEmpty());
	}

	/**
	 * Checks if the given map is empty or null.
	 *
	 * @param p_map The map to check.
	 * @return {@code true} if the map is empty or null, otherwise {@code false}.
	 */
			public static boolean isMapEmpty(Map<?, ?> p_map) {
		return (p_map == null || p_map.isEmpty());
	}

	/**
	 * Gets the absolute path of a file within the project's resources directory.
	 *
	 * @param p_fileName The name of the file.
	 * @return The absolute file path.
	 */
	public static String getMapFilePath(String p_fileName) {
		String l_absolutePath = new File("").getAbsolutePath();
		return l_absolutePath + File.separator + ApplicationConstants.SRC_MAIN_RESOURCES + File.separator + p_fileName;
	}
}
