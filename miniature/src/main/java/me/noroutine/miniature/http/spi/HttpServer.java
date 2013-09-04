package me.noroutine.miniature.http.spi;

import me.noroutine.miniature.http.Handler;
import me.noroutine.miniature.http.Middleware;

import java.util.List;
import java.util.Map;

/**
 * @author Oleksii Khilkevych
 * @since 04.09.13
 */
public interface HttpServer {
    void setPort(int port);
    void setHandlers(Map<String, Handler> hanlers);
    void setMiddlewares(List<Middleware> middlewares);
    void start();
}
