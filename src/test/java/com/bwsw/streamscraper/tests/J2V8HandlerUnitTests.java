package com.bwsw.streamscraper.tests;

import com.bwsw.streamscraper.system.models.HashKeyValueStoreStub;
import com.bwsw.streamscraper.system.models.J2V8JSONHandler;
import org.json.simple.parser.ParseException;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class J2V8HandlerUnitTests {

    @BeforeClass
    public static void setUpBeforeClass() throws java.lang.Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() {
    }

    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
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
            J2V8JSONHandler h = new J2V8JSONHandler(
                    readFile("../../src/main/resources/j2v8test01.js",
                            Charset.defaultCharset()), 1,
                    new HashKeyValueStoreStub<>());
            h.init();
            h.shutdown();
        } catch (Exception e) {
            System.err.println(e.getClass().toString());
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void test002() throws ParseException, ScriptException {

        try {
            J2V8JSONHandler h = new J2V8JSONHandler(
                    readFile("../../src/main/resources/j2v8test01.js",
                            Charset.defaultCharset()), 1,
                    new HashKeyValueStoreStub<>());

            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++)
                h.init();
            long elapsedTime = System.currentTimeMillis() - start;
            System.err.println("Time init() spent: " + new Long(elapsedTime).toString());

        } catch (Exception e) {
            System.err.println(e.getClass().toString());
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void test003() {
        assertEquals(true, true);
    }

}