package com.bwsw.streamscraper.tests;

import com.bwsw.streamscraper.system.*;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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
        VirtualStream vs1 = new VirtualStream("test", 10);

        try {
				ps.addVirtualStream(vs1);
				assertEquals(1, 0);
		} catch (IncompatibleStreamException e) {
			assertEquals(0, 0);
		}
	}

    @Ignore
    public void test004_factoryPVS() throws ImpossibleStreamException {
/*		VirtualStream vs = StreamFactory.getParallelVirtualStream(UUID.randomUUID(), 10, true);
        assertNotEquals(null, vs);
		assertEquals("10", vs.getProperty(PlatformStream.P_BANDWIDTH));
        assertEquals("true", vs.getProperty(PlatformStream.P_PARALLEL));
        assertEquals("true", vs.getProperty(PlatformStream.P_EPHEMERAL));
        assertEquals(1, vs.getWeight());
        assertEquals(null, vs.getProperty(PlatformStream.P_BACKLOG));
        assertEquals(null, vs.getProperty(PlatformStream.P_RECURRENT));
*/
    }

	@Test
    public void test004_factoryPPS() throws ImpossibleStreamException {
        ParallelPlatformStream ps = new ParallelPlatformStream(10);
        assertEquals(10, ps.getBandwidth());
        assertEquals("true", ps.getProperty(PlatformStream.P_PARALLEL));
        assertEquals(null, ps.getProperty(PlatformStream.P_BACKLOG));
        assertEquals(null, ps.getProperty(PlatformStream.P_RECURRENT));

        try {
            ps = new ParallelPlatformStream(1);
            assertTrue(false);
        } catch (ImpossibleStreamException e) {
            assertTrue(true);
        }

        try {
            ps = new ParallelPlatformStream(0);
            assertTrue(false);
        } catch (ImpossibleStreamException e) {
            assertTrue(true);
        }

    }

    @Test
    public void test004_factoryRVS() throws ImpossibleStreamException {
        ParallelVirtualStream vs;
    }

    @Test
    public void test004_factoryRPS() throws ImpossibleStreamException {
        RecurrentPlatformStream ps = new RecurrentPlatformStream(UUID.randomUUID(), 100);
        assertEquals(100, ps.getBacklogLength());
        assertEquals(null, ps.getProperty(PlatformStream.P_BANDWIDTH));
        assertEquals(null, ps.getProperty(PlatformStream.P_PARALLEL));
        assertEquals("100", ps.getProperty(PlatformStream.P_BACKLOG));
        assertEquals("true", ps.getProperty(PlatformStream.P_RECURRENT));

        try {
            ps = new RecurrentPlatformStream(-1);
            assertTrue(false);
        } catch (ImpossibleStreamException e) {
            assertTrue(true);
        }

    }


}
