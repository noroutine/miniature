package me.noroutine.miniature;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public class MiniatureResponse implements Response {

    private HttpExchange exchange;

    private int statusCode;

    private long contentLength;

    public MiniatureResponse(HttpExchange exchange) throws IOException {
        this.exchange = exchange;
    }

    @Override
    public Response status(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    @Override
    public Response length(long length) {
        this.contentLength = length;
        return this;
    }

    @Override
    public Response headers(Map<? extends String, ? extends List<String>> headers) {
        this.exchange.getResponseHeaders().putAll(headers);
        return this;
    }

    @Override
    public Response header(String header, String value) {
        this.exchange.getResponseHeaders().add(header, value);
        return this;
    }

    @Override
    public Response header(String header, int value) {
        this.exchange.getResponseHeaders().add(header, Integer.toString(value));
        return this;
    }

    @Override
    public Response send(CharSequence body) {
        try {
            this.exchange.sendResponseHeaders(statusCode, contentLength);
            this.exchange.getResponseBody().write(body.toString().getBytes());

            this.exchange.getResponseBody().close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return this;
    }
}
