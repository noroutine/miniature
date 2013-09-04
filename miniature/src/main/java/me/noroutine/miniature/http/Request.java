package me.noroutine.miniature.http;

import com.sun.net.httpserver.HttpPrincipal;

import java.io.InputStream;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public interface Request {
    InputStream body();

    String method();
    String url();
    HttpPrincipal principal();

    Object attribute(java.lang.String s);
    void attribute(java.lang.String s, java.lang.Object o);

}
