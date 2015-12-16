package com.bwsw.streamscraper.system;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class PlatformStream {

	public static String P_EPHEMERAL 	= "ephemeral";
	public static String P_SOLID 		= "solid";
	public static String P_BANDWIDTH 	= "bandwidth";
	public static String P_BACKLOG 		= "backlog";
	public static String P_PARALLEL 	= "parallel";
	public static String P_RECURRENT 	= "recurrent";
	
	UUID psid;
	HashMap<String, String> properties;
	HashMap<UUID, VirtualStream> vstreams;
	
	boolean has_changes;
	
	public PlatformStream() {
		psid = UUID.randomUUID();
        properties = new HashMap<>();
        vstreams = new HashMap<>();
        has_changes = true;
	}

	/**
	 * 
	 * @param id - stream ID
	 */
	public PlatformStream(UUID id) {
		this.psid = id;
        properties = new HashMap<>();
        vstreams = new HashMap<>();
        has_changes = true;
	}

	public PlatformStream setProperty(String name, String value) {
		properties.put(name, value);
		has_changes = true;
		return this;
	}
	
	public String getProperty(String name) {
		return properties.get(name);
	}
	
	public Set<String> getPropertyList() {
		return properties.keySet();
	}
	
	public boolean getHasChanges() {
		return has_changes;
	}
	
	public void setHasChanges(boolean c) {
		this.has_changes = c;
	}
	
	public UUID getID() {
		return psid;
    }

    /**
     * @param id - stream ID
     */
    public PlatformStream setID(UUID id) {
        this.psid = id;
        has_changes = true;
        return this;
    }
	
	public HashMap<UUID, VirtualStream> getVirtualStreams() {
		return vstreams;
	}
	
	public PlatformStream addVirtualStream(VirtualStream s) 
			throws 
			DuplicateVstreamException, 
			IncompatibleStreamException, 
			ImpossibleStreamException 
	{
		if (vstreams.containsKey(s.getID()))
			throw new DuplicateVstreamException("Virtual stream `" + s.getID().toString() + 
												"' duplicate entry in `" + this.getID().toString() + "'");
		VirtualStream.addPolicyCheck(s, this);
		VirtualStream.updatePolicy(s,this);
		
		vstreams.put(s.getID(), s);
		has_changes = true;
		return this;
	}
	
	public VirtualStream getVirtualStream(UUID id) {
		return vstreams.get(id);
	}

}
