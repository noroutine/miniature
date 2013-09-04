package me.noroutine.miniature.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

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
    public InputStream body() {
        return null;
    }

    @Override
    public String method() {
        return this.exchange.getRequestMethod();
    }

    @Override
    public String url() {
        return this.exchange.getRequestURI().toString();
    }

    @Override
    public HttpPrincipal principal() {
        return null;
    }

    @Override
    public Object attribute(String s) {
        return null;
    }

    @Override
    public void attribute(String s, Object o) {
    }
}
