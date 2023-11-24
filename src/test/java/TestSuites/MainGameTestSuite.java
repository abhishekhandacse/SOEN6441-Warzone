package TestSuites;

import Models.*;
import Services.PlayerServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ DeployTest.class, PlayerTest.class, PlayerServiceTest.class, AdvanceTest.class, AirliftTest.class,
		BlockadeTest.class, ModelBombTest.class, DiplomacyTest.class, RandomPlayerTest.class, BenevolentPlayerTest.class, AggressivePlayerTest.class, TournamentTest.class})
public class MainGameTestSuite {
}