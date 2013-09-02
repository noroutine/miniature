package me.noroutine;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Oleksii Khilkevych
 * @since 02.09.13
 */
public class Miniature {

    private List<Middleware> middlewares = new LinkedList<Middleware>();

    private Map<String, Handler> handlers = new HashMap<String, Handler>();

    public static interface Handler {
        void handle(Request request, Response response) throws Throwable;
    }

    public static abstract class Middleware implements Iterator<Middleware> {
        public abstract Middleware handle(Request request, Response response);

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Middleware next() {
            return null;
        }

        @Override
        public void remove() {
        }
    }

    public static interface Request {
        InputStream stream();

        String method();
        String url();
    }

    public static interface Response {
        Response status(int statusCode);
        Response length(long length);
        Response headers(Map<? extends String, ? extends List<String>> headers);
        Response header(String header, String value);
        Response header(String header, int value);
        Response send(CharSequence body);
    }


    private static class MiniatureRequest implements Request {
        private HttpExchange exchange;

        private MiniatureRequest(HttpExchange exchange) throws IOException {
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

    private static class MiniatureResponse implements Response {

        private HttpExchange exchange;

        private int statusCode;

        private long contentLength;

        private MiniatureResponse(HttpExchange exchange) throws IOException {
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


    public static Middleware logger() {
        return new Middleware() {

            @Override
            public Middleware handle(Request request, Response response) {
                System.out.printf("%s %s\n", request.method(), request.url());
                return this.next();
            }
        };
    }

    public Miniature listen(int port) {

        HttpServer httpServer = new HttpServer();

        httpServer.setPort(port);

        LinkedList<Filter> filters = new LinkedList<Filter>();

        for (final Middleware middleware: middlewares) {
            filters.add(new Filter() {
                @Override
                public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
                    MiniatureRequest request = new MiniatureRequest(httpExchange);
                    MiniatureResponse response = new MiniatureResponse(httpExchange);

                    middleware.handle(request, response);

                    chain.doFilter(httpExchange);
                }

                @Override
                public String description() {
                    return null;
                }
            });
        }

        httpServer.setFilters(filters);

        httpServer.setContexts(new HashMap<String, HttpHandler>() {{

            for (final Map.Entry< String, Handler> requestMapping:  handlers.entrySet()) {
                put(requestMapping.getKey(), new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        System.out.println("executing");
                        MiniatureRequest request = new MiniatureRequest(exchange);
                        MiniatureResponse response = new MiniatureResponse(exchange);
                        try {
                            requestMapping.getValue().handle(request, response);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                });
            }
        }});

        try {
            httpServer.afterPropertiesSet();
        }
        catch (IOException e) {
            System.out.println("Could not listen on port: " + port);
            System.exit(-1);
        }

        return this;
    }

    public Miniature use(Middleware... ms) {
        Collections.addAll(middlewares, ms);
        return this;
    }

    public Miniature get(String pattern, Handler handler) {
        this.handlers.put(pattern, handler);
        return this;
    }

}
