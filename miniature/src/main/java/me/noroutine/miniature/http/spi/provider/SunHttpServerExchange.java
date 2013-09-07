package me.noroutine.miniature.http.spi.provider;

import com.sun.net.httpserver.HttpExchange;
import me.noroutine.miniature.http.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author Oleksii Khilkevych
 * @since 04.09.13
 */
public class SunHttpServerExchange implements Exchange {

    private static final Logger log = LoggerFactory.getLogger(SunHttpServerExchange.class);

    private HttpExchange httpExchange;

    private boolean sent = false;

    private Stack<Throwable> exceptions = new Stack<Throwable>();

    public SunHttpServerExchange(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    @Override
    public Request request() {
        return requestFromProviderHttpExchange(httpExchange);
    }

    @Override
    public Response response() {
        return responseFromProviderHttpExchange(httpExchange);
    }

    @Override
    public void send(Response response) {
        Response.State state = response.take(Response.State.class);

        if (sent) {
            throw new IllegalStateException("Response was already sent to client");
        }
        sent = true;

        for (Map.Entry<String, String> entry: state.getHeaders().getHeaders().entrySet()) {
            httpExchange.getResponseHeaders().set(entry.getKey(), entry.getValue());
        }

        try {
            httpExchange.sendResponseHeaders(state.getStatusCode(), state.getContentLength());
            if (state.getBody() != null) {
                IOUtils.copy(state.getBody(), httpExchange.getResponseBody());
            }
            httpExchange.getResponseBody().close();
        } catch (IOException e) {
            if (! e.getMessage().equals("Broken pipe")) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private class Exceptions implements Exchange.Exceptions {

        private SunHttpServerExchange parent = SunHttpServerExchange.this;

        @Override
        public void push(Throwable t) {
            if (t != null) {
                parent.exceptions.push(t);
            }
        }

        @Override
        public Throwable pop() {
            return parent.exceptions.pop();
        }

        @Override
        public int size() {
            return parent.exceptions.size();
        }

        @Override
        public List<Throwable> getExceptions() {
            return parent.exceptions;
        }
    }

    @Override
    public <T> T take(Class<T> more) {
        if (more == Exchange.Exceptions.class) {
            return (T) new Exceptions();
        }

        return null;
    }

    private Request requestFromProviderHttpExchange(HttpExchange exchange) {
        MiniatureRequest request = new MiniatureRequest();
        Request.State state = request.take(Request.State.class);
        state.setInputStream(exchange.getRequestBody());
        state.setMethod(exchange.getRequestMethod());
        state.setUri(exchange.getRequestURI());
        state.setExchange(this);
        return request;
    }

    private Response responseFromProviderHttpExchange(HttpExchange exchange) {
        MiniatureResponse response = new MiniatureResponse();
        response.state().setExchange(this);
        response.state().setSent(sent);
        return response;
    }
}
