package com.bwsw.streamscraper.system.services;

import com.bwsw.streamscraper.system.exceptions.BadPlatformStreamException;
import com.bwsw.streamscraper.system.exceptions.IncompatibleStreamException;
import com.bwsw.streamscraper.system.models.*;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

import java.util.UUID;

public class CassandraStreamStorageService implements IStreamStorageService {

    CassandraStreamManagementService service;

    BoundStatement insertPstreamPropertiesStmt;
    BoundStatement insertPstreamVstreamsStmt;

    public CassandraStreamStorageService(CassandraStreamManagementService svc) {
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

    /*
    *
    *
    *
    *
     */
    private void saveProperties(PlatformStream ps) {
        for (String name : ps.getPropertyList()) {
            String p = ps.getProperty(name);
            service.getSession().execute(insertPstreamPropertiesStmt.bind(ps.getID(), name, p));
        }
    }

    /*
    *
    *
    *
    * */
    private void saveConnectedVirtualStreams(PlatformStream ps)
            throws IncompatibleStreamException, BadPlatformStreamException {
        if (null != ps.getVirtualStreams()) {
            for (UUID id : ps.getVirtualStreams().keySet()) {
                if (ps.getVirtualStream(id).getHasChanges())
                    save(ps.getVirtualStream(id));
                service.getSession().execute(insertPstreamVstreamsStmt.bind(ps.getID(), id));
            }
        }
    }

    /*
    *
    *
    *
    *
    * */
    public void platformStreamBasicSave(PlatformStream ps)
            throws IncompatibleStreamException, BadPlatformStreamException {
        saveProperties(ps);
        ps.setHasChanges(false);
        saveConnectedVirtualStreams(ps);
    }

    /*
    *
    *
    *
    *
    * */
    private void virtualStreamBasicSave(VirtualStream vs) {
        saveProperties(vs);
    }

    /*
    *
    *
    *
    * */
    public void save(PlatformStream s)
            throws IncompatibleStreamException, BadPlatformStreamException {
        if (s.getClass().equals(ParallelPlatformStream.class)) {
            ParallelPlatformStream ps = (ParallelPlatformStream) s;
            int bandwidth = ps.getBandwidth();
            // additional actions here
            // .
            platformStreamBasicSave(s);
            s.setHasChanges(false);
            return;
        }
        if (s.getClass().equals(RecurrentPlatformStream.class)) {
            RecurrentPlatformStream ps = (RecurrentPlatformStream) s;
            int backlog = ps.getBacklogLength();
            // additional actions here
            // .
            platformStreamBasicSave(s);
            s.setHasChanges(false);
            return;
        }
        throw new IncompatibleStreamException("Unknown Pstream is requested to save");
    }

    /*
    *
    *
    *
    * */
    public void save(VirtualStream s)
            throws IncompatibleStreamException, BadPlatformStreamException {

        if (null == s.getAssignedStream())
            throw new BadPlatformStreamException("Pstream assigned to Vstream is null.");

        if (s.getAssignedStream().getHasChanges())
            throw new BadPlatformStreamException("Pstream assigned to Vstream has unsaved changes.");

        if (s.getClass().equals(ParallelVirtualStream.class)) {
            ParallelVirtualStream vs = (ParallelVirtualStream) s;
            String name = vs.getName();
            // additional actions here
            // .
            virtualStreamBasicSave(s);
            s.setHasChanges(false);
            return;
        }
        if (s.getClass().equals(RecurrentVirtualStream.class)) {
            RecurrentVirtualStream vs = (RecurrentVirtualStream) s;
            String name = vs.getName();
            // additional actions here
            // .
            virtualStreamBasicSave(s);
            s.setHasChanges(false);
            return;
        }
        throw new IncompatibleStreamException("Unknown Vstream is requested to save");
    }
}
