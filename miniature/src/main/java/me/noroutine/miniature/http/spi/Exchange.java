package me.noroutine.miniature.http.spi;

import me.noroutine.miniature.http.Request;
import me.noroutine.miniature.http.Response;

/**
 * @author Oleksii Khilkevych
 * @since 04.09.13
 */
public interface Exchange {
    Request request();
    Response response();

    static interface ResponseSender {
        void send(Response response);
    }
}
