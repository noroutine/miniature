package me.noroutine;

import com.sun.net.httpserver.*;
import me.noroutine.miniature.*;
import me.noroutine.miniature.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author Oleksii Khilkevych
 * @since 02.09.13
 */
public class Miniature {

    private static final Logger log = LoggerFactory.getLogger(Miniature.class);

    private List<Middleware> middlewares = new LinkedList<Middleware>();

    private Map<String, Handler> handlers = new HashMap<String, Handler>();

    public static Middleware logger() {
        return new Middleware() {

            @Override
            public Middleware handle(Request request, Response response) {
                log.info("{} {}", request.method(), request.url());
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
                        log.info("executing handler for {}", requestMapping.getKey());
                        MiniatureRequest request = new MiniatureRequest(exchange);
                        MiniatureResponse response = new MiniatureResponse(exchange);
                        requestMapping.getValue().handle(request, response);
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
