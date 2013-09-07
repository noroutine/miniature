package me.noroutine.miniature.http;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public interface Handler {
    void handle(Exchange exchange);
}
