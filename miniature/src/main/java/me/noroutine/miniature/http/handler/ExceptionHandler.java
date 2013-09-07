package me.noroutine.miniature.http.handler;

import me.noroutine.miniature.http.*;

/**
 * @author Oleksii Khilkevych
 * @since 07.09.13
 */
public class ExceptionHandler implements Handler {

    @Override
    public void handle(Exchange exchange) {
        if (! exchange.response().take(Response.State.class).isSent()) {
            Exchange.Exceptions exceptions = exchange.take(Exchange.Exceptions.class);
            if (exceptions.size() > 0) {
                exchange.response().status(500).body("500").send();
            }
        }
    }
}
