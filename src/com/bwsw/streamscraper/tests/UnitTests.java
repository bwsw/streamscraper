package com.bwsw.streamscraper.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.runners.MethodSorters;

import com.bwsw.streamscraper.system.*;

import org.junit.FixMethodOrder;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UnitTests {

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
	public void test001() throws Exception {
		/*
		 * ParallelPlatformStream
		 */
		assertNotEquals(null, svc.createParallelPlatformStream(100));
	}

	@Test
	public void test002()  throws Exception {
		/*
		 * ParallelPlatformStreamDefault
		 */
		assertNotEquals(null, svc.createParallelPlatformStream());
	}
	
	@Test
	public void test003() {
		/*
		 * RecurrentPlatformStream
		 */
		assertNotEquals(null, svc.createRecurrentPlatformStream(200));
	}
	
	@Test
	public void test004() throws Exception {
		/*
		 * RecurrentPlatformStreamDefault
		 */
		assertNotEquals(null, svc.createRecurrentPlatformStream());				
	}
	
	@Test
	public void test005() {
		/*
		 * GetParallelPlatformStreamByParam
		 */
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
		/*
		 * GetRecurrentPlatformStreamByParam
		 */
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
		/* VirtualStreamRecurrentEphemeralCreate
		 * when we create ephemeral vstream it should be mapped on existing only-ephemeral pstream 
		 * of the same bandwidth. If backlog is different, then other pstream will be created. 
		 */

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
		/*
		 * VirtualStreamParallelEphemeralCreate
		 * when we create ephemeral vstream it should be mapped on existing only-ephemeral pstream 
		 * of the same bandwidth. If bandwidth is different, then other pstream will be created. 
		 */
		
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
		/*
		 * VirtualStreamParallelSolidCreate
		 * when we create new vstream as solid it should be mapped to free pstream 
		 * (or to newly created pstream)
		 */
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
		/*
		 * VirtualStreamRecurrentSolidCreate
		 */
		svc.createGenericVirtualStream(UUID.randomUUID().toString(), 
									StreamScraperMgmtService.VTYPE_RECURRENT, 
									StreamScraperMgmtService.VTYPE_SOLID, 
									StreamScraperMgmtService.PPROP_BACKLOG, 
									"10", "");
	}

}
