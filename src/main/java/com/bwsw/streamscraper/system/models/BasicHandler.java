package com.bwsw.streamscraper.system.models;

/**
 * Created by ivan on 18.12.15.
 */
public class BasicHandler {

    String code_commit;
    String code_end;
    String code_process;
    String code_init;
    int commit_interval;

    BasicHandler(String code_init,
                 String code_process,
                 String code_commit,
                 int commit_interval,
                 String code_end) {

        this.commit_interval = commit_interval;
        this.code_init = code_init;
        this.code_process = code_process;
        this.code_commit = code_commit;
        this.code_end = code_end;

    }

    public String getCodeInit() {
        return this.code_init;
    }

    public String getCodeProcess() {
        return this.code_process;
    }

    public String getCodeCommit() {
        return this.code_commit;
    }

    public String getCodeEnd() {
        return this.code_end;
    }

    public int getCommitInterval() {
        return this.commit_interval;
    }

    public void setCommitInterval() {
        this.commit_interval = commit_interval;
    }
}
