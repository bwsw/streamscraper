package com.bwsw.streamscraper.system.models;

/**
 * Created by ivan on 23.12.15.
 */
class J2V8CachedVar {
    boolean is_cached;
    int int_variant;
    boolean bool_variant;
    String string_variant;

    public J2V8CachedVar() {
        is_cached = false;
        int_variant = 0;
        bool_variant = false;
        string_variant = "";
    }

    public boolean getCachedState() {
        return is_cached;
    }

    public void setCachedState(boolean state) {
        is_cached = state;
    }

    public int getOrRequire(int v) {
        // TODO: Fix
        if (is_cached)
            return int_variant;
        is_cached = true;
        int_variant = v;
        return int_variant;
    }

    public boolean getOrRequire(boolean v) {
        // TODO: Fix
        if (is_cached)
            return bool_variant;
        is_cached = true;
        bool_variant = v;
        return bool_variant;
    }

    public String getOrRequire(String v) {
        // TODO: Fix
        if (is_cached)
            return string_variant;
        is_cached = true;
        string_variant = v;
        return string_variant;
    }

}