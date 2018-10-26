package com.COMP6461.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.StringTokenizer;

public class SocketConnectionFromServer implements Runnable {
    public static String SERVER_NAME = "Server: Concordia COMP 6461";
    private ServerStartingParams serverStartingParams;
    private Socket socket;
    private HttpServerRequest serverRequest;
    private StringBuilder responseToSend;

    public SocketConnectionFromServer(ServerStartingParams serverStartingParams) {
        this.serverStartingParams = serverStartingParams;
    }

    public void printIfDebug(String str) {
        if (this.serverStartingParams.printDebugMessage)
            System.out.println(str);
    }

    public void initiateServer() {
        try {
            ServerSocket serverConnect = new ServerSocket(serverStartingParams.port);
            this.printIfDebug("Server started.\nListening for connections on port : " + serverStartingParams.port + " ...\n");

            // we listen until user halts server execution
            while (true) {
                SocketConnectionFromServer myServer = new SocketConnectionFromServer(serverStartingParams);
                myServer.socket = serverConnect.accept();
                if (serverStartingParams.printDebugMessage) {
                    this.printIfDebug("Connection opened. (" + new Date() + ")");
                }

                // create dedicated thread to manage the client connection
                Thread thread = new Thread(myServer);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        BufferedReader in = null;
        this.serverRequest = new HttpServerRequest();
        this.printIfDebug("Server started with request from client...");
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String input = in.readLine();
            StringTokenizer parse = new StringTokenizer(input);
            this.serverRequest.method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
            this.serverRequest.uri = parse.nextToken().toLowerCase();
            int postDataI = -1;
            String line = "";
            while ((line = in.readLine()) != null && (line.length() != 0)) {
                this.serverRequest.headers.add(line);
                if (line.indexOf("Content-Length:") > -1) {
                    postDataI = new Integer(
                            line.substring(
                                    line.indexOf("Content-Length:") + 16,
                                    line.length())).intValue();
                }
            }
            String postData = "";
            // read the post data
            if (postDataI > 0) {
                char[] charArray = new char[postDataI];
                in.read(charArray, 0, postDataI);
                postData = new String(charArray);
            }
            this.serverRequest.body.append(postData);
            this.performOperation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void performOperation() {
        responseToSend = new StringBuilder();
        responseToSend.append("HTTP/1.1 ");
        if (this.serverRequest.method.toLowerCase().equals("get")) {
            // 1.
            if (this.serverRequest.uri.equals("/")) {
                responseToSend.append("200 Ok");
                responseToSend.append("\nDate: " + new Date());
                responseToSend.append("\n" + SERVER_NAME);
                StringBuilder listOfFilesNames = new StringBuilder();
                Path currentRelativePath = Paths.get("");
                File folder = new File(currentRelativePath.toAbsolutePath().toString() + this.serverStartingParams.filePath);
                File[] listOfFiles = folder.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                    listOfFilesNames.append("\n" + listOfFiles[i].getName());
                }
                responseToSend.append("\nContent-Length:" + listOfFilesNames.length());
                responseToSend.append("\n");
                responseToSend.append("\n{" + listOfFilesNames + "\n}");
            } else {
                Path currentRelativePath = Paths.get("");
                File file = new File(currentRelativePath.toAbsolutePath().toString() + this.serverRequest.uri);
                if (!file.exists()) {
                    responseToSend.append("404 Not Found");
                    responseToSend.append("\nDate: " + new Date());
                    responseToSend.append("\n" + SERVER_NAME);
                } else {
                    responseToSend.append("200 Ok");
                    responseToSend.append("\nDate: " + new Date());

                    responseToSend.append("\n" + SERVER_NAME);
                    String readLine = "";
                    StringBuilder linesFromFile = new StringBuilder();
                    BufferedReader bufferedReader = null;
                    try {
                        bufferedReader = new BufferedReader(new FileReader(file));
                        while ((readLine = bufferedReader.readLine()) != null) {
                            linesFromFile.append("\n" + readLine);
                        }
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    responseToSend.append("\nContent-type: text/plain");
                    responseToSend.append("\nContent-Length:" + linesFromFile.length());
                    responseToSend.append("\n");
                    responseToSend.append("\n{" + linesFromFile + "\n}");
                }
            }
        }
        if (this.serverRequest.method.toLowerCase().equals("post")) {
            if (!this.serverRequest.uri.equals("")) {
                Path currentRelativePath = Paths.get("");
                File file = new File(currentRelativePath.toAbsolutePath().toString() + this.serverRequest.uri);
                if (file.exists()) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
                        writer.write(this.serverRequest.body.toString());
                        responseToSend.append("200 Ok");
                        responseToSend.append("\nDate: " + new Date());
                        responseToSend.append("\n" + SERVER_NAME);
                        responseToSend.append("\n");
                        responseToSend.append("\n{" + this.serverRequest.body + "\n}");
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    responseToSend.append("404 File not Found");
                    responseToSend.append("\nDate: " + new Date());
                    responseToSend.append("\n" + SERVER_NAME);
                    responseToSend.append("\n");
                    responseToSend.append("\n{" + this.serverRequest.body + "\n}");
                }
            }
        }
        this.sendResponse();
    }

    public void sendResponse() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.print(this.responseToSend);
            out.flush();
            printIfDebug("Closing Connection with Client.");
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
