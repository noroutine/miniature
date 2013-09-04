package me.noroutine.miniature.http;

import java.io.InputStream;
import java.net.URI;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public interface Request {
    String body();
    String method();
    String url();

    State state();
    /**
     * Returns a friendly class, with more methods
     *
     * @param more
     * @param <T>
     * @return
     */
    <T> T take(Class<T> more);

    static interface State {
        InputStream getInputStream();
        void setInputStream(InputStream inputStream);
        String getMethod();
        void setMethod(String method);
        URI getUri();
        void setUri(URI uri);
    }
}
