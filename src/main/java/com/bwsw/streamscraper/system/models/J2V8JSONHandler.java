package com.bwsw.streamscraper.system.models;

import com.bwsw.streamscraper.system.exceptions.JSONCompileException;
import com.bwsw.streamscraper.system.models.adapters.ICallbackFactory;
import com.eclipsesource.v8.*;
import org.apache.samza.storage.kv.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by ivan on 18.12.15.
 */
public class J2V8JSONHandler extends BasicHandler {

    static String P_PROCESS = "process";
    static String P_SHUTDOWN = "shutdown";
    static String P_INIT = "init";
    static String P_COMMIT = "commit";
    private static ArrayList<ICallbackFactory> callback_factory_list;
    V8 runtime;
    String uniq;
    V8Object script;
    Logger logger;
    boolean do_init;
    boolean do_process;
    boolean do_shutdown;
    boolean do_commit;
    KeyValueStore<Object, Object> store;

    public J2V8JSONHandler(String code, int commit_interval, KeyValueStore<Object, Object> stor)
            throws
            JSONCompileException,
            NoSuchAlgorithmException {
        super(commit_interval);
        store = stor;
        uniq = "handler";
        runtime = V8.createV8Runtime();
        logger = LoggerFactory.getLogger(J2V8JSONHandler.class);

        registerLogCallback(runtime, logger);

        for (ICallbackFactory f : callback_factory_list)
            f.generate(runtime, store, logger);

        runtime.executeVoidScript(code);
        //System.err.println(code);

        script = runtime.getObject(uniq);
        //System.err.println(script);

        if (null == script)
            throw new JSONCompileException("Unable to compile `" + code + "'.");

        do_shutdown = true;
        do_init = true;
        do_process = true;
        do_commit = true;

    }

    public static void addCallbackFactory(ICallbackFactory f) {
        if (null == callback_factory_list)
            callback_factory_list = new ArrayList<>();
        callback_factory_list.add(f);
    }

    private void registerLogCallback(V8 runtime, Logger logger) {
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
    }

    @Override
    public void shutdown() throws Exception {
        if (do_shutdown)
            try {
                script.executeVoidFunction(J2V8JSONHandler.P_SHUTDOWN, null);
            } catch (Exception e) {
                do_shutdown = false;
                System.err.println(e.getClass().toString());
                System.err.println(e.getMessage());
            }
        super.shutdown();
    }

    @Override
    public void init() throws Exception {
        if (do_init)
            try {
                script.executeVoidFunction(J2V8JSONHandler.P_INIT, null);
            } catch (Exception e) {
                do_init = false;
                System.err.println(e.getClass().toString());
                System.err.println(e.getMessage());
            }
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


