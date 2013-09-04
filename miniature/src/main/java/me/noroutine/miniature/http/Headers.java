package me.noroutine.miniature.http;

import java.util.*;

/**
 * @author Oleksii Khilkevych
 * @since 04.09.13
 */
public class Headers {

    private Map<String, String> headers = new HashMap<String, String>();

    public int size() {
        return headers.size();
    }

    public boolean isEmpty() {
        return headers.isEmpty();
    }


    public Object add(String key, Object value) {
        return headers.put(key, value.toString());
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void clear() {
        headers.clear();
    }
}
