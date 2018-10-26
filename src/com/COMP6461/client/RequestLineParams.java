package com.COMP6461.client;

import java.util.ArrayList;

public class RequestLineParams {
    String host;
    String url;
    int port;
    String urlParameters;
    String data;
    String typeRequest;
    boolean isVerbose;
    boolean isInline;
    boolean readFromFile;
    boolean writeTofile;
    String fileName;
    ArrayList<String> headers;

    public RequestLineParams() {
        headers = new ArrayList<String>();
        urlParameters = "/";
    }

    @Override
    public String toString() {
        return "RequestLineParams{" +
                "host='" + host + '\'' +
                ", url='" + url + '\'' +
                ", port=" + port +
                ", urlParameters='" + urlParameters + '\'' +
                ", data='" + data + '\'' +
                ", typeRequest='" + typeRequest + '\'' +
                ", isVerbose=" + isVerbose +
                ", isInline=" + isInline +
                ", readFromFile=" + readFromFile +
                ", writeTofile=" + writeTofile +
                ", fileName='" + fileName + '\'' +
                ", headers=" + headers +
                '}';
    }
}
