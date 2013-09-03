package me.noroutine.miniature;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public class MiniatureRequest implements Request {
    private HttpExchange exchange;

    public MiniatureRequest(HttpExchange exchange) throws IOException {
        this.exchange = exchange;
    }

    @Override
    public InputStream stream() {
        return exchange.getRequestBody();
    }

    @Override
    public String method() {
        return this.exchange.getRequestMethod();
    }

    @Override
    public String url() {
        return this.exchange.getRequestURI().toString();
    }
}
