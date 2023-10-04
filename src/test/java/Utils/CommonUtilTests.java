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
    public void whenStringNull_shouldReturnTrue(){
        String l_string = null;

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result,true);
    }

    @Test
    public void whenStringEmpty_shouldReturnTrue(){
        String l_string = "";

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result,true);
    }

    @Test
    public void whenStringValid_shouldReturnFalse(){
        String l_string = "abc123";

        boolean result = CommonUtil.isEmpty(l_string);

        assertEquals(result,false);
    }

}
