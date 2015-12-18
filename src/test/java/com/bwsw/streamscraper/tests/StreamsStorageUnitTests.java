package com.bwsw.streamscraper.tests;

import com.bwsw.streamscraper.system.services.CassandraStreamManagementService;
import org.junit.*;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StreamsStorageUnitTests {

	public static CassandraStreamManagementService svc;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		svc = new CassandraStreamManagementService();
		svc.initDb();
		svc.begin();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		svc.end();
	}

	@Before
	public void setUp() throws Exception {
		svc.initDb();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test001_saveLoadPlatformStream() {
	}
}
