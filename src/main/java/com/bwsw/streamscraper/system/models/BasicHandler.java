package com.bwsw.streamscraper.system.models;

/**
 * Created by ivan on 18.12.15.
 */
public class BasicHandler {

    int commit_interval;

    BasicHandler() {
        commit_interval = 60;
    }

    BasicHandler(int commit_interval) {
        this.commit_interval = commit_interval;
    }

    public void init() throws Exception {

    }

    public void process(Object data) throws Exception {

    }

    public void commit() throws Exception {

    }

    public void shutdown() throws Exception {

    }

    public void setCommitInterval(int commit_interval) {
        this.commit_interval = commit_interval;
    }

}
