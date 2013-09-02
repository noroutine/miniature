package me.noroutine.example;

import me.noroutine.Miniature;

import static me.noroutine.Miniature.*;

/**
 * @author Oleksii Khilkevych
 * @since 02.09.13
 */
public class Simple {

    static class Config {
        static int PORT = 3000;
    }

    public static void main(String[] args) {

        Miniature mini = new Miniature();

        mini.use(new Middleware() {
            @Override
            public Middleware handle(Request request, Response response) {
                System.out.println("Example middleware 1");
                return null;
            }
        });

        mini.use(new Middleware() {
            @Override
            public Middleware handle(Request request, Response response) {
                System.out.println("Example middleware 2");
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

        System.out.println("Listening on port " + Config.PORT);

        mini.listen(Config.PORT);
    }
}
