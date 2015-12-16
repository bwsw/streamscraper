package com.bwsw.streamscraper.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.junit.runners.MethodSorters;

import com.bwsw.streamscraper.system.*;

import org.junit.FixMethodOrder;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StreamsStorageUnitTests {

	public static StreamScraperMgmtService svc;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		svc = new StreamScraperMgmtService();
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
	public void test001_saveLoadPlatformStream() 
			throws 
				DuplicateVstreamException, 
				IncompatibleStreamException, 
				ImpossibleStreamException {
		PlatformStreamStorageService ss = new PlatformStreamStorageService(svc);
		PlatformStream ps = new PlatformStream();
		ps
			.setProperty("property1", "value1")
			.setProperty("property2", "value2")
			.setProperty(PlatformStream.P_PARALLEL, "true")
			.setProperty(PlatformStream.P_EPHEMERAL, "true")
			.addVirtualStream((VirtualStream)(new VirtualStream())
					.setProperty(PlatformStream.P_PARALLEL, "true")
					.setProperty(PlatformStream.P_EPHEMERAL, "true"));
		ss.save(ps);
	}
}
