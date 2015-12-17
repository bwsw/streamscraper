package com.bwsw.streamscraper.system.services;

import com.bwsw.streamscraper.system.models.*;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

import java.util.UUID;

public class StreamStorageService {

    StreamScraperMgmtService service;

    BoundStatement insertPstreamPropertiesStmt;
    BoundStatement insertPstreamVstreamsStmt;

    public StreamStorageService(StreamScraperMgmtService svc) {
        service = svc;
        init();
    }

    public Session getSession() {
        return service.getSession();
    }

    public void init() {
        //----------------------------------------------------------------------------------
        PreparedStatement statement = getSession().prepare("INSERT INTO pstream (id, key, value) VALUES(?, ?, ?)");
        insertPstreamPropertiesStmt = new BoundStatement(statement);
        //----------------------------------------------------------------------------------
        statement = getSession().prepare("INSERT INTO pstream_vstream (pstreamid, vstreamid) VALUES(?, ?)");
        insertPstreamVstreamsStmt = new BoundStatement(statement);
    }

    private void saveProperties(PlatformStream ps) {
        for (String name : ps.getPropertyList()) {
            String p = ps.getProperty(name);
            service.getSession().execute(insertPstreamPropertiesStmt.bind(ps.getID(), name, p));
        }
    }

    private void saveConnectedVirtualStreams(PlatformStream ps) {
        if (null != ps.getVirtualStreams()) {
            for (UUID id : ps.getVirtualStreams().keySet()) {
                if (ps.getVirtualStream(id).getHasChanges())
                    save(ps.getVirtualStream(id));
                service.getSession().execute(insertPstreamVstreamsStmt.bind(ps.getID(), id));
            }
        }
    }

    public void platformStreamBasicSave(PlatformStream ps) {
        saveProperties(ps);
        saveConnectedVirtualStreams(ps);
    }

    private void virtualStreamBasicSave(VirtualStream vs) {
        saveProperties(vs);
    }

    public void save(PlatformStream s) {
        if (s.getClass().equals(ParallelPlatformStream.class)) {
            s.setHasChanges(false);
            return;
        }
        if (s.getClass().equals(RecurrentPlatformStream.class)) {
            s.setHasChanges(false);
            return;
        }
    }

    public void save(VirtualStream s) {
        if (s.getClass().equals(ParallelVirtualStream.class)) {
            virtualStreamBasicSave(s);
            s.setHasChanges(false);
            return;
        }
        if (s.getClass().equals(RecurrentVirtualStream.class)) {
            virtualStreamBasicSave(s);
            s.setHasChanges(false);
            return;
        }
    }
}