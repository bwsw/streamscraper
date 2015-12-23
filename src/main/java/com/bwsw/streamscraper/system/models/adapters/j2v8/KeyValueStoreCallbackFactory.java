package com.bwsw.streamscraper.system.models.adapters.j2v8;

import com.bwsw.streamscraper.system.models.BasicHandler;
import com.bwsw.streamscraper.system.models.HashKeyValueStoreStub;
import com.bwsw.streamscraper.system.models.J2V8Handler;
import com.bwsw.streamscraper.system.models.adapters.ICallbackFactory;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8;
import org.apache.samza.storage.kv.KeyValueStore;
import org.slf4j.Logger;

/**
 * Created by ivan on 21.12.15.
 */
public class KeyValueStoreCallbackFactory implements ICallbackFactory {
    HashKeyValueStoreStub<Object, Object> store;

    public KeyValueStoreCallbackFactory(HashKeyValueStoreStub<Object, Object> store) {
        this.store = store;
    }

    private static void registerKVStoreDelCallback(V8 runtime, KeyValueStore<Object, Object> store, Logger logger) {
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

    private static void registerKVStoreSetCallback(V8 runtime, KeyValueStore<Object, Object> store, Logger logger) {
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

    private static void registerKVStoreGetCallback(V8 runtime, KeyValueStore<Object, Object> store, Logger logger) {
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

    public void generate(BasicHandler h) {
        J2V8Handler handler = (J2V8Handler) h;

        registerKVStoreDelCallback(handler.getRuntime(), store, handler.getLogger());
        registerKVStoreSetCallback(handler.getRuntime(), store, handler.getLogger());
        registerKVStoreGetCallback(handler.getRuntime(), store, handler.getLogger());
    }


}
