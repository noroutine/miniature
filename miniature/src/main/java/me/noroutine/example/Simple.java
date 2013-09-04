package me.noroutine.example;

import me.noroutine.Miniature;
import me.noroutine.miniature.http.Handler;
import me.noroutine.miniature.http.Middleware;
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

        mini.use(Miniature.logger());

        mini.use(new Middleware() {
            @Override
            public void handle(Request request, Response response) {
                log.info("Example middleware 1");
            }
        });

        mini.use(new Middleware() {
            @Override
            public void handle(Request request, Response response) {
                log.info("Example middleware 2");
            }
        });

        mini.get("/", new Handler() {
            @Override
            public void handle(Request request, Response response) {

                String body = "Hello, World\n";

                response.header("Content-Type", "text/plain");
//                response.header("Content-Length", body.length());

                response.length(body.length());

                response.status(200);


                response.body(body);

                response.send();
            }
        });

        mini.get("/test", new Handler() {
            @Override
            public void handle(Request request, Response response) {
                String hello = "Hello, Test\n";
                log.info("blah");
                response.header("Content-Type", "text/plain");
//                response.header("Content-Length", hello.length());

                response.length(hello.length());

                response.status(200);

                response.body(hello);

                response.send();
            }
        });

        mini.get("/123", new Handler() {
            @Override
            public void handle(Request request, Response response) {
                response.header("Blah", "123")
                        .send();
            }
        });

        log.info("Listening on port " + Config.PORT);

        mini.listen(Config.PORT);
    }
}
