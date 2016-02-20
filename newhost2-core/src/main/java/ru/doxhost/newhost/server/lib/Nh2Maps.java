package ru.doxhost.newhost.server.lib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Nh2Maps {

    private Map<String, String> map = new HashMap<String, String>();

    private Nh2Maps(){}

    private Nh2Maps(String key, String val) {
        map.put(key, val);
    }

    public static Nh2Maps createHashMap(final String key, final String val) {
        return new Nh2Maps(key, val);
    }

    public static Nh2Maps createHashMap() {
        return new Nh2Maps();
    }

    public Nh2Maps add(final String key, final String val) {
        map.put(key, val);
        return this;
    }

    public Map<String, String> readOnly() {
        return Collections.unmodifiableMap(map);
    }
}
