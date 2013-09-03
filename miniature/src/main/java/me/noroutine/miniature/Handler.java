package me.noroutine.miniature;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public interface Handler {
    void handle(Request request, Response response);
}
