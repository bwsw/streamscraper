package com.bwsw.streamscraper.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.Suite;

import com.bwsw.streamscraper.tests.StopOnFirstFailureSuite;

/**
* @author Raphael
*
*/
@RunWith(Suite.class)
@SuiteClasses({
com.bwsw.streamscraper.tests.UnitTests.class,
})

public class TestSuite {

}
