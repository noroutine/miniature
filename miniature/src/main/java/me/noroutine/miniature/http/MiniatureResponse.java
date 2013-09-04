package me.noroutine.miniature.http;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public class MiniatureResponse implements Response {

    private HttpExchange exchange;

    private int statusCode;

    private long contentLength;

    private InputStream body;

    public MiniatureResponse(HttpExchange exchange) throws IOException {
        this.exchange = exchange;
    }

    
    public Response status(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    
    public Response length(long length) {
        this.contentLength = length;
        return this;
    }

    
    public Response header(String header, String value) {
        this.exchange.getResponseHeaders().add(header, value);
        return this;
    }

    
    public Response header(String header, int value) {
        this.exchange.getResponseHeaders().add(header, Integer.toString(value));
        return this;
    }

    
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

    @Override
    @SuppressWarnings("unchecked")
    public <T> T take(Class<T> more) {
        if (more == Body.class) {
            return (T) new Body() {
                @Override
                public Body body(String responseBody) {
                    body = new ByteArrayInputStream(responseBody.getBytes());
                    return this;
                }

                @Override
                public Body body(InputStream responseBody) {
                    body = responseBody;
                    return this;
                }
            };
        }

        if (more == Status.class) {
            return (T) new Status() {
                @Override
                public Status status(int statusCode) {
                    MiniatureResponse.this.statusCode  = statusCode;
                    return this;
                }
            };
        }

        if (more == Sender.class) {
            return (T) new Sender() {
                @Override
                public void send() {
                    try {
                        exchange.sendResponseHeaders(statusCode, contentLength);
                        IOUtils.copy(body, exchange.getResponseBody());
                        exchange.getResponseBody().close();

                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            };
        }

        if (more == Headers.class) {
            return (T) new Headers() {

                @Override
                public Headers length(long length) {
                    contentLength = length;
                    return this;
                }


                @Override
                public Headers header(String header, Object value) {
                    exchange.getResponseHeaders().add(header, value.toString());
                    return this;
                }

                @Override
                public Headers header(String header, Iterable<Object> values) {
                    throw new UnsupportedOperationException("Not implemented"); // TODO (oleksiy on 03.09.13): implement this
                }
            };
        }

        throw new UnsupportedOperationException("Not implemented"); // TODO (oleksiy on 03.09.13): implement this
    }
}
