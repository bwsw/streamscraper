package com.bwsw.streamscraper.tests;

import com.eclipsesource.v8.V8;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class J2V8HandlerUnitTests {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test001() {
        V8 runtime = V8.createV8Runtime();
        //V8Function f = runtime. ("function cnt(s) { return s.length; }");
        //int result = runtime.executeIntegerScript("var str = \"Hello World!\";cnt(str);");
        //System.err.println(result);
        runtime.release();
    }

    @Test
    public void test002() {
    }

    @Test
    public void test003() {
        assertEquals(true, true);
    }

}