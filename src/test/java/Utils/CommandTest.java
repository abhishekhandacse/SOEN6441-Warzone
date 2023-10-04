package Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * The type Command test.
 */
public class CommandTest {

    /**
     * Test valid command.
     */
    @Test
    public void testValidCommand() {
        CommandHandler l_command = new CommandHandler("editcontinent -add continentID continentvalue");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("editcontinent", l_rootCommand);
    }

    /**
     * Test in valid command.
     */
    @Test
    public void testInValidCommand() {
        CommandHandler l_command = new CommandHandler("");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("", l_rootCommand);
    }


    /**
     * Test single word.
     */
    @Test
    public void testSingleWord() {
        CommandHandler l_command = new CommandHandler("validatemap");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("validatemap", l_rootCommand);
    }

    /**
     * Testno flag command.
     */
    @Test
    public void testnoFlagCommand() {
        CommandHandler l_command = new CommandHandler("loadmap abc.txt");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("loadmap", l_rootCommand);
    }

    /**
     * Test single command.
     */
    @Test
    public void testSingleCommand() {
        CommandHandler l_command = new CommandHandler("editcontinent -remove continentID");
        List<Map<String, String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String, String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandTwo = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandTwo);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

    /**
     * Test single command with extra spaces.
     */
    @Test
    public void testSingleCommandWithExtraSpaces() {
        CommandHandler l_command = new CommandHandler("editcontinent      -remove continentID");
        List<Map<String, String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String, String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandTwo = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandTwo);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

    /**
     * Test multi command.
     */
    @Test
    public void testMultiCommand() {
        CommandHandler l_command = new CommandHandler("editcontinent -add continentID continentValue  -remove continentID");
        List<Map<String, String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String, String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandOne = new HashMap<String, String>() {{
            put("arguments", "continentID continentValue");
            put("operation", "add");
        }};
        Map<String, String> l_expectedCommandTwo = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandOne);
        l_expectedOperationsAndValues.add(l_expectedCommandTwo);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

    /**
     * Test no flag command.
     */
    @Test
    public void testNoFlagCommand() {
        CommandHandler l_command = new CommandHandler("loadmap abc.txt");
        List<Map<String, String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String, String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandOne = new HashMap<String, String>() {{
            put("arguments", "abc.txt");
            put("operation", "filename");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandOne);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

    /**
     * Test no flag with extra spaces.
     */
    @Test
    public void testNoFlagWithExtraSpaces() {
        CommandHandler l_command = new CommandHandler("loadmap         abc.txt");
        List<Map<String, String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String, String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandOne = new HashMap<String, String>() {{
            put("arguments", "abc.txt");
            put("operation", "filename");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandOne);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

}
