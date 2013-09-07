package me.noroutine.miniature.http.handler.routing;

import me.noroutine.miniature.http.Exchange;
import me.noroutine.miniature.http.Handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleksii Khilkevych
 * @since 07.09.13
 */
public class MethodRouter implements Handler {

    private Map<String, Handler> methodHandlers = new HashMap<String, Handler>();

    public MethodRouter(Map<String, Handler> methodHandlers) {
        this.methodHandlers = methodHandlers;
    }

    @Override
    public void handle(Exchange exchange) {
        Handler handler = methodHandlers.get(exchange.request().method());
        if (handler != null) {
            handler.handle(exchange);
        }
    }
}
