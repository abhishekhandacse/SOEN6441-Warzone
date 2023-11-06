package Utils;

import Constants.ApplicationConstantsHardcoding;

import java.io.File;
import java.util.Collection;

/**
 * Utility class for common operations.
 */
public class CommonUtil {

	/**
	 * Get the absolute path for a file using the provided file name.
	 *
	 * @param p_fileName The name of the file.
	 * @return The absolute path for the file.
	 */
	public static String getAbsolutePathForFile(String p_fileName) {
		String l_absolutePath = new File("").getAbsolutePath();
		return l_absolutePath + File.separator + ApplicationConstantsHardcoding.CLASSPATH_SRC_MAIN_RESOURCES + File.separator + p_fileName;
	}

	/**
	 * Check if a string is null or empty.
	 *
	 * @param p_str The string to check.
	 * @return True if the string is null or empty, otherwise false.
	 */
	public static boolean isNullOrEmpty(String p_str) {
		return (p_str == null || p_str.trim().isEmpty());
	}

	/**
	 * Check if an object is null.
	 *
	 * @param p_object The object to check.
	 * @return True if the object is null, otherwise false.
	 */
	public static boolean isNullObject(Object p_object) {
		return (p_object == null);
	}

	/**
	 * Check if a collection is null or empty.
	 *
	 * @param p_collection The collection to check.
	 * @return True if the collection is null or empty, otherwise false.
	 */
	public static boolean isNullOrEmptyCollection(Collection<?> p_collection) {
		return (p_collection == null || p_collection.isEmpty());
	}
}
