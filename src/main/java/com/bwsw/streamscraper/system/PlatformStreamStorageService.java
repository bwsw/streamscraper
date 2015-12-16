package com.bwsw.streamscraper.system;

import java.util.UUID;

import javax.swing.plaf.basic.BasicScrollPaneUI.VSBChangeListener;

public class PlatformStreamStorageService {
	StreamScraperMgmtService service;
	
	public PlatformStreamStorageService(StreamScraperMgmtService svc) {
		// TODO Auto-generated constructor stub
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
