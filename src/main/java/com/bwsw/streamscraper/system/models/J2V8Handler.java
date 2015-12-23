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
public class J2V8Handler extends BasicHandler {

    static String P_PROCESS = "process";
    static String P_SHUTDOWN = "shutdown";
    static String P_INIT = "init";
    static String P_COMMIT = "commit";
    static String uniq = "v_cd6a84247215681203b961c73d47dca8";

    private static ArrayList<ICallbackFactory> callback_factory_list;

    CachedJSVar ci,
            tbd,
            tbe,
            mf,
            as;

    boolean do_init,
            do_process,
            do_shutdown,
            do_commit;

    V8 runtime;
    V8Object script;
    Logger logger;

    public J2V8Handler(String code, int commit_interval)
            throws
            JSONCompileException,
            NoSuchAlgorithmException {
        super(commit_interval);

        ci = new CachedJSVar();
        tbd = new CachedJSVar();
        tbe = new CachedJSVar();
        mf = new CachedJSVar();
        as = new CachedJSVar();

        String uniq_h = "handler";

        runtime = V8.createV8Runtime();
        logger = LoggerFactory.getLogger(J2V8Handler.class);

        registerLogCallback(runtime, logger);

        for (ICallbackFactory f : callback_factory_list)
            f.generate(this);

        runtime.executeVoidScript("function get_event() { return " + uniq + ";}");
        runtime.executeVoidScript(code);

        script = runtime.getObject(uniq_h);

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
                script.executeVoidFunction(J2V8Handler.P_SHUTDOWN, null);
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
                script.executeVoidFunction(J2V8Handler.P_INIT, null);
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
                script.executeVoidFunction(J2V8Handler.P_COMMIT, null);
            } catch (Exception e) {
                do_commit = false;
                System.err.println(e.getClass().toString());
                System.err.println(e.getMessage());
            }
    }

    public int getCommitInterval() {
        try {
            int ci = this.ci.getOrRequire(script.getInteger("commit_interval"));
            return ci;
        } catch (V8ResultUndefined e) {
            return this.commit_interval;
        }
    }

    public boolean getTerminateOnBadData() {
        try {
            boolean tbd = this.tbd.getOrRequire(script.getBoolean("terminate_on_bad_data"));
            return tbd;
        } catch (V8ResultUndefined e) {
            return true;
        }
    }

    public boolean getTerminateOnBadEval() {
        try {
            boolean tbe = this.tbe.getOrRequire(script.getBoolean("terminate_on_bad_eval"));
            return tbe;
        } catch (V8ResultUndefined e) {
            return true;
        }
    }

    public MSG_FORMAT getMessageFormat() {
        try {
            String mf = this.mf.getOrRequire(script.getString("message_format"));
            if (null == mf)
                return MSG_FORMAT.JSON;
            if (mf.equals("json"))
                return MSG_FORMAT.JSON;
            if (mf.equals("avro"))
                return MSG_FORMAT.AVRO;
            return MSG_FORMAT.UNKNOWN;
        } catch (V8ResultUndefined e) {
            return MSG_FORMAT.JSON;
        }
    }

    public String getAvroSchema() {
        try {
            String schema = this.as.getOrRequire(script.getString("avro_schema"));
            return schema;
        } catch (V8ResultUndefined e) {
            return null;
        }
    }

    protected boolean prepareData(Object object) throws Exception {
        String s = (String) object;
        String expr = "var " + uniq + " = " + s + ";";
        try {
            V8Object o = runtime.executeObjectScript(expr);
            return true;
        } catch (V8ScriptCompilationException e) {
            logger.info(e.getClass().toString());
            logger.info(e.getMessage());
            if (getTerminateOnBadData()) {
                throw e;
            }
            return false;
        }
    }

    @Override
    public void process(Object obj) throws Exception {
        if (do_process) {
            boolean prepared = prepareData(obj);
            try {
                if (prepared)
                    script.executeVoidFunction(J2V8Handler.P_PROCESS, null);
            } catch (Exception e) {
                do_process = false;
                System.err.println(e.getClass().toString());
                System.err.println(e.getMessage());
            }
        }
    }

    public enum MSG_FORMAT {JSON, AVRO, UNKNOWN}

    class CachedJSVar {
        public boolean is_cached = false;
        public int int_variant = 0;
        public boolean bool_variant = false;
        public String string_variant = "";

        public int getOrRequire(int v) {
            if (is_cached)
                return int_variant;
            is_cached = true;
            int_variant = v;
            return int_variant;
        }

        public boolean getOrRequire(boolean v) {
            if (is_cached)
                return bool_variant;
            is_cached = true;
            bool_variant = v;
            return bool_variant;
        }

        public String getOrRequire(String v) {
            if (is_cached)
                return string_variant;
            is_cached = true;
            string_variant = v;
            return string_variant;
        }

    }
}


