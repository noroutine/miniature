package me.noroutine.miniature;

import me.noroutine.miniature.http.Request;
import me.noroutine.miniature.http.Response;

import java.util.Iterator;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public abstract class Middleware implements Iterator<Middleware> {
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
