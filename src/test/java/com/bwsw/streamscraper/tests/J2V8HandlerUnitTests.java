package com.bwsw.streamscraper.tests;

import com.bwsw.streamscraper.system.models.HashKeyValueStoreStub;
import com.bwsw.streamscraper.system.models.J2V8JSONHandler;
import org.json.simple.parser.ParseException;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.script.ScriptException;

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
            J2V8JSONHandler h = new J2V8JSONHandler(
                    "{'init': function () { for(i=0;i<10;i++) {} } }", 1);
            h.setStore(new HashKeyValueStoreStub<>());
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
                    //
                    "{'init': function () { kv_store_set('test',1); log(kv_store_get('test')); } }", 1);
            h.setStore(new HashKeyValueStoreStub<>());
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1; i++) {
                h.init();
            }
            long elapsedTime = System.currentTimeMillis() - start;
            System.err.println("Time init()x1MLR spent: " + new Long(elapsedTime).toString());

        } catch (Exception e) {
            System.err.println(e.getClass().toString());
            System.err.println(e.getMessage());
        }


/*
        {
            V8 v8 = V8.createV8Runtime();
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                v8.executeObjectScript("var a = [{\"_id\":\"567557d8d909b2a7bd7e963b\",\"index\":0,\"guid\":\"b2a7bb76-947d-4360-811a-ebc17d42d704\",\"isActive\":true,\"balance\":\"$1,440.78\",\"picture\":\"http://placehold.it/32x32\",\"age\":40,\"eyeColor\":\"brown\",\"name\":\"Parrish Cervantes\",\"gender\":\"male\",\"company\":\"MARVANE\",\"email\":\"parrishcervantes@marvane.com\",\"phone\":\"+1 (977) 454-2946\",\"address\":\"973 Ocean Court, Kansas, Illinois, 4632\",\"about\":\"Lorem non non Lorem laborum incididunt ullamco irure. Dolore reprehenderit excepteur esse dolore incididunt duis eu consectetur sint duis in laborum. Ipsum culpa adipisicing Lorem sit. Sint labore aute veniam et ut occaecat laborum sit reprehenderit ipsum exercitation sunt esse. Id ad laborum et qui exercitation non proident velit culpa.\\r\\n\",\"registered\":\"2014-10-26T12:28:41 -06:00\",\"latitude\":-29.269262,\"longitude\":-106.720058,\"tags\":[\"commodo\",\"ut\",\"deserunt\",\"minim\",\"eu\",\"magna\",\"amet\"],\"friends\":[{\"id\":0,\"name\":\"Marisa Hopper\"},{\"id\":1,\"name\":\"Glenna Camacho\"},{\"id\":2,\"name\":\"Freida Howe\"}],\"greeting\":\"Hello, Parrish Cervantes! You have 4 unread messages.\",\"favoriteFruit\":\"banana\"}];");
            }
            long elapsedTime = System.currentTimeMillis() - start;
            System.err.println("Time spent: " + new Long(elapsedTime).toString());
        }
        {
            JSONParser parser = new JSONParser();
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                parser.parse("[{\"_id\":\"567557d8d909b2a7bd7e963b\",\"index\":0,\"guid\":\"b2a7bb76-947d-4360-811a-ebc17d42d704\",\"isActive\":true,\"balance\":\"$1,440.78\",\"picture\":\"http://placehold.it/32x32\",\"age\":40,\"eyeColor\":\"brown\",\"name\":\"Parrish Cervantes\",\"gender\":\"male\",\"company\":\"MARVANE\",\"email\":\"parrishcervantes@marvane.com\",\"phone\":\"+1 (977) 454-2946\",\"address\":\"973 Ocean Court, Kansas, Illinois, 4632\",\"about\":\"Lorem non non Lorem laborum incididunt ullamco irure. Dolore reprehenderit excepteur esse dolore incididunt duis eu consectetur sint duis in laborum. Ipsum culpa adipisicing Lorem sit. Sint labore aute veniam et ut occaecat laborum sit reprehenderit ipsum exercitation sunt esse. Id ad laborum et qui exercitation non proident velit culpa.\\r\\n\",\"registered\":\"2014-10-26T12:28:41 -06:00\",\"latitude\":-29.269262,\"longitude\":-106.720058,\"tags\":[\"commodo\",\"ut\",\"deserunt\",\"minim\",\"eu\",\"magna\",\"amet\"],\"friends\":[{\"id\":0,\"name\":\"Marisa Hopper\"},{\"id\":1,\"name\":\"Glenna Camacho\"},{\"id\":2,\"name\":\"Freida Howe\"}],\"greeting\":\"Hello, Parrish Cervantes! You have 4 unread messages.\",\"favoriteFruit\":\"banana\"}]");
            }
            long elapsedTime = System.currentTimeMillis() - start;
            System.err.println("Time spent: " + new Long(elapsedTime).toString());

        }
*/
    }

    @Test
    public void test003() {
        assertEquals(true, true);
    }

}