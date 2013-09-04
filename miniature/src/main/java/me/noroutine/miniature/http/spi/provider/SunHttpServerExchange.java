package me.noroutine.miniature.http.spi.provider;

import com.sun.net.httpserver.HttpExchange;
import me.noroutine.miniature.http.*;
import me.noroutine.miniature.http.Exchange;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author Oleksii Khilkevych
 * @since 04.09.13
 */
public class SunHttpServerExchange implements Exchange {
    private HttpExchange httpExchange;

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

        for (Map.Entry<String, String> entry: state.getHeaders().getHeaders().entrySet()) {
            httpExchange.getResponseHeaders().set(entry.getKey(), entry.getValue());
        }

        try {
            httpExchange.sendResponseHeaders(state.getStatusCode(), state.getContentLength());
            IOUtils.copy(state.getBody(), httpExchange.getResponseBody());
            httpExchange.getResponseBody().close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
        return response;
    }
}
