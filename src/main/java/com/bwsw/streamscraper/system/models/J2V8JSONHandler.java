package com.bwsw.streamscraper.system.models;

import com.bwsw.streamscraper.system.exceptions.JSONCompileException;
import com.bwsw.streamscraper.system.models.adapters.ICallbackFactory;
import com.eclipsesource.v8.*;
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

    public J2V8JSONHandler(String code, int commit_interval)
            throws
            JSONCompileException,
            NoSuchAlgorithmException {
        super(commit_interval);
        uniq = "handler";
        runtime = V8.createV8Runtime();
        logger = LoggerFactory.getLogger(J2V8JSONHandler.class);

        registerLogCallback(runtime, logger);

        for (ICallbackFactory f : callback_factory_list)
            f.generate(this);

        runtime.executeVoidScript(code);

        script = runtime.getObject(uniq);

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

    public V8 getRuntime() {
        return runtime;
    }

    public Logger getLogger() {
        return logger;
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
            try {
                script.executeVoidFunction(J2V8JSONHandler.P_COMMIT, null);
            } catch (Exception e) {
                do_commit = false;
                System.err.println(e.getClass().toString());
                System.err.println(e.getMessage());
            }
    }

    public int getCommitInterval() {
        try {
            int ci = script.getInteger("commit_interval");
            return ci;
        } catch (V8ResultUndefined e) {
            return this.commit_interval;
        }
    }

    public boolean getTerminateOnBadData() {
        try {
            boolean tbd = script.getBoolean("terminate_on_bad_data");
            return tbd;
        } catch (V8ResultUndefined e) {
            return true;
        }
    }

    public boolean getTerminateOnBadEval() {
        try {
            boolean tbe = script.getBoolean("terminate_on_bad_eval");
            return tbe;
        } catch (V8ResultUndefined e) {
            return true;
        }
    }

    protected V8Array prepareData(Object object) throws Exception {
        V8Array arr = new V8Array(runtime);
        String s = (String) object;
        String uniq = "v_cd6a84247215681203b961c73d47dca8";
        String expr = "var " + uniq + " = " + s + ";";
        System.err.println(expr);
        try {
            V8Object o = runtime.executeObjectScript(expr);
            arr.push(o);
            return arr;
        } catch (V8ScriptCompilationException e) {
            logger.info(e.getClass().toString());
            logger.info(e.getMessage());
            if (getTerminateOnBadData()) {
                throw e;
            }
            return null;
        }
    }

    @Override
    public void process(Object obj) throws Exception {
        if (do_process) {
            V8Array array = prepareData(obj);
            try {
                if (null != array) {
                    script.executeVoidFunction(J2V8JSONHandler.P_PROCESS, array);
                    array.release();
                }
            } catch (Exception e) {
                do_process = false;
                System.err.println(e.getClass().toString());
                System.err.println(e.getMessage());
            }
        }
    }


}


