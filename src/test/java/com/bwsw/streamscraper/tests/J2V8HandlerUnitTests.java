package com.bwsw.streamscraper.tests;

import com.bwsw.streamscraper.system.models.J2V8Handler;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class J2V8HandlerUnitTests {

    @BeforeClass
    public static void setUpBeforeClass() throws java.lang.Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test001() throws Exception {
        try {
            J2V8Handler h = new J2V8Handler(
                    "{'init': function () { log('test'); } }", 1);
            h.init();
            h.shutdown();
        } catch (Exception e) {
            System.err.println(e.getClass().toString());
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void test002() {
    }

    @Test
    public void test003() {
        assertEquals(true, true);
    }

}