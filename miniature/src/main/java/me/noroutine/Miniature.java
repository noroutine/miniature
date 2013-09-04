package me.noroutine;

import me.noroutine.miniature.http.Middleware;
import me.noroutine.miniature.http.*;
import me.noroutine.miniature.http.spi.HttpServer;
import me.noroutine.miniature.http.spi.provider.SunHttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Oleksii Khilkevych
 * @since 02.09.13
 */
public class Miniature {

    private Class<? extends HttpServer> DEFAULT_HTTP_SERVER = SunHttpServer.class;

    private static final Logger log = LoggerFactory.getLogger(Miniature.class);

    private List<Middleware> middlewares = new LinkedList<Middleware>();

    private Map<String, Handler> handlers = new HashMap<String, Handler>();

    public static Middleware logger() {
        return new Middleware() {

            @Override
            public void handle(Request request, Response response) {
                log.info("{} {}", request.method(), request.url());
            }
        };
    }

    private HttpServer createHttpServer() {
        HttpServer server = null;
        ServiceLoader<HttpServer> serverLoader = ServiceLoader.load(HttpServer.class);

        Iterator<HttpServer> serverIterator = serverLoader.iterator();

        if (serverIterator.hasNext()) {
           server = serverIterator.next();
        } else {
            try {
                server = DEFAULT_HTTP_SERVER.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        return server;
    }

    public Miniature listen(int port) {

        HttpServer server = createHttpServer();

        server.setPort(port);
        server.setMiddlewares(middlewares);
        server.setHandlers(handlers);
        server.start();

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
