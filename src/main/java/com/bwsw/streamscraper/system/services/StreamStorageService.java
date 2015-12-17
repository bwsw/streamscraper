package com.bwsw.streamscraper.system.services;

import com.bwsw.streamscraper.system.models.*;

import java.util.UUID;

public class StreamStorageService {
    StreamScraperMgmtService service;

    public StreamStorageService(StreamScraperMgmtService svc) {
        service = svc;
    }
	
	public void save(PlatformStream s) {
		for(String name: s.getPropertyList())
		{
			String p = s.getProperty(name);
			service.getSession().execute(service.insertPstreamPropertiesStmt.bind(s.getID(),name, p));
		}

        if (null != s.getVirtualStreams()) {
            for (UUID id : s.getVirtualStreams().keySet()) {
                service.getSession().execute(service.insertPstreamVstreamsStmt.bind(s.getID(), id));
            }
        }
    }

    public void save(VirtualStream v) {

    }

    public void save(ParallelVirtualStream v) {

    }

    public void save(ParallelPlatformStream p) {

    }

    public void save(RecurrentVirtualStream v) {

    }

    public void save(RecurrentPlatformStream p) {

    }

}
