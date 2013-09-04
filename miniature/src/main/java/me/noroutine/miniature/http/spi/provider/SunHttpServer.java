package me.noroutine.miniature.http.spi.provider;

import com.sun.net.httpserver.*;
import me.noroutine.miniature.http.*;
import me.noroutine.miniature.http.spi.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * Proudly stolen from Spring Project
 *
 * {@link org.springframework.beans.factory.FactoryBean} that creates a simple
 * HTTP server, based on the HTTP server that is included in Sun's JRE 1.6.
 * Starts the HTTP server on initialization and stops it on destruction.
 * Exposes the resulting {@link com.sun.net.httpserver.HttpServer} object.
 * <p/>
 * <p>Allows for registering {@link com.sun.net.httpserver.HttpHandler HttpHandlers}
 * for specific {@link #setContexts context paths}. Alternatively,
 * register such context-specific handlers programmatically on the
 * {@link com.sun.net.httpserver.HttpServer} itself.
 *
 * @author Oleksii Khilkevych
 * @author Juergen Hoeller
 * @author Arjen Poutsma
 * @since 02.09.13
 */
public class SunHttpServer implements HttpServer {

    private int port = 8080;

    private String hostname;

    private int backlog = -1;

    private int shutdownDelay = 0;

    private Executor executor;

    private Map<String, Handler> handlers;

    private List<Filter> filters;

    private Authenticator authenticator;

    private com.sun.net.httpserver.HttpServer server;


    /**
     * Specify the HTTP server's port. Default is 8080.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Specify the HTTP server's hostname to bind to. Default is localhost;
     * can be overridden with a specific network address to bind to.
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Specify the HTTP server's TCP backlog. Default is -1,
     * indicating the system's default value.
     */
    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    /**
     * Specify the number of seconds to wait until HTTP exchanges have
     * completed when shutting down the HTTP server. Default is 0.
     */
    public void setShutdownDelay(int shutdownDelay) {
        this.shutdownDelay = shutdownDelay;
    }

    /**
     * Set the JDK concurrent executor to use for dispatching incoming requests.
     *
     * @see com.sun.net.httpserver.HttpServer#setExecutor
     */
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    /**
     * Register {@link com.sun.net.httpserver.HttpHandler HttpHandlers}
     * for specific context paths.
     *
     * @param contexts a Map with context paths as keys and HttpHandler
     *                 objects as values
     * @see org.springframework.remoting.httpinvoker.SimpleHttpInvokerServiceExporter
     * @see org.springframework.remoting.caucho.SimpleHessianServiceExporter
     * @see org.springframework.remoting.caucho.SimpleBurlapServiceExporter
     */
    public void setHandlers(Map<String, Handler> handlers) {
        this.handlers = handlers;
    }

    /**
     * Register common {@link com.sun.net.httpserver.Filter Filters} to be
     * applied to all locally registered {@link #setContexts contexts}.
     */
    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    /**
     * Register a common {@link com.sun.net.httpserver.Authenticator} to be
     * applied to all locally registered {@link #setContexts contexts}.
     */
    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public void start() {
        try {
            InetSocketAddress address = (this.hostname != null ?
                    new InetSocketAddress(this.hostname, this.port) : new InetSocketAddress(this.port));
            this.server = com.sun.net.httpserver.HttpServer.create(address, this.backlog);
            if (this.executor != null) {
                this.server.setExecutor(this.executor);
            }
            if (this.handlers != null) {
                for (String key : this.handlers.keySet()) {
                    HttpContext httpContext = this.server.createContext(key, httpHandlerFromHandler(this.handlers.get(key)));
                    if (this.filters != null) {
                        httpContext.getFilters().addAll(this.filters);
                    }

                    if (this.authenticator != null) {
                        httpContext.setAuthenticator(this.authenticator);
                    }
                }
            }

            this.server.start();
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + port);
            System.exit(-1);
        }

    }

    public com.sun.net.httpserver.HttpServer getObject() {
        return this.server;
    }

    public Class<? extends com.sun.net.httpserver.HttpServer> getObjectType() {
        return (this.server != null ? this.server.getClass() : com.sun.net.httpserver.HttpServer.class);
    }

    public boolean isSingleton() {
        return true;
    }

    public void destroy() {
        this.server.stop(this.shutdownDelay);
    }

    @Override
    public void setMiddlewares(List<Middleware> middlewares) {
        LinkedList<Filter> filters = new LinkedList<Filter>();

        for (final Middleware middleware: middlewares) {
            filters.add(new Filter() {
                @Override
                public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
                    Exchange x = new SunHttpServerExchange(httpExchange);
                    Request request = x.request();
                    Response response = x.response();

                    middleware.handle(request, response);

                    chain.doFilter(httpExchange);
                }

                @Override
                public String description() {
                    return null;
                }
            });
        }

        this.filters = filters;
    }

    private HttpHandler httpHandlerFromHandler(final Handler handler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {

                Exchange x = new SunHttpServerExchange(exchange);
                Request request = x.request();
                Response response = x.response();
                handler.handle(request, response);
            }
        };
    }
}
