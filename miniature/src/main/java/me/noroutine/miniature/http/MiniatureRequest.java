package me.noroutine.miniature.http;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URI;

/**
* @author Oleksii Khilkevych
* @since 03.09.13
*/
public class MiniatureRequest implements Request {

    private InputStream inputStream;

    private String method;

    private URI uri;

    public MiniatureRequest()  {
    }

    @Override
    public String body() {
        StringWriter body = new StringWriter();
        try {
            IOUtils.copy(inputStream, body);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return body.toString();
    }

    @Override
    public String method() {
        return method;
    }

    @Override
    public String url() {
        return uri.toString();
    }

    @Override
    public Request.State state() {
        return take(Request.State.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T take(Class<T> more) {
        if (more == Request.State.class) {
            return (T) new State();
        }

        return null;
    }

    private class State implements Request.State {
        private MiniatureRequest parent = MiniatureRequest.this;

        @Override
        public InputStream getInputStream() {
            return parent.inputStream;
        }

        @Override
        public void setInputStream(InputStream inputStream) {
            parent.inputStream = inputStream;
        }

        @Override
        public String getMethod() {
            return parent.method;
        }

        @Override
        public void setMethod(String method) {
            parent.method = method;
        }

        @Override
        public URI getUri() {
            return parent.uri;
        }

        @Override
        public void setUri(URI uri) {
            parent.uri = uri;
        }
    }
}
