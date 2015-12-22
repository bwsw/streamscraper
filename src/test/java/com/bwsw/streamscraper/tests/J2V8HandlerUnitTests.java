package com.bwsw.streamscraper.tests;

import com.bwsw.streamscraper.system.models.HashKeyValueStoreStub;
import com.bwsw.streamscraper.system.models.J2V8JSONHandler;
import com.bwsw.streamscraper.system.models.adapters.j2v8.KeyValueStoreCallbackFactory;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class J2V8HandlerUnitTests {

    @BeforeClass
    public static void setUpBeforeClass() throws java.lang.Exception {
        J2V8JSONHandler.addCallbackFactory(
                new KeyValueStoreCallbackFactory(
                        new HashKeyValueStoreStub<>()));
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
        J2V8JSONHandler h = new J2V8JSONHandler(
                readFile("../../src/main/resources/j2v8test01.js",
                        Charset.defaultCharset()), 1);
        h.init();
        assertEquals(60, h.getCommitInterval());
        assertEquals(true, h.getTerminateOnBadData());
        assertEquals(true, h.getTerminateOnBadEval());
    }

    @Test
    public void test002() throws Exception {
        J2V8JSONHandler h = new J2V8JSONHandler(
                readFile("../../src/main/resources/j2v8test02.js",
                        Charset.defaultCharset()), 1);
        h.init();
        assertEquals(1, h.getCommitInterval());
        assertEquals(true, h.getTerminateOnBadData());
        assertEquals(true, h.getTerminateOnBadEval());
    }

    @Test
    public void test004() throws Exception {
        J2V8JSONHandler h = new J2V8JSONHandler(
                readFile("../../src/main/resources/j2v8test04.js",
                        Charset.defaultCharset()), 1);
        h.init();
        assertEquals(30, h.getCommitInterval());
        assertEquals(false, h.getTerminateOnBadData());
        assertEquals(false, h.getTerminateOnBadEval());
    }

    @Test
    public void test005() throws Exception {
        J2V8JSONHandler h = new J2V8JSONHandler(
                readFile("../../src/main/resources/j2v8test02.js",
                        Charset.defaultCharset()), 1);
        h.init();
        assertEquals(1, h.getCommitInterval());
        assertEquals(true, h.getTerminateOnBadData());
        try {
            h.process(readFile("../../src/main/resources/j2v8test02_data.js",
                    Charset.defaultCharset()));
            assertEquals(true, false);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assertEquals(true, true);
        }
    }

    @Test
    public void test006() throws Exception {
        J2V8JSONHandler h = new J2V8JSONHandler(
                readFile("../../src/main/resources/j2v8test04.js",
                        Charset.defaultCharset()), 1);
        h.init();
        assertEquals(30, h.getCommitInterval());
        assertEquals(false, h.getTerminateOnBadData());
        try {
            h.process(readFile("../../src/main/resources/j2v8test04_data.js",
                    Charset.defaultCharset()));
            assertEquals(true, true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assertEquals(true, false);
        }
    }

    @Test
    public void test007() throws Exception {
        J2V8JSONHandler h = new J2V8JSONHandler(
                readFile("../../src/main/resources/j2v8test07.js",
                        Charset.defaultCharset()), 1);
        h.init();
        try {
            String data = readFile("../../src/main/resources/j2v8test07_data.js", Charset.defaultCharset());
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++)
                h.process(data);
            long elapsedTime = System.currentTimeMillis() - start;
            System.err.println(elapsedTime);
            assertEquals(true, true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assertEquals(true, false);
        }
    }


}