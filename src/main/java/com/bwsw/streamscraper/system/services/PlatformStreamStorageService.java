package com.bwsw.streamscraper.system.services;

import com.bwsw.streamscraper.system.models.PlatformStream;
import com.bwsw.streamscraper.system.models.StreamScraperMgmtService;

import java.util.UUID;

public class PlatformStreamStorageService {
	StreamScraperMgmtService service;
	
	public PlatformStreamStorageService(StreamScraperMgmtService svc) {
		service = svc;
	}
	
	public void save(PlatformStream s) {
		for(String name: s.getPropertyList())
		{
			String p = s.getProperty(name);
			service.getSession().execute(service.insertPstreamPropertiesStmt.bind(s.getID(),name, p));
		}
		
		if(null != s.getVirtualStreams())
			for(UUID id: s.getVirtualStreams().keySet()) 
			{
				service.getSession().execute(service.insertPstreamVstreamsStmt.bind(s.getID(),id));
			}
	}

}
