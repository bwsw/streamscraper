package com.bwsw.streamscraper.system.models;

import com.bwsw.streamscraper.system.exceptions.JSONCompileException;
import com.eclipsesource.v8.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

/**
 * Created by ivan on 18.12.15.
 */
public class J2V8JSONHandler extends BasicHandler {

    static String P_PROCESS = "process";
    static String P_SHUTDOWN = "shutdown";
    static String P_INIT = "init";
    static String P_COMMIT = "commit";
    String code;
    V8 runtime;
    String uniq;
    V8Object script;
    Logger logger;
    boolean do_init;
    boolean do_process;
    boolean do_shutdown;
    boolean do_commit;

    public J2V8JSONHandler(String code, int commit_interval) throws JSONCompileException, NoSuchAlgorithmException {
        super(commit_interval);
        logger = LoggerFactory.getLogger(J2V8JSONHandler.class);
        uniq = "v_06c57bd0be5d5ebffe5bcf4c305445ec";
        runtime = V8.createV8Runtime();

        JavaVoidCallback callback = (receiver, parameters) -> {
            if (parameters.length() > 0) {
                Object arg1 = parameters.get(0);
                logger.error(arg1.toString());
                System.err.println(arg1.toString());
                if (arg1 instanceof Releasable) {
                    ((Releasable) arg1).release();
                }
            }
        };

        runtime.registerJavaMethod(callback, "log");

        String scr = "var " + uniq + " = " + code + ";";
        System.err.println(scr);
        runtime.executeVoidScript(scr);
        script = runtime.getObject(uniq);
        if (null == script)
            throw new JSONCompileException("Unable to compile `" + code + "'.");

        V8Object f;

        do_shutdown = true;
        do_init = true;
        do_process = true;
        do_commit = true;

        if (!V8.getUndefined().equals(runtime.getObject(J2V8JSONHandler.P_SHUTDOWN)))
            do_shutdown = false;

        if (!V8.getUndefined().equals(runtime.getObject(J2V8JSONHandler.P_INIT)))
            do_init = false;

        if (!V8.getUndefined().equals(runtime.getObject(J2V8JSONHandler.P_PROCESS)))
            do_process = false;

        if (!V8.getUndefined().equals(runtime.getObject(J2V8JSONHandler.P_COMMIT)))
            do_commit = false;
    }

    @Override
    public void shutdown() throws Exception {
        if (do_shutdown)
            script.executeVoidFunction(J2V8JSONHandler.P_SHUTDOWN, null);
        super.shutdown();
    }

    @Override
    public void init() throws Exception {
        if (do_init)
            script.executeVoidFunction(J2V8JSONHandler.P_INIT, null);
    }

    @Override
    public void commit() throws Exception {
        if (do_commit)
            script.executeVoidFunction(J2V8JSONHandler.P_COMMIT, null);
    }

    protected V8Array prepareData(Object object) throws Exception {
        V8Array arr = new V8Array(runtime);
        String s = (String) object;
        V8Object o = runtime.executeObjectScript(s);
        // TODO: fix null etc.
        if (null == o)
            throw new JSONCompileException("Failed to compile `" + s + "' to JSON.");
        arr.push(o);
        return arr;
    }

    @Override
    public void process(Object obj) throws Exception {
        V8Object f = runtime.getObject(J2V8JSONHandler.P_COMMIT);
        if (!V8.getUndefined().equals(f)) {
            V8Array array = prepareData(obj);
            script.executeVoidFunction(J2V8JSONHandler.P_PROCESS, array);
        }
    }
}


