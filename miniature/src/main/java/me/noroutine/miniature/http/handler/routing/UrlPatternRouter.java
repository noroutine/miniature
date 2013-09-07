package me.noroutine.miniature.http.handler.routing;

import me.noroutine.miniature.http.Exchange;
import me.noroutine.miniature.http.Handler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Oleksii Khilkevych
 * @since 07.09.13
 */
public class UrlPatternRouter implements Handler {

    private Map<String, Handler> handlers = new LinkedHashMap<String, Handler>();

    public UrlPatternRouter(Map<String, Handler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void handle(Exchange exchange) {
        for (Map.Entry<String, Handler> handlerEntry: handlers.entrySet()) {
            if (exchange.request().url().matches(handlerEntry.getKey())) {
                handlerEntry.getValue().handle(exchange);
                break;
            }
        }
    }

}
