package com.bwsw.streamscraper.system.services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.io.FileInputStream;
import java.util.Properties;


public class CassandraStreamManagementService implements IStreamManagementService {


	Cluster cluster;
	Session session;
    Properties properties;

	public CassandraStreamManagementService() throws Exception {
        properties = new Properties();
        FileInputStream in = new FileInputStream("config.properties");
        properties.load(in);
        in.close();

        String cassandra_ip = properties.getProperty("streamscraper.cassandra.ip");
        if (cassandra_ip == null)
			throw new Exception("Failed to find 'streamscraper.cassandra.ip' in properties file.");

		cluster = Cluster.builder().addContactPoint(cassandra_ip).build();

        String cassandra_db = properties.getProperty("streamscraper.cassandra.db");
        if (null == cassandra_db)
            cassandra_db = "streamscraper";
        session = cluster.connect(cassandra_db);
    }

    public String getProperty(String pname) {
        return properties.getProperty(pname);
    }

	public Session getSession() {
		return session;
	}

	public void begin() {
		
	}
	
	public void initDb() 
	{
		finiDb();

		session.execute("CREATE TABLE IF NOT EXISTS pstream (id UUID, key text, value text, " + 
				" PRIMARY KEY (id, key))");

		session.execute("CREATE TABLE IF NOT EXISTS pstream_vstream (pstreamid UUID, vstreamid UUID, " + 
				" PRIMARY KEY (pstreamid, vstreamid))");
		
	}
	
	public void finiDb() {
		session.execute("TRUNCATE pstream");		
		session.execute("TRUNCATE pstream_vstream");
		
	}

	// ends communication with management system
	// 
	public void end() {
		cluster.close();
	}
}