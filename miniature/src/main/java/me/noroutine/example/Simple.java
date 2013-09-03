package me.noroutine.example;

import me.noroutine.Miniature;
import me.noroutine.miniature.*;
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

        mini.get("/", new Handler() {
            @Override
            public void handle(Request request, Response response) {
                String body = "Hello, World\n";

                response.header("Content-Type", "text/plain");

                response.length(body.length());
                response.status(200);

                response.send(body);
            }
        });

        mini.get("/test", new Handler() {
            @Override
            public void handle(Request request, Response response) {
                String body = "Hello, Test\n";

                response.header("Content-Type", "text/plain");

                response.length(body.length());
                response.status(200);

                response.send(body);
            }
        });

        log.info("Listening on port " + Config.PORT);

        mini.listen(Config.PORT);
    }
}
