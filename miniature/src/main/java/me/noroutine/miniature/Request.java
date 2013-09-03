package me.noroutine.miniature;

import java.io.InputStream;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public interface Request {
    InputStream stream();

    String method();
    String url();
}
