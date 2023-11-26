package TestSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import Utils.CommandTest;

/**
 * Test suit for running all test suites involved in this game project
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ CommandTest.class, MainGameTestSuite.class, MainGameTestSuite.class })
public class TestSuiteRunner {
}