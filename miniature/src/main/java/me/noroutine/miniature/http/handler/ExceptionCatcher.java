package me.noroutine.miniature.http.handler;

import me.noroutine.miniature.http.Exchange;
import me.noroutine.miniature.http.Handler;

/**
 * @author Oleksii Khilkevych
 * @since 07.09.13
 */
public class ExceptionCatcher implements Handler {

    private Handler delegate;

    public ExceptionCatcher(Handler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handle(Exchange exchange) {
        try {
            delegate.handle(exchange);
        } catch (Throwable t) {
            exchange.take(Exchange.Exceptions.class).push(t);
        }
    }

}
