package me.noroutine.example;

import me.noroutine.Miniature;
import me.noroutine.miniature.http.*;
import me.noroutine.miniature.http.handler.*;
import me.noroutine.miniature.http.handler.routing.UrlPatternRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;

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

//        mini.use(Miniature.logger());
//
//        mini.use(new Middleware() {
//            @Override
//            public void handle(Request request, Response response) {
//                log.info("Example middleware 1");
//            }
//        });
//
//        mini.use(new Middleware() {
//            @Override
//            public void handle(Request request, Response response) {
//                log.info("Example middleware 2");
//            }
//        });

//        mini.get("/", new Handler() {
//            @Override
//            public void handle(Exchange x) {
//                Response response = x.response();
//
//                String body = "Hello, World\n";
//
//                response.header("Content-Type", "text/plain");
////                response.header("Content-Length", body.length());
//
//                response.length(body.length());
//
//                response.status(200);
//
//
//                response.body(body);
//
//                response.send();
//            }
//        });

//        mini.get("/test.*", new Handler() {
//            @Override
//            public void handle(Exchange exchange) {
//
//                Response response = exchange.response();
//
//                String hello = "Hello, Test\n";
//                log.info("blah");
//                response.header("Content-Type", "text/plain");
//                response.header("Content-Length", hello.length());
//
//                response.length(hello.length());
//
//                response.status(200);
//
//                response.body(hello);
//
//                response.send();
//            }
//        });

//        mini.get("/123", new Handler() {
//            @Override
//            public void handle(Request request, Response response) {
//                response.header("Blah", "123")
//                        .send();
//            }
//        });


        mini.handler(buildHandlerChain());

        mini.listen(Config.PORT);
        log.info("Listening on port " + Config.PORT);
    }

    private static Handler buildHandlerChain() {
        // top-level handler chain which includes exception handling and fallback 404 handler
        return new HandlerChain(new LinkedList<Handler>() {{
            add(new ExceptionCatcher(
                    new UrlPatternRouter(new HashMap<String, Handler>() {{
                        put("/test.*", new Handler() {
                            @Override
                            public void handle(Exchange exchange) {

                                Response response = exchange.response();

                                String hello = "Hello, Test\n";
                                log.info("blah");
                                response.header("Content-Type", "text/plain");
                                response.header("Content-Length", hello.length());

                                response.length(hello.length());

                                response.status(200);

                                response.body(hello);

                                response.send();
                            }
                        });

                        put("/", new Handler() {
                            @Override
                            public void handle(Exchange exchange) {
                                Response response = exchange.response();

                                String body = "Hello, World\n";

                                response.header("Content-Type", "text/plain");
                                //                response.header("Content-Length", body.length());

                                response.length(body.length());

                                response.status(200);


                                response.body(body);

                                response.send();
                            }
                        });
                        put("/exception", new Handler() {
                            @Override
                            public void handle(Exchange exchange) {
                                Object a = new Object[] { 1, 2, 3} [5];
                            }
                        });
                    }})
            ));

            // Global exception handler
            add(new ExceptionHandler());

            // global not found
            add(new NotFound());
        }});
    }

}
