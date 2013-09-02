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
                System.out.println("Example middleware ");
                return null;
            }
        });

        mini.get("/", new Handler() {
            @Override
            public void handle(Request request, Response response) throws Throwable {
                String body = "Hello, World\n";

                request.header("Content-Type", "text/plain");
                request.header("Content-Length", body.length());

                response.status(200);
                response.send(body);

                response.stream().write(body.getBytes());
                response.stream().close();
            }
        });

        System.out.println("Listening on port " + Config.PORT);

        mini.listen(Config.PORT);
    }
}
