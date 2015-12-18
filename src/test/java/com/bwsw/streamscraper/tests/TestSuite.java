package com.bwsw.streamscraper.tests;

/**
 * Created by ivan on 18.12.15.
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StreamsUnitTests.class,
        J2V8HandlerUnitTests.class,
        StreamsStorageUnitTests.class
})
public class TestSuite {
}
