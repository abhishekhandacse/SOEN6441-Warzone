package Utils;

import com.github.davidmoten.guavamini.Lists;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * The type Common util test.
 */
public class CommonUtilTest {

    /**
     * Test string null. to check if string is null. It should result true.
     */
    @Test
    public void testStringNull() {

        String l_string = null;

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result, true);
    }

    /**
     * Test string empty. To check if String entered is empty. Result should be true
     */
    @Test
    public void testStringEmpty() {
        String l_string = "";

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result, true);
    }

    /**
     * Test string valid. To check if string entered is valid and not empty. Result should be false
     */
    @Test
    public void testStringValid() {
        String l_string = "abc123";

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result, false);
    }

    /**
     * Test valid object passed. To check if the object is valid.
     */
    @Test
    public void testValidObjectPassed() {
        Object l_validObject = new Object();

        boolean result = CommonUtil.isNull(l_validObject);

        assertEquals(result, false);
    }

    /**
     * Test in valid object passed. to check if the object is not null.
     */
    @Test
    public void testInValidObjectPassed() {
        Object l_inValidObject = null;

        boolean result = CommonUtil.isNull(l_inValidObject);

        assertEquals(result, true);
    }

    /**
     * Test empty collection. TO check if the collection passed is empty.
     */
    @Test
    public void testEmptyCollection() {
        ArrayList<Integer> l_arrayList = Lists.newArrayList();

        boolean result = CommonUtil.isCollectionEmpty(l_arrayList);

        assertEquals(result, true);
    }

    /**
     * Test collection not empty.To check if the collection passed is not empty.
     */
    @Test
    public void testCollectionNotEmpty() {
        ArrayList<Integer> l_arrayList = Lists.newArrayList(1, 2, 3);

        boolean result = CommonUtil.isCollectionEmpty(l_arrayList);

        assertEquals(result, false);
    }


    /**
     * Test empty map.To check if the map passed is  empty.
     */
    @Test
    public void TestEmptyMap() {
        Map<Object, Object> l_map = Collections.emptyMap();

        boolean result = CommonUtil.isMapEmpty(l_map);

        assertEquals(result, true);
    }

    /**
     * Test map not empty. To check if the map passed is not empty.
     */
    @Test
    public void TestMapNotEmpty() {
        HashMap<String, String> l_map = new HashMap<>();
        l_map.put("KEY", "VALUE");

        boolean result = CommonUtil.isMapEmpty(l_map);

        assertEquals(result, false);
    }

    /**
     * Test pass file name.  To check if valid file name is passed.
     */
    @Test
    public void TestPassFileName() {
        String l_fileName = "abcd";

        String filePath = CommonUtil.getMapFilePath(l_fileName);

        assertThat(filePath, CoreMatchers.containsString(l_fileName));
    }

}
