package me.noroutine.miniature;

import java.util.List;
import java.util.Map;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public interface Response {
    Response status(int statusCode);
    Response length(long length);
    Response headers(Map<? extends String, ? extends List<String>> headers);
    Response header(String header, String value);
    Response header(String header, int value);
    Response send(CharSequence body);
}
