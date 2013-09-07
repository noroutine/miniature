package me.noroutine.miniature.http.handler;

import me.noroutine.miniature.http.Exchange;
import me.noroutine.miniature.http.Handler;

/**
 * @author Oleksii Khilkevych
 * @since 07.09.13
 */
public class NotFound implements Handler {

    @Override
    public void handle(Exchange exchange) {
        exchange.response().status(404).body("404").send();
    }
}
