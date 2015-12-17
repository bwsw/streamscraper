package com.bwsw.streamscraper.tests;

import com.bwsw.streamscraper.system.exceptions.DuplicateVstreamException;
import com.bwsw.streamscraper.system.exceptions.ImpossibleStreamException;
import com.bwsw.streamscraper.system.exceptions.IncompatibleStreamException;
import com.bwsw.streamscraper.system.models.*;
import com.bwsw.streamscraper.system.services.CassandraStreamManagementService;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.UUID;

import static org.junit.Assert.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StreamsUnitTests {

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
    }


	@Test
    public void test004_PPS() throws ImpossibleStreamException {
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
    public void test005_PVS() throws ImpossibleStreamException {
        ParallelVirtualStream vs = new ParallelVirtualStream("test", 1, 10, true);
        assertNotNull(vs);
        assertEquals("test", vs.getName());
        assertEquals(1, vs.getWeight());
        assertEquals(10, vs.getBandwidth());
        assertEquals(true, vs.isEphemeral());
        assertEquals("true", vs.getProperty(PlatformStream.P_PARALLEL));
    }

    @Test
    public void test006_RPS() throws ImpossibleStreamException {
        RecurrentPlatformStream ps = new RecurrentPlatformStream(UUID.randomUUID(), 100);
        assertEquals(100, ps.getBacklogLength());
        assertEquals(null, ps.getProperty(PlatformStream.P_BANDWIDTH));
        assertEquals(null, ps.getProperty(PlatformStream.P_PARALLEL));
        assertEquals("true", ps.getProperty(PlatformStream.P_RECURRENT));

        try {
            ps = new RecurrentPlatformStream(-1);
            assertTrue(false);
        } catch (ImpossibleStreamException e) {
            assertTrue(true);
        }

    }

    @Test
    public void test007_RVS() throws ImpossibleStreamException {
        RecurrentVirtualStream vs = new RecurrentVirtualStream("test", 1, 10, true);
        assertNotNull(vs);
        assertEquals("test", vs.getName());
        assertEquals(1, vs.getWeight());
        assertEquals(10, vs.getBacklogLength());
        assertEquals(true, vs.isEphemeral());
        assertEquals(null, vs.getProperty(PlatformStream.P_BANDWIDTH));
        assertEquals(null, vs.getProperty(PlatformStream.P_PARALLEL));
        assertEquals("true", vs.getProperty(PlatformStream.P_RECURRENT));
    }

    @Test
    public void test008_StreamAttach1()
            throws ImpossibleStreamException,
            DuplicateVstreamException,
            IncompatibleStreamException {
        ParallelPlatformStream ps = new ParallelPlatformStream(10);
        ParallelVirtualStream vs = new ParallelVirtualStream("test", 1, 10, true);

        vs.setHasChanges(false);
        ps.setHasChanges(false);

        try {
            ps.addVirtualStream(vs);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void test009_StreamAttach2()
            throws ImpossibleStreamException,
            DuplicateVstreamException,
            IncompatibleStreamException {
        ParallelPlatformStream ps = new ParallelPlatformStream(10);
        ParallelVirtualStream vs = new ParallelVirtualStream("test", 1, 20, true);

        vs.setHasChanges(false);
        ps.setHasChanges(false);

        try {
            ps.addVirtualStream(vs);
            assertTrue(false);
        } catch (IncompatibleStreamException e) {
            assertTrue(true);
        }
    }

    @Test
    public void test010_StreamAttach3()
            throws ImpossibleStreamException,
            DuplicateVstreamException,
            IncompatibleStreamException {
        ParallelPlatformStream ps = new ParallelPlatformStream(10);
        RecurrentVirtualStream vs = new RecurrentVirtualStream("test", 1, 100, true);

        vs.setHasChanges(false);
        ps.setHasChanges(false);

        try {
            ps.addVirtualStream(vs);
            assertTrue(false);
        } catch (IncompatibleStreamException e) {
            assertTrue(true);
        }
    }

    @Test
    public void test011_StreamAttach4()
            throws ImpossibleStreamException,
            DuplicateVstreamException,
            IncompatibleStreamException {
        RecurrentPlatformStream ps = new RecurrentPlatformStream(100);
        RecurrentVirtualStream vs = new RecurrentVirtualStream("test", 1, 100, true);

        vs.setHasChanges(false);
        ps.setHasChanges(false);

        try {
            ps.addVirtualStream(vs);
            assertTrue(true);
        } catch (IncompatibleStreamException e) {
            System.err.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void test012_StreamAttach5()
            throws ImpossibleStreamException,
            DuplicateVstreamException,
            IncompatibleStreamException {
        RecurrentPlatformStream ps = new RecurrentPlatformStream(100);
        RecurrentVirtualStream vs = new RecurrentVirtualStream("test", 1, 200, true);

        vs.setHasChanges(false);
        ps.setHasChanges(false);

        try {
            ps.addVirtualStream(vs);
            assertTrue(false);
        } catch (IncompatibleStreamException e) {
            assertTrue(true);
        }
    }

    @Test
    public void test013_StreamAttach6()
            throws ImpossibleStreamException,
            DuplicateVstreamException,
            IncompatibleStreamException {
        RecurrentPlatformStream ps = new RecurrentPlatformStream(100);
        ParallelVirtualStream vs = new ParallelVirtualStream("test", 1, 200, true);

        vs.setHasChanges(false);
        ps.setHasChanges(false);

        try {
            ps.addVirtualStream(vs);
            assertTrue(false);
        } catch (IncompatibleStreamException e) {
            assertTrue(true);
        }
    }

}
