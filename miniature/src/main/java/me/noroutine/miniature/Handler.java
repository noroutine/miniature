package me.noroutine.miniature;

import me.noroutine.miniature.http.Request;
import me.noroutine.miniature.http.Response;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public interface Handler {
    void handle(Request request, Response response);
}
