package com.bwsw.streamscraper.system;

import java.util.UUID;

import org.junit.runners.model.InitializationError;

import java.util.HashMap;
import java.util.ArrayList;

public class PlatformStream {

	UUID psid;
	HashMap<String, String> properties;
	HashMap<UUID, VirtualStream> vstreams;
	
	public PlatformStream() {
		// TODO Auto-generated constructor stub
		psid = UUID.randomUUID();
		properties = new HashMap<String, String>();
		vstreams = new HashMap<UUID, VirtualStream>();
	}

	/**
	 * 
	 * @param id - stream ID
	 */
	public PlatformStream(UUID id) {
		this.psid = id;
	}

	/**
	 * 
	 * @param id - stream ID
	 * 
	 */
	public PlatformStream setID(UUID id) {
		this.psid = id;
		return this;
	}
	
	public PlatformStream setProperty(String name, String value) {
		properties.put(name, value);
		return this;
	}
	
	public String getProperty(String name) {
		return properties.get(name);
	}
	
	
	public UUID getID() {
		return psid;
	}
	
	public HashMap<UUID, VirtualStream> getVstreams() {
		return vstreams;
	}
	
	public PlatformStream addVirtualstream(VirtualStream s) throws DuplicateVstreamException {
		if (vstreams.containsKey(s.getID()))
			throw new DuplicateVstreamException("Virtual stream `" + s.getID().toString() + 
												"' duplicate entry in `" + this.getID().toString() + "'");
		vstreams.put(s.getID(), s);
		return this;
	}
	
	public PlatformStream getVirtualStream(UUID id) {
		return vstreams.get(id);
	}
	
}
