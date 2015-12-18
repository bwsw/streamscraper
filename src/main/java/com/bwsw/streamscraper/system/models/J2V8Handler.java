package com.bwsw.streamscraper.system.models;

import com.bwsw.streamscraper.system.exceptions.JSONCompileException;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;

/**
 * Created by ivan on 18.12.15.
 */
public class J2V8Handler extends BasicHandler {

    static String P_SHUTDOWN = "shutdown";
    static String P_INIT = "init";
    static String P_COMMIT = "init";
    String code;
    V8 runtime;
    V8Object script;

    public J2V8Handler(String code, int commit_interval) {
        super(commit_interval);
        runtime = V8.createV8Runtime();
        script = runtime.executeObjectScript(code);
    }

    @Override
    public void shutdown() throws Exception {
        script.executeVoidFunction(J2V8Handler.P_SHUTDOWN, null);
        runtime.release();
        super.shutdown();
    }

    @Override
    public void init() throws Exception {
        script.executeVoidFunction(J2V8Handler.P_INIT, null);
        runtime.release();
        super.shutdown();
    }

    @Override
    public void commit() throws Exception {
        script.executeVoidFunction(J2V8Handler.P_COMMIT, null);
        runtime.release();
        super.shutdown();
    }

    protected V8Array prepareData(Object object) throws Exception {
        V8Array arr = new V8Array(runtime);
        String s = (String) object;
        V8Object o = runtime.executeObjectScript(s);
        if (null == o)
            throw new JSONCompileException("Failed to compile `" + s + "' to JSON.");
        arr.push(o);
        return arr;
    }

    @Override
    public void process(Object obj) throws Exception {
        V8Array array = prepareData(obj);
        script.executeVoidFunction(J2V8Handler.P_COMMIT, array);
        runtime.release();
        super.shutdown();
    }

}


