

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.bwsw.streamscraper.system.StreamScraperMgmtService;
import com.bwsw.streamscraper.tests.StreamsStorageUnitTests;
import com.bwsw.streamscraper.tests.StreamsUnitTests;

import java.util.ArrayList;
import java.util.UUID;

public class CassandraMainTest {
		private static StreamScraperMgmtService m;
        public static void main(String[] args) {
            Result result = JUnitCore.runClasses(StreamsUnitTests.class, StreamsStorageUnitTests.class);
            for (Failure failure : result.getFailures()) {
              System.out.println(failure.toString());
            }
	}

}