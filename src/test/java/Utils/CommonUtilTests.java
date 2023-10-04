package Utils;

import com.github.davidmoten.guavamini.Lists;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
public class CommonUtilTests {

    @Test
    public void testStringNull(){
        String l_string = null;

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result,true);
    }

    @Test
    public void testStringEmpty(){
        String l_string = "";

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result,true);
    }

    @Test
    public void testStringValid(){
        String l_string = "abc123";

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result,false);
    }

    @Test
    public void testValidObjectPassed(){
        Object l_validObject = new Object();

        boolean result = CommonUtil.isNull(l_validObject);

        assertEquals(result,false);
    }

    @Test
    public void testInValidObjectPassed(){
        Object l_inValidObject = null;

        boolean result = CommonUtil.isNull(l_inValidObject);

        assertEquals(result,true);
    }


}
