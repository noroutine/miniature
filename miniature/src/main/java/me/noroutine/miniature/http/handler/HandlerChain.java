package me.noroutine.miniature.http.handler;

import me.noroutine.miniature.http.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Oleksii Khilkevych
 * @since 07.09.13
 */
public class HandlerChain implements Handler {

    private List<Handler> handlers = new LinkedList<Handler>();

    public HandlerChain(List<Handler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void handle(Exchange exchange) {
        for (Handler handler: handlers) {
            handler.handle(exchange);

            Response.State responseState = exchange.response().take(Response.State.class);
            if (responseState.isSent()) {
                break;
            }
        }
    }
}
