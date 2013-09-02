package me.noroutine;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * @author Oleksii Khilkevych
 * @since 02.09.13
 */
public class Miniature {

    private List<Middleware> middlewares = new LinkedList<Middleware>();

    private ServerSocket serverSocket;

    private Handler handler;

    public static interface Handler {
        void handle(Request request, Response response) throws Throwable;
    }

    public static abstract class Middleware implements Iterator<Middleware> {
        public abstract Middleware handle(Request request, Response response);

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Middleware next() {
            return null;
        }

        @Override
        public void remove() {
        }
    }

    public static interface Request {

        Request header(String header, String value);
        Request header(String header, int value);

        InputStream stream();

        String method();
        String url();
    }

    private static class MiniatureRequest implements Request {
        private InputStream inputStream;

        private MiniatureRequest(Socket socket) throws IOException {
            inputStream = socket.getInputStream();
        }

        @Override
        public Request header(String header, String value) {
            return null;
        }

        @Override
        public Request header(String header, int value) {
            return null;
        }

        @Override
        public InputStream stream() {
            return inputStream;
        }

        @Override
        public String method() {
            return null;
        }

        @Override
        public String url() {
            return null;
        }
    }

    private static class MiniatureResponse implements Response {

        private OutputStream outputStream;

        private MiniatureResponse(Socket socket) throws IOException {
            this.outputStream = socket.getOutputStream();
        }

        @Override
        public Response status(int statusCode) {
            return null;
        }

        @Override
        public Response send(CharSequence body) {
            try {
                outputStream.write(body.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return this;
        }

        @Override
        public OutputStream stream() {
            return outputStream;
        }
    }

    public static interface Response {
        Response status(int statusCode);
        Response send(CharSequence body);

        OutputStream stream();
    }

    public static Middleware logger() {
        return new Middleware() {

            @Override
            public Middleware handle(Request request, Response response) {
                System.out.printf("%s %s", request.method(), request.url());
                return this.next();
            }
        };
    }

    // TODO (oleksiy on 02.09.13): implement http engine
    public Miniature listen(int port) {

        // TODO (oleksiy on 02.09.13): wire up all components


        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println("Could not listen on port: " + port);
            System.exit(-1);
        }


        // TODO (oleksiy on 02.09.13): start listener
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                MiniatureRequest request = new MiniatureRequest(clientSocket);
                MiniatureResponse response = new MiniatureResponse(clientSocket);

                for (Middleware middleware: middlewares) {
                    middleware.handle(request, response);
                }

                handler.handle(request, response);
            } catch (IOException e1) {
                System.out.println("Accept failed: " + port);
                System.exit(-1);
            } catch (Throwable throwable) {
                throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public Miniature use(Middleware... ms) {
        Collections.addAll(middlewares, ms);
        return this;
    }

    // TODO (oleksiy on 02.09.13): implement url patterns
    public Miniature get(CharSequence pattern, Handler handler) {
        this.handler = handler;
        return this;
    }

}
