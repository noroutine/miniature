package me.noroutine.miniature.http;

/**
 * @author Oleksii Khilkevych
 * @since 04.09.13
 */
public interface Exchange {
    Request request();
    Response response();
    void send(Response response);
}
