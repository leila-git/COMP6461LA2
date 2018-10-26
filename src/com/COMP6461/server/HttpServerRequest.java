package com.COMP6461.server;

import java.util.ArrayList;

public class HttpServerRequest {
    String method;
    String uri;
    ArrayList<String> headers;
    StringBuilder body;

    public HttpServerRequest() {
        this.method = "";
        this.uri = "";
        this.headers = new ArrayList<>();
        this.body = new StringBuilder();
    }

    @Override
    public String toString() {
        return "HttpServerRequest{" +
                "method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", headers=" + headers +
                ", body=" + body +
                '}';
    }
}
