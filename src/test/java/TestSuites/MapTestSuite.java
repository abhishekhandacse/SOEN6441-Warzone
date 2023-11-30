package TestSuites;

import Models.ForMapTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import Services.MapServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ ForMapTest.class, MapServiceTest.class })
public class MapTestSuite {
}