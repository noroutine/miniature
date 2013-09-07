package me.noroutine.miniature.http;

import java.util.List;

/**
 * @author Oleksii Khilkevych
 * @since 04.09.13
 */
public interface Exchange {
    Request request();
    Response response();
    void send(Response response);

    /**
     * Returns a friendly class, with more methods
     *
     * @param more
     * @param <T>
     * @return
     */
    <T> T take(Class<T> more);

    public static interface Exceptions {
        void push(Throwable t);
        Throwable pop();
        int size();
        List<Throwable> getExceptions();
    }
}
