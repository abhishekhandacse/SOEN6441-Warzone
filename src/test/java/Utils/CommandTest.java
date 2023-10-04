package Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CommandTest {

    @Test
    public void test_validCommand_getRootCommand(){
        CommandHandler l_command = new CommandHandler("editcontinent -add continentID continentvalue");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("editcontinent",l_rootCommand);
    }

    @Test
    public void test_inValidCommand_getRootCommand(){
        CommandHandler l_command = new CommandHandler("");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("", l_rootCommand);
    }


    @Test
    public void test_singleWord_getRootCommand(){
        CommandHandler l_command = new CommandHandler("validatemap");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("validatemap", l_rootCommand);
    }

}
