package me.noroutine.miniature.http;

import java.io.InputStream;

/**
 * @author Oleksii Khilkevych
 * @since 03.09.13
 */
public interface Response {

    // Fluent mutators
    Response status(int statusCode);

    Response header(String header, Object value);

    Response length(long length);

    Response body(String body);

    void send();

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
        public int getStatusCode();
        public void setStatusCode(int statusCode);

        public long getContentLength();
        public void setContentLength(long contentLength);

        public Headers getHeaders();
        public void setHeaders(Headers headers);

        public InputStream getBody();
        public void setBody(InputStream body);

        public boolean isSent();
        public void setSent(boolean sent);

        Exchange getExchange();
        void setExchange(Exchange exchange);

    }
}
