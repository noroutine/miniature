package me.noroutine.miniature.http.spi.provider;

import com.sun.net.httpserver.*;
import me.noroutine.miniature.http.Handler;
import me.noroutine.miniature.http.spi.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/**
 * Proudly stolen from Spring Project
 * <p/>
 * Simple HTTP server, based on the HTTP server that is included in Sun's JRE 1.6.
 *
 * @author Oleksii Khilkevych
 * @since 02.09.13
 */
public class SunHttpServer implements HttpServer {

    private int port = 8080;

    private String hostname;

    private int backlog = -1;

    private int shutdownDelay = 0;

    private Executor executor;

    private Handler handler;

    private Authenticator authenticator;

    private com.sun.net.httpserver.HttpServer server;

    /**
     * Specify the HTTP server's port. Default is 8080.
     */
    public void port(int port) {
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
     */
    public void handler(Handler handler) {
        this.handler = handler;
    }

    /**
     * Register a common {@link com.sun.net.httpserver.Authenticator}
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

            if (this.handler != null) {
                HttpContext httpContext = this.server.createContext("/", new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        handler.handle(new SunHttpServerExchange(exchange));
                    }
                });

                if (this.authenticator != null) {
                    httpContext.setAuthenticator(this.authenticator);
                }
            }

            this.server.start();
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + port);
            System.exit(-1);
        }

    }

    public void destroy() {
        this.server.stop(this.shutdownDelay);
    }

}
