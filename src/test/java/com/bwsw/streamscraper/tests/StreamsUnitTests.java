package com.bwsw.streamscraper.tests;

import com.bwsw.streamscraper.system.*;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StreamsUnitTests {

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
	public void test001_constructor_PS() {
		PlatformStream ps = new PlatformStream();
		PlatformStream ps2 = new PlatformStream(ps.getID());
		assertEquals(ps.getID(), ps2.getID());
	}
	
	@Test
	public void test002_setID() {
		UUID id = UUID.randomUUID();
		PlatformStream ps = new PlatformStream();
		ps.setID(id);
		assertEquals(id, ps.getID());
	}
	
	@Test
	public void test003_setProperty() {
		PlatformStream ps = new PlatformStream();
		ps.setProperty("bandwidth", "10").setProperty("log", "yes");
		assertEquals("10", ps.getProperty("bandwidth"));
		assertEquals("yes",ps.getProperty("log"));
		assertEquals(null, ps.getProperty("unset"));
	}
	
	@Test
	public void test003_vstreams() 
			throws DuplicateVstreamException, 
			IncompatibleStreamException, 
			ImpossibleStreamException {
		PlatformStream ps = new PlatformStream();
		VirtualStream vs1 = new VirtualStream();
		
		try {
				ps.addVirtualStream(vs1);
				assertEquals(1, 0);
		} catch (IncompatibleStreamException e) {
			assertEquals(0, 0);
		}
	}

    @Ignore
    public void test004_factoryPVS() throws ImpossibleStreamException {
		VirtualStream vs = StreamFactory.getParallelVirtualStream(UUID.randomUUID(), 10, true);
		assertNotEquals(null, vs);
		assertEquals("10", vs.getProperty(PlatformStream.P_BANDWIDTH));
        assertEquals("true", vs.getProperty(PlatformStream.P_PARALLEL));
        assertEquals("true", vs.getProperty(PlatformStream.P_EPHEMERAL));
        assertEquals(1, vs.getWeight());
        assertEquals(null, vs.getProperty(PlatformStream.P_BACKLOG));
        assertEquals(null, vs.getProperty(PlatformStream.P_RECURRENT));

	}

	@Test
    public void test004_factoryPPS() throws ImpossibleStreamException {
        ParallelPlatformStream ps = new ParallelPlatformStream(10);
        assertEquals(10, ps.getBandwidth());
        assertEquals("true", ps.getProperty(PlatformStream.P_PARALLEL));
        assertEquals(null, ps.getProperty(PlatformStream.P_BACKLOG));
        assertEquals(null, ps.getProperty(PlatformStream.P_RECURRENT));
    }

    @Test
    public void test004_factoryRVS() {
        VirtualStream vs = StreamFactory.getRecurrentVirtualStream(UUID.randomUUID(), 100, true);
        assertEquals("100", vs.getProperty(PlatformStream.P_BACKLOG));
        assertEquals("true", vs.getProperty(PlatformStream.P_RECURRENT));
        assertEquals("true", vs.getProperty(PlatformStream.P_EPHEMERAL));
        assertEquals(1, vs.getWeight());
        assertEquals(null, vs.getProperty(PlatformStream.P_BANDWIDTH));
        assertEquals(null, vs.getProperty(PlatformStream.P_PARALLEL));
    }

    @Test
    public void test004_factoryRPS() {
        PlatformStream ps = StreamFactory.getRecurrentPlatformStream(UUID.randomUUID(), 100);
        assertEquals(null, ps.getProperty(PlatformStream.P_BANDWIDTH));
        assertEquals(null, ps.getProperty(PlatformStream.P_PARALLEL));
        assertEquals("100", ps.getProperty(PlatformStream.P_BACKLOG));
        assertEquals("true", ps.getProperty(PlatformStream.P_RECURRENT));
    }


/*	@Test
	public void test001() throws Exception {
		
		 * ParallelPlatformStream
		 
		assertNotEquals(null, svc.createParallelPlatformStream(100));
	}

	@Test
	public void test002()  throws Exception {
		
		 * ParallelPlatformStreamDefault
		 
		assertNotEquals(null, svc.createParallelPlatformStream());
	}
	
	@Test
	public void test003() {
		
		 * RecurrentPlatformStream
		 
		assertNotEquals(null, svc.createRecurrentPlatformStream(200));
	}
	
	@Test
	public void test004() throws Exception {
		
		 * RecurrentPlatformStreamDefault
		 
		assertNotEquals(null, svc.createRecurrentPlatformStream());				
	}
	
	@Test
	public void test005() {
		
		 * GetParallelPlatformStreamByParam
		 
		ArrayList<UUID> l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, StreamScraperMgmtService.PPROP_BANDWIDTH, "10");
		assertEquals(0, l.size());
		svc.createParallelPlatformStream(10);
		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, StreamScraperMgmtService.PPROP_BANDWIDTH, "10");
		assertEquals(1, l.size());
		svc.createParallelPlatformStream(10);
		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, StreamScraperMgmtService.PPROP_BANDWIDTH, "10");
		assertEquals(2, l.size());
	}

	@Test
	public void test006() {
		
		 * GetRecurrentPlatformStreamByParam
		 
		ArrayList<UUID> l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_RECURRENT, StreamScraperMgmtService.PPROP_BACKLOG, "10");
		assertEquals(0,l.size());
		svc.createRecurrentPlatformStream(10);
		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_RECURRENT, StreamScraperMgmtService.PPROP_BACKLOG, "10");
		assertEquals(1,l.size());

		svc.createRecurrentPlatformStream(10);
		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_RECURRENT, StreamScraperMgmtService.PPROP_BACKLOG, "10");
		assertEquals(2,l.size());
	}
	
	@Test
	public void test007() throws Exception {
		 VirtualStreamRecurrentEphemeralCreate
		 * when we create ephemeral vstream it should be mapped on existing only-ephemeral pstream 
		 * of the same bandwidth. If backlog is different, then other pstream will be created. 
		 

		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
									StreamScraperMgmtService.VTYPE_RECURRENT, 
									StreamScraperMgmtService.VTYPE_EPHEMERAL, 
									StreamScraperMgmtService.PPROP_BACKLOG, 
									"99", "");
		
		ArrayList<UUID> l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_RECURRENT, StreamScraperMgmtService.PPROP_BACKLOG, "99");
		assertEquals(1,l.size());

		
		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
									StreamScraperMgmtService.VTYPE_RECURRENT, 
									StreamScraperMgmtService.VTYPE_EPHEMERAL, 
									StreamScraperMgmtService.PPROP_BACKLOG, 
									"99", "");

		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_RECURRENT, StreamScraperMgmtService.PPROP_BACKLOG, "99");
		assertEquals(1,l.size());

		
		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
									StreamScraperMgmtService.VTYPE_RECURRENT, 
									StreamScraperMgmtService.VTYPE_EPHEMERAL, 
									StreamScraperMgmtService.PPROP_BACKLOG, 
									"99", "");

		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_RECURRENT, StreamScraperMgmtService.PPROP_BACKLOG, "99");
		assertEquals(1,l.size());

		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
				StreamScraperMgmtService.VTYPE_RECURRENT, 
				StreamScraperMgmtService.VTYPE_EPHEMERAL, 
				StreamScraperMgmtService.PPROP_BACKLOG, 
				"100", "");

		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_RECURRENT, StreamScraperMgmtService.PPROP_BACKLOG, "99");
		assertEquals(1,l.size());
		
		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_RECURRENT, StreamScraperMgmtService.PPROP_BACKLOG, "100");
		assertEquals(1,l.size());
		
	}
	
	@Test
	public void test008() throws Exception {
		
		 * VirtualStreamParallelEphemeralCreate
		 * when we create ephemeral vstream it should be mapped on existing only-ephemeral pstream 
		 * of the same bandwidth. If bandwidth is different, then other pstream will be created. 
		 
		
		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
									StreamScraperMgmtService.VTYPE_PARALLEL, 
									StreamScraperMgmtService.VTYPE_EPHEMERAL, 
									StreamScraperMgmtService.PPROP_BANDWIDTH, 
									"10", "");

		ArrayList<UUID> l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, 
															StreamScraperMgmtService.PPROP_BANDWIDTH, "10");
		assertEquals(1,l.size());

		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
				StreamScraperMgmtService.VTYPE_PARALLEL, 
				StreamScraperMgmtService.VTYPE_EPHEMERAL, 
				StreamScraperMgmtService.PPROP_BANDWIDTH, 
				"10", "");

		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, 
				StreamScraperMgmtService.PPROP_BANDWIDTH, "10");
		assertEquals(1,l.size());

		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
				StreamScraperMgmtService.VTYPE_PARALLEL, 
				StreamScraperMgmtService.VTYPE_EPHEMERAL, 
				StreamScraperMgmtService.PPROP_BANDWIDTH, 
				"12", "");

		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, 
				StreamScraperMgmtService.PPROP_BANDWIDTH, "10");
		assertEquals(1,l.size());

		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, 
				StreamScraperMgmtService.PPROP_BANDWIDTH, "12");
		assertEquals(1,l.size());
		
	}

	@Test
	public void test009() throws Exception {
		
		 * VirtualStreamParallelSolidCreate
		 * when we create new vstream as solid it should be mapped to free pstream 
		 * (or to newly created pstream)
		 
		UUID ps1 = svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
									StreamScraperMgmtService.VTYPE_PARALLEL, 
									StreamScraperMgmtService.VTYPE_SOLID, 
									StreamScraperMgmtService.PPROP_BANDWIDTH, 
									"10", "");

		ArrayList<UUID> l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, 
															StreamScraperMgmtService.PPROP_BANDWIDTH, "10");
		assertEquals(1,l.size());

		UUID ps2 = svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
									StreamScraperMgmtService.VTYPE_PARALLEL, 
									StreamScraperMgmtService.VTYPE_SOLID, 
									StreamScraperMgmtService.PPROP_BANDWIDTH, 
									"10", "");

		assertNotEquals(ps1, ps2);
				
		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, 
				StreamScraperMgmtService.PPROP_BANDWIDTH, "10");

		assertEquals(2,l.size());

		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
									StreamScraperMgmtService.VTYPE_PARALLEL, 
									StreamScraperMgmtService.VTYPE_EPHEMERAL, 
									StreamScraperMgmtService.PPROP_BANDWIDTH, 
									"10", "");

		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, 
									StreamScraperMgmtService.PPROP_BANDWIDTH, "10");
		
		assertEquals(3,l.size());

		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
									StreamScraperMgmtService.VTYPE_PARALLEL, 
									StreamScraperMgmtService.VTYPE_EPHEMERAL, 
									StreamScraperMgmtService.PPROP_BANDWIDTH, 
									"10", "");

		l = svc.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, 
									StreamScraperMgmtService.PPROP_BANDWIDTH, "10");
		assertEquals(3,l.size());

		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
									StreamScraperMgmtService.VTYPE_PARALLEL, 
									StreamScraperMgmtService.VTYPE_SOLID, 
									StreamScraperMgmtService.PPROP_BANDWIDTH, 
									"10", "");

		assertEquals(4,l.size());
		
	}

	@Test
	public void test010() throws Exception {
		
		 * VirtualStreamRecurrentSolidCreate
		 
		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
									StreamScraperMgmtService.VTYPE_RECURRENT, 
									StreamScraperMgmtService.VTYPE_SOLID, 
									StreamScraperMgmtService.PPROP_BACKLOG, 
									"10", "");
	}
*/
}
