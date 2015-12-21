package com.bwsw.streamscraper.system.models;

import com.bwsw.streamscraper.system.exceptions.JSONCompileException;
import com.eclipsesource.v8.*;
import org.apache.samza.storage.kv.KeyValueStore;
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
    V8 runtime;
    String uniq;
    V8Object script;
    Logger logger;
    boolean do_init;
    boolean do_process;
    boolean do_shutdown;
    boolean do_commit;
    KeyValueStore<Object, Object> store;

    public J2V8JSONHandler(String code, int commit_interval)
            throws
            JSONCompileException,
            NoSuchAlgorithmException {
        super(commit_interval);
        //store
        logger = LoggerFactory.getLogger(J2V8JSONHandler.class);
        uniq = "v_06c57bd0be5d5ebffe5bcf4c305445ec";
        runtime = V8.createV8Runtime();

        registerLogCallback(runtime, logger);
        registerKVStoreGetCallback(runtime, logger);
        registerKVStoreSetCallback(runtime, logger);
        registerKVStoreDelCallback(runtime, logger);

        String scr = "var " + uniq + " = " + code + ";";
        System.err.println(scr);
        runtime.executeVoidScript(scr);
        script = runtime.getObject(uniq);
        if (null == script)
            throw new JSONCompileException("Unable to compile `" + code + "'.");

        do_shutdown = true;
        do_init = true;
        do_process = true;
        do_commit = true;

    }

    private void registerKVStoreDelCallback(V8 runtime, Logger logger) {
        JavaVoidCallback store_del = (receiver, parameters) -> {
            try {
                if (parameters.length() > 0) {
                    Object arg1 = parameters.get(0);
                    store.delete(arg1);
                    if (arg1 instanceof Releasable) {
                        ((Releasable) arg1).release();
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println(e.getClass().toString());
            }
        };
        runtime.registerJavaMethod(store_del, "kv_store_del");
    }

    private void registerKVStoreSetCallback(V8 runtime, Logger logger) {
        JavaVoidCallback store_set = (receiver, parameters) -> {
            try {
                if (parameters.length() > 1) {
                    Object arg1 = parameters.get(0);
                    Object arg2 = parameters.get(1);
                    store.put(arg1, arg2);
                    if (arg1 instanceof Releasable) {
                        ((Releasable) arg1).release();
                    }
                    if (arg2 instanceof Releasable) {
                        ((Releasable) arg2).release();
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println(e.getClass().toString());
            }
        };
        runtime.registerJavaMethod(store_set, "kv_store_set");
    }

    private void registerKVStoreGetCallback(V8 runtime, Logger logger) {
        JavaCallback store_get = (receiver, parameters) -> {
            Object value = null;
            try {
                if (parameters.length() > 0) {
                    Object arg1 = parameters.get(0);
                    value = store.get(arg1);
                    if (arg1 instanceof Releasable) {
                        ((Releasable) arg1).release();
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println(e.getClass().toString());
            }
            return value;
        };
        runtime.registerJavaMethod(store_get, "kv_store_get");
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

    public void setStore(KeyValueStore<Object, Object> kv) {
        store = kv;
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


