package Utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandHandlerTest {


    @Test
    public void test_whenNoCommandPassed_getEmptyString(){
        CommandHandler l_command = new CommandHandler("");

        String l_correctRootWord = l_command.fetchRootCommand();

        assertEquals("",l_correctRootWord);
    }

    @Test
    public void test_whenCorrectCommand_thenGetRootWord(){
        CommandHandler l_commandHandler = new CommandHandler("editcountry -add countryID continentID -remove countryID");

        String l_correctRootWord = l_commandHandler.fetchRootCommand();

        assertEquals("editcountry",l_correctRootWord);
    }

    @Test
    public void test_whenSingleWordCommand_thenShouldGiveRootCommand(){
        CommandHandler l_command = new CommandHandler("showmap");

        String l_correctRootWor = l_command.fetchRootCommand();

        assertEquals("showmap", l_correctRootWor );
    }

}
