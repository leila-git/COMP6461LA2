package com.COMP6461.server;

public class ServerStartingParams {
    int port;
    boolean printDebugMessage;
    String filePath;

    public ServerStartingParams() {
        this.port = 8080;
        this.filePath = "";
    }

    @Override
    public String toString() {
        return "ServerStartingParams{" +
                "port=" + port +
                ", printDebugMessage=" + printDebugMessage +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
