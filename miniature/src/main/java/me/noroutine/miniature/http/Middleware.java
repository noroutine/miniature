package me.noroutine.miniature.http;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public interface Middleware {
    void handle(Request request, Response response);
}
