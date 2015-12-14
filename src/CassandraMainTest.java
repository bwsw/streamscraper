import java.util.ArrayList;
import java.util.UUID;

import bitworks.streamscraper.system.StreamScraperMgmtService;

public class CassandraMainTest {
		private static StreamScraperMgmtService m;
        public static void main(String[] args) {
		try {
			m = new StreamScraperMgmtService();
//			System.err.println(m.createParallelPlatformStream(100));
//			System.err.println(m.createParallelPlatformStream());
//			System.err.println(m.createRecurrentPlatformStream(200));
//			System.err.println(m.createRecurrentPlatformStream());
//			ArrayList<UUID> l = m.getPlatformStreamsByParam(StreamScraperMgmtService.PSTYPE_PARALLEL, "bandwidth", "10");
//			for (UUID u: l) {
//				System.err.println(u);
//				
//			}
			m.createGenericVirtualStream(UUID.randomUUID().toString(), 
											StreamScraperMgmtService.VTYPE_RECURRENT, 
											StreamScraperMgmtService.VTYPE_EPHEMERAL, 
											"history", 
											"99", 
											"function (){}");	
			m.end();
		}
		catch(Exception e) {
			System.err.println(e);
			m.end();
		};
	}

}