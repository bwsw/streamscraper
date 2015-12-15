package com.bwsw.streamscraper.system;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Properties;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.Collections;



//import com.datastax.driver.core;
//import com.datastax.driver.core;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;


public class StreamScraperMgmtService {

	Cluster cluster;
	Session session;
	Properties props;
	
	public BoundStatement insertPstreamPropertiesStmt;
	public BoundStatement insertPstreamVstreamsStmt;	
	
	BoundStatement insertPstreamStmt;
	BoundStatement insertPstreamPropertyStmt;
	BoundStatement selectStreamByPropertyStmt;
	BoundStatement selectVstreamByNameStmt;
	BoundStatement updatePstreamVstreamCntrsStmt;
	BoundStatement selectPstreamVstreamCntrsStmt;
	BoundStatement insertVstreamMappingStmt;
	BoundStatement insertReverseVstreamMappingStmt;
	BoundStatement insertPstreamSolidStateStmt;
	BoundStatement selectPstreamSolidStateStmt;
	
	public static final int PSTYPE_RECURRENT = 1;
	public static final int PSTYPE_PARALLEL = 2;

	public static final int VTYPE_EPHEMERAL = 1;
	public static final int VTYPE_SOLID = 2;
	
	public static final int VTYPE_PARALLEL = PSTYPE_PARALLEL;
	public static final int VTYPE_RECURRENT = PSTYPE_RECURRENT;
	
	public static final String PPROP_BACKLOG = "backlog";
	public static final String PPROP_BANDWIDTH = "bandwidth";	
	
	public Session getSession() {
		return session;
	}
	
	public StreamScraperMgmtService() throws Exception {
		props = new Properties();
		FileInputStream in = new FileInputStream("config.properties");
		props.load(in);
		in.close();	

		String cassandra_ip = props.getProperty("streamscraper.cassandra.ip");
		if (cassandra_ip == null)
			throw  new Exception("Failed to find 'streamscraper.cassandra.ip' in properties file.");		
		
		cluster = Cluster.builder().addContactPoint(cassandra_ip).build();
		session = cluster.connect("streamscraper");	
	}

	public void begin() {
		
		PreparedStatement statement;
		statement = session.prepare("INSERT INTO pstreams (stype, id) VALUES(?, ?)");
		insertPstreamStmt = new BoundStatement(statement);		

		//----------------------------------------------------------------------------------
		statement = session.prepare("INSERT INTO pstream (id, key, value) VALUES(?, ?, ?)");
		insertPstreamPropertiesStmt = new BoundStatement(statement);
		//----------------------------------------------------------------------------------
		statement = session.prepare("INSERT INTO pstream_vstream (pstreamid, vstreamid) VALUES(?, ?)");
		insertPstreamVstreamsStmt = new BoundStatement(statement);

		
		//----------------------------------------------------------------------------------
		statement = session.prepare("INSERT INTO pstreams_properties (pstype, key, value, stream_id) " + 
									"VALUES(?, ?, ?, ?)");
		insertPstreamPropertyStmt = new BoundStatement(statement);		

		statement = session.prepare("SELECT stream_id FROM pstreams_properties WHERE " + 
									" pstype = ? AND key = ? AND value = ?");
		selectStreamByPropertyStmt = new BoundStatement(statement);		

		statement = session.prepare("SELECT vstream FROM streammap WHERE vstream = ?");
		selectVstreamByNameStmt = new BoundStatement(statement);		

		statement = session.prepare("UPDATE pstreams_counters SET vstream_ctr = vstream_ctr + 1 " + 
									"WHERE stream_id = ?");
		updatePstreamVstreamCntrsStmt = new BoundStatement(statement);

		statement = session.prepare("SELECT vstream_ctr FROM pstreams_counters WHERE stream_id = ?");
		selectPstreamVstreamCntrsStmt = new BoundStatement(statement);

		statement = session.prepare("INSERT INTO streammap (vstream, handler, pstream, vsdeployment_type, vstype) " + 
									"VALUES(?, ?, ?, ?, ?)");
		insertVstreamMappingStmt = new BoundStatement(statement);		

		statement = session.prepare("INSERT INTO streamrmap (vstype, pstype, pstream , vstream) " + 
									"VALUES(?, ?, ?, ?)");
		insertReverseVstreamMappingStmt = new BoundStatement(statement);		

		statement = session.prepare("INSERT INTO pstreams_solid (pstream, is_solid) VALUES(?, true)");
		insertPstreamSolidStateStmt = new BoundStatement(statement);		

		statement = session.prepare("SELECT is_solid FROM pstreams_solid WHERE pstream = ?");
		selectPstreamSolidStateStmt = new BoundStatement(statement);		
	}
	
	public void initDb() 
	{
		finiDb();

		session.execute("CREATE TABLE IF NOT EXISTS pstream (id UUID, key text, value text, " + 
				" PRIMARY KEY (id, key))");

		session.execute("CREATE TABLE IF NOT EXISTS pstream_vstream (pstreamid UUID, vstreamid UUID, " + 
				" PRIMARY KEY (pstreamid, vstreamid))");
		
		
		session.execute("CREATE TABLE IF NOT EXISTS pstreams (stype int, id uuid, PRIMARY KEY (stype, id))");
		session.execute("CREATE TABLE IF NOT EXISTS pstreams_properties (pstype int, key text, value text, " + 
						" stream_id uuid, PRIMARY KEY (pstype, key, value, stream_id))");
		session.execute("CREATE TABLE IF NOT EXISTS pstreams_counters (stream_id uuid PRIMARY KEY, vstream_ctr counter)");
		session.execute("CREATE TABLE IF NOT EXISTS pstreams_solid (pstream uuid PRIMARY KEY, is_solid boolean)");
		session.execute("CREATE TABLE IF NOT EXISTS streammap (vstream text PRIMARY KEY, handler blob,pstream uuid, " + 
						"vsdeployment_type int, vstype int)");
		session.execute("CREATE TABLE IF NOT EXISTS streamrmap (vstype int, pstype int, pstream uuid, vstream text, " + 
						" PRIMARY KEY (vstype, pstype, pstream))");
	}
	
	public void finiDb() {
		session.execute("TRUNCATE pstream");		
		session.execute("TRUNCATE pstream_vstream");
		
		session.execute("TRUNCATE streamrmap");
		session.execute("TRUNCATE streammap");
		session.execute("TRUNCATE pstreams_solid");
		session.execute("TRUNCATE pstreams_counters");
		session.execute("TRUNCATE pstreams_properties");
		session.execute("TRUNCATE pstreams");
	}

	private UUID createGenericStream(int pstype, String property_name, int property_value) {
		UUID uuid = UUID.randomUUID();
		session.execute(insertPstreamStmt.bind(pstype, uuid));
		session.execute(insertPstreamPropertyStmt.bind(pstype, property_name, Integer.toString(property_value) , uuid));
		return uuid;
		
	}

	// creates recurrent stream
	// stub
	public UUID createRecurrentPlatformStream(int history_backlog) {
		return createGenericStream(PSTYPE_RECURRENT, PPROP_BACKLOG, history_backlog);	
	}

	// creates default recurrent stream
	// stub
	public UUID createRecurrentPlatformStream() throws Exception {
		String default_backlog = props.getProperty("streamscraper.streams.recurrent.default_backlog");
		if (default_backlog == null)
			throw  new Exception("Failed to find 'streamscraper.streams.parallel.default_bandwidth' in properties file.");
		return createRecurrentPlatformStream(Integer.parseInt(default_backlog));
	}

	// returns ArrayList of pstream UUIDs found by specific parameter of key & value
	//
	public ArrayList<UUID> getPlatformStreamsByParam(int pstype, String key, String value) {
		ArrayList<UUID> l = new ArrayList<UUID>();
		ResultSet results = session.execute(selectStreamByPropertyStmt.bind(pstype, key, value));		
		for (Row row: results) {
			UUID uuid = row.getUUID("stream_id");
			l.add(uuid);
		}
		return l;
	}
	
	// allows to create generic vstream mapped to specific pstream
	//
	public UUID createGenericVirtualStream(String vsname, 
											int vsdeployment_type, 
											int vstype, 
											String key, 
											String value, 
											String handler) throws Exception {
		
		if (vstype == VTYPE_EPHEMERAL && handler == null) {
			throw new Exception("Handler is NULL. If vstream is ephemeral, then handler should be provided.");
		}
		
		if (key == null || value == null) {
			throw new Exception("Parameter key and Parameter value should be non-null strings.");			
		}
		
		// check if duplicate name
		ResultSet r = session.execute(selectVstreamByNameStmt.bind(vsname));
		if (!r.all().isEmpty()) {
			throw new DuplicateVstreamException("Stream with vsname = '" + vsname + "' already exists! Unable to add one more!");
		}
		// find suitable pstreams
		ArrayList<UUID> suitable_pstreams = getPlatformStreamsByParam(vsdeployment_type, key, value);
		if(suitable_pstreams.isEmpty()) {
			suitable_pstreams.add(createGenericStream(vsdeployment_type, key, Integer.parseInt(value)));
		}
		
		UUID pstream = null;
		if (vstype == VTYPE_SOLID) {
			// find stream with no counter set
			for (UUID u: suitable_pstreams) {
				r = session.execute(selectPstreamVstreamCntrsStmt.bind(u));
				if (r.all().isEmpty()) {
					// ok, this pstream is ok
					pstream = u;
					break;
				}
			}
			if (pstream == null) {
				// all pstreams are busy
				pstream = createGenericStream(vsdeployment_type, key, Integer.parseInt(value));
			}
		} 
		else if (vstype == VTYPE_EPHEMERAL) {
			
			long minArray[] = new long[suitable_pstreams.size()];
			for (int i = 0 ; i < suitable_pstreams.size(); i++) minArray[i] = -1;
			
			// iterate over suitable pstreams to find one with minimal counter (less used)
			int ctr = 0;
			for (UUID u: suitable_pstreams) {
				r = session.execute(selectPstreamSolidStateStmt.bind(u));
				if(r.all().isEmpty()) {
					r = session.execute(selectPstreamVstreamCntrsStmt.bind(u));
					List<Row> data = r.all();
					if (data.isEmpty()) {
						// if counter is absent -> 0
						minArray[ctr]= 0;
					} else {
						// if counter exists then get it
						minArray[ctr] = data.get(0).getLong("vstream_ctr");
						//System.out.print(new Long(data.get(0).getLong("vstream_ctr")));
					}			
				}
				ctr ++;
			}
			
			// check if at least one pstream filled with counter
			boolean found = false;
			for(int i=0 ; i < minArray.length ; i++)
			{
				if (minArray[i] > - 1)
				{
					found = true;
					break;
				}
			}
			if (found) 
			{
				// none suitable streams found, create one
				pstream = createGenericStream(vsdeployment_type, key, Integer.parseInt(value));
			} 
			else {
				// find minimal one
				int min_idx = 0;
				for (int i = 0; i < minArray.length;i++) {
					if (minArray[min_idx] > minArray[i])
						min_idx = i;
				}
				// assign pstream min load stream
				pstream = suitable_pstreams.get(min_idx);
			}
		} else {
			throw new Exception("Unknown vstream deployment type");
		}
		
		// create vstream mapping
		ByteBuffer bbuf = null;
		if (handler != null)
			bbuf = ByteBuffer.wrap(handler.getBytes());
		
		session.execute(insertVstreamMappingStmt.bind(vsname, bbuf, pstream, vsdeployment_type, vstype));
		// create reverse mapping
		session.execute(insertReverseVstreamMappingStmt.bind(vstype, vsdeployment_type, pstream, vsname));
		// update counters
		session.execute(updatePstreamVstreamCntrsStmt.bind(pstream));
		if (vstype == VTYPE_SOLID) {
			// update solid state if vstype should be solid
			session.execute(insertPstreamSolidStateStmt.bind(pstream));
		}
		return pstream;
	}
	
	// creates parallel stream
	// stub
	public UUID createParallelPlatformStream(int bandwidth) {
		return createGenericStream(PSTYPE_PARALLEL, PPROP_BANDWIDTH, bandwidth);
	}
	
	// creates default parallel stream
	// stub
	public UUID createParallelPlatformStream() throws Exception {
		String default_bandwidth = props.getProperty("streamscraper.streams.parallel.default_bandwidth");
		if (default_bandwidth == null)
			throw  new Exception("Failed to find 'streamscraper.streams.parallel.default_bandwidth' in properties file.");
		return createParallelPlatformStream(Integer.parseInt(default_bandwidth));
	}
	
	// ends communication with management system
	// 
	public void end() {
		cluster.close();
	}
}