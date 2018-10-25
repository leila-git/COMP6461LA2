package com.COMP6461.client;

import java.util.ArrayList;

public class RequestLineParams {
    String host;
    String url;
    String urlParamaters;
    String data;
    String typeRequest;
    boolean isVerbose;
    boolean isInline;
    boolean readFromFile;
    boolean writeTofile;
    String fileName;
    ArrayList<String> headers;
    public RequestLineParams(){
        headers = new ArrayList<String>();
    }

    @Override
    public String toString() {
        return "RequestLineParams{" +
                "\nhost='" + host + '\'' +
                ", \nurl='" + url + '\'' +
                ", \nurlParamaters='" + urlParamaters + '\'' +
                ", \ndata='" + data + '\'' +
                ", \ntypeRequest='" + typeRequest + '\'' +
                ", \nisVerbose=" + isVerbose +
                ", \nisInline=" + isInline +
                ", \nreadFromFile=" + readFromFile +
                ", \nwriteTofile=" + writeTofile +
                ", \nfileName='" + fileName + '\'' +
                ", \nheaders=" + headers +
                '}';
    }
}
