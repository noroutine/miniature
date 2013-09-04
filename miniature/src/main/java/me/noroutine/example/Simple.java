package me.noroutine.example;

import me.noroutine.Miniature;
import me.noroutine.miniature.Handler;
import me.noroutine.miniature.Middleware;
import me.noroutine.miniature.http.Request;
import me.noroutine.miniature.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oleksii Khilkevych
 * @since 02.09.13
 */
public class Simple {

    static class Config {
        static int PORT = 3000;
    }

    private static final Logger log = LoggerFactory.getLogger(Simple.class);

    public static void main(String[] args) {


        Miniature mini = new Miniature();

        mini.use(new Middleware() {
            @Override
            public Middleware handle(Request request, Response response) {
                log.info("Example middleware 1");
                return null;
            }
        });

        mini.use(new Middleware() {
            @Override
            public Middleware handle(Request request, Response response) {
                log.info("Example middleware 2");
                return null;
            }
        });

        mini.use(Miniature.logger());

//        mini.get("/", new Handler() {
//            @Override
//            public void handle(Request request, Response response) {
//                String body = "Hello, World\n";
//
//                H
//                response.header("Content-Type", "text/plain");
//                response.header("Content-Length", body.length());
//                response.status(200);
//
//                response.body(body);
//
//                response.send();
//            }
//        });
//
        mini.get("/test", new Handler() {
            @Override
            public void handle(Request request, Response response) {
                String hello = "Hello, Test\n";

                Response.Headers headers = response.take(Response.Headers.class);

                Response.Body body = response.take(Response.Body.class);
                Response.Status status = response.take(Response.Status.class);

                Response.Sender sender = response.take(Response.Sender.class);

                headers.header("Content-Type", "text/plain");
                headers.header("Content-Length", hello.length());

                status.status(200);

                body.body(hello);

                sender.send();
            }
        });

        log.info("Listening on port " + Config.PORT);

        mini.listen(Config.PORT);
    }
}
