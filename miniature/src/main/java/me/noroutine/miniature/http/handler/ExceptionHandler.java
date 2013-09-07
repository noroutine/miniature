package me.noroutine.miniature.http.handler;

import me.noroutine.miniature.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oleksii Khilkevych
 * @since 07.09.13
 */
public class ExceptionHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public void handle(Exchange exchange) {
        if (! exchange.response().take(Response.State.class).isSent()) {
            Exchange.Exceptions exceptions = exchange.take(Exchange.Exceptions.class);
            if (exceptions.size() > 0) {
                exchange.response().status(500).body("500").send();
                Throwable last = exceptions.pop();

                log.error(last.getMessage(), last);
            }
        }
    }
}
