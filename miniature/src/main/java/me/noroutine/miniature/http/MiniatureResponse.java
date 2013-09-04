package me.noroutine.miniature.http;

import me.noroutine.miniature.http.spi.Exchange;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public class MiniatureResponse implements Response {

    private int statusCode;

    private long contentLength;

    private Headers headers = new Headers();

    private InputStream body;

    private Exchange.ResponseSender responseSender;

    public MiniatureResponse() {
    }

    @Override
    public Response length(long length) {
        this.contentLength = length;
        return this;
    }

    @Override
    public Response header(String header, Object value) {
        this.headers.add(header, value.toString());
        return this;
    }

    @Override
    public Response status(int statusCode) {
        this.statusCode  = statusCode;
        return this;
    }

    @Override
    public Response body(String responseBody) {
        this.body = new ByteArrayInputStream(responseBody.getBytes());
        return this;
    }

    @Override
    public void send() {
        take(Response.SenderAccess.class).getResponseSender().send(this);
    }

    @Override
    public Response.State state() {
        return take(Response.State.class);
    }

    private class State implements Response.State {
        private MiniatureResponse parent = MiniatureResponse.this;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            parent.statusCode = statusCode;
        }

        public long getContentLength() {
            return contentLength;
        }

        public void setContentLength(long contentLength) {
            parent.contentLength = contentLength;
        }

        public Headers getHeaders() {
            return headers;
        }

        public void setHeaders(Headers headers) {
            parent.headers = headers;
        }

        public InputStream getBody() {
            return body;
        }

        public void setBody(InputStream body) {
            parent.body = body;
        }

    }

    private class SenderAccess implements Response.SenderAccess {
        private MiniatureResponse parent = MiniatureResponse.this;

        public Exchange.ResponseSender getResponseSender() {
            return parent.responseSender;
        }

        public void setResponseSender(Exchange.ResponseSender responseSender) {
            parent.responseSender = responseSender;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T take(Class<T> more) {

        if (more == Response.State.class) {
            return (T) new State();
        }

        if (more == Response.SenderAccess.class) {
            return (T) new SenderAccess();
        }

        return null;
    }
}
