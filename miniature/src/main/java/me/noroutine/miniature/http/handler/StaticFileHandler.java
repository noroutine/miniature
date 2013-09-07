package me.noroutine.miniature.http.handler;

import me.noroutine.miniature.http.*;

import java.io.*;
import java.util.HashMap;

/**
 * @author Oleksii Khilkevych
 * @since 07.09.13
 */
public class StaticFileHandler implements Handler {

    private static HashMap<String, String> TYPES = new HashMap<String, String>() {{

        put("css", "text/css");
        put("js", "application/javascript");
        put("json", "application/javascript");
        put("html", "text/html");
        put("jpg", "image/jpeg");
        put("png", "image/png");
        put("eot", "application/vnd.ms-fontobject");
        put("woff", "application/font-woff");
        put("ttf", "application/octet-stream");
        put("svg", "image/svg+xml");
    }};

    private String folder;

    public StaticFileHandler(String folder) {
        this.folder = folder;
    }

    @Override
    public void handle(Exchange exchange) {
        File file = new File(folder + exchange.request().url());

        try {
            if (file.exists()) {

                if (file.isDirectory()) {
                    file = new File(folder + exchange.request().url() + "/index.html");
                }

                Response response = exchange.response();

                String contentType = getMimeType(file);
                if (contentType != null) {
                    response.status(200)
                            .header("Content-Type", contentType)
                            .take(Response.State.class).setBody(new FileInputStream(file));

                    response.send();
                }
            }
        } catch (IOException e) {
             e.printStackTrace();
        }
    }

    private String getMimeType(File file) {
        int lastDot = file.getName().lastIndexOf(".");
        String extension = lastDot < 0 ? null : file.getName().substring(lastDot + 1);
        return TYPES.get(extension);
    }

}
