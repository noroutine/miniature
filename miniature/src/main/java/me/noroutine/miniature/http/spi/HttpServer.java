package me.noroutine.miniature.http.spi;

import me.noroutine.miniature.http.Handler;

/**
 * @author Oleksii Khilkevych
 * @since 04.09.13
 */
public interface HttpServer {
    void port(int port);
    void handler(Handler handler);
    void start();
}
