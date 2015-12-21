package com.bwsw.streamscraper.system.models;

import org.apache.samza.storage.kv.Entry;
import org.apache.samza.storage.kv.KeyValueIterator;
import org.apache.samza.storage.kv.KeyValueStore;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ivan on 21.12.15.
 */
public class HashKeyValueStoreStub<K, V> implements KeyValueStore<K, V> {

    HashMap<K, V> h;

    public HashKeyValueStoreStub() {
        h = new HashMap<>();
    }

    @Override
    public V get(K o) {
        return h.get(o);
    }

    @Override
    public void put(K o, V o2) {
        h.put(o, o2);
    }

    @Override
    public void putAll(List<Entry<K, V>> list) {
        for (Entry<K, V> e : list) {
            h.put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void delete(Object o) {
        h.remove(o);
    }

    @Override
    public KeyValueIterator range(Object from, Object to) {
        return null;
    }

    @Override
    public KeyValueIterator all() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void flush() {

    }
}
