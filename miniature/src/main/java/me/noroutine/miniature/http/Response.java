package me.noroutine.miniature.http;

import java.io.InputStream;

/**
 * @author Oleksii Khilkevych
 * @since 03.09.13
 */
public interface Response {

    /**
     * Returns a friendly class, with more methods
     * @param more
     * @param <T>
     * @return
     */
    <T> T take(Class<T> more);

    interface Status {
        // Fluent mutators
        Status status(int statusCode);
    }

    interface Headers {
        Headers header(String header, Object value);
        Headers header(String header, Iterable<Object> values);
        Headers length(long length);
    }

    interface Body {
        Body body(String body);
        Body body(InputStream body);
    }

    interface Sender {
        // send
        void send();
    }
}
