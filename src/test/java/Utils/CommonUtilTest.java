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
     * Test string null.
     */
    @Test
    public void testStringNull() {

        String l_string = null;

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result, true);
    }

    /**
     * Test string empty.
     */
    @Test
    public void testStringEmpty() {
        String l_string = "";

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result, true);
    }

    /**
     * Test string valid.
     */
    @Test
    public void testStringValid() {
        String l_string = "abc123";

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result, false);
    }

    /**
     * Test valid object passed.
     */
    @Test
    public void testValidObjectPassed() {
        Object l_validObject = new Object();

        boolean result = CommonUtil.isNull(l_validObject);

        assertEquals(result, false);
    }

    /**
     * Test in valid object passed.
     */
    @Test
    public void testInValidObjectPassed() {
        Object l_inValidObject = null;

        boolean result = CommonUtil.isNull(l_inValidObject);

        assertEquals(result, true);
    }

    /**
     * Test empty collection.
     */
    @Test
    public void testEmptyCollection() {
        ArrayList<Integer> l_arrayList = Lists.newArrayList();

        boolean result = CommonUtil.isCollectionEmpty(l_arrayList);

        assertEquals(result, true);
    }

    /**
     * Test collection not empty.
     */
    @Test
    public void testCollectionNotEmpty() {
        ArrayList<Integer> l_arrayList = Lists.newArrayList(1, 2, 3);

        boolean result = CommonUtil.isCollectionEmpty(l_arrayList);

        assertEquals(result, false);
    }


    /**
     * Test empty map.
     */
    @Test
    public void TestEmptyMap() {
        Map<Object, Object> l_map = Collections.emptyMap();

        boolean result = CommonUtil.isMapEmpty(l_map);

        assertEquals(result, true);
    }

    /**
     * Test map not empty.
     */
    @Test
    public void TestMapNotEmpty() {
        HashMap<String, String> l_map = new HashMap<>();
        l_map.put("KEY", "VALUE");

        boolean result = CommonUtil.isMapEmpty(l_map);

        assertEquals(result, false);
    }

    /**
     * Test pass file name.
     */
    @Test
    public void TestPassFileName() {
        String l_fileName = "abcd";

        String filePath = CommonUtil.getMapFilePath(l_fileName);

        assertThat(filePath, CoreMatchers.containsString(l_fileName));
    }

}
