package com.bwsw.streamscraper.tests;

import com.bwsw.streamscraper.system.exceptions.DuplicateVstreamException;
import com.bwsw.streamscraper.system.exceptions.ImpossibleStreamException;
import com.bwsw.streamscraper.system.exceptions.IncompatibleStreamException;
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

	@Ignore
	public void test001_saveLoadPlatformStream() 
			throws 
				DuplicateVstreamException, 
				IncompatibleStreamException, 
				ImpossibleStreamException {
/*		CassandraStreamStorageService ss = new CassandraStreamStorageService(svc);
		PlatformStream ps = new PlatformStream();
		ps
			.setProperty("property1", "value1")
			.setProperty("property2", "value2")
			.setProperty(PlatformStream.P_PARALLEL, "true")
			.setProperty(PlatformStream.P_EPHEMERAL, "true")
			.addVirtualStream((VirtualStream)(new VirtualStream("test",))
					.setProperty(PlatformStream.P_PARALLEL, "true")
					.setProperty(PlatformStream.P_EPHEMERAL, "true"));
		ss.save(ps);*/
	}
}
