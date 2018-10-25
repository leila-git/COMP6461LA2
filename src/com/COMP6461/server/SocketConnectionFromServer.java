package com.COMP6461.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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

    public void initiateServer() {
        try {
            ServerSocket serverConnect = new ServerSocket(serverStartingParams.port);
            System.out.println("Server started.\nListening for connections on port : " + serverStartingParams.port + " ...\n");

            // we listen until user halts server execution
            while (true) {
                SocketConnectionFromServer myServer = new SocketConnectionFromServer(serverStartingParams);
                myServer.socket = serverConnect.accept();
                if (serverStartingParams.printDebugMessage) {
                    System.out.println("Connection opened. (" + new Date() + ")");
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
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String input = in.readLine();
            StringTokenizer parse = new StringTokenizer(input);
            this.serverRequest.method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
            this.serverRequest.uri = parse.nextToken().toLowerCase();
            //"E:\\My stuff\\Masters Study Material\\6th term\\Assignment2\\serverDirectory\\abc.txt";
            boolean readHeaders = true;
            while ((input = in.readLine()) != null) {
                if (input == "") {
                    readHeaders = false;
                }
                if (readHeaders) {
                    this.serverRequest.headers.add(input);
                } else {
                    this.serverRequest.body.append(input);
                }
                this.performOperation();
            }
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
                File folder = new File(getClass().getResource("abc.txt").getPath());
                File[] listOfFiles = folder.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                    System.out.println("File " + listOfFiles[i].getName());
                    listOfFilesNames.append("\n" + listOfFiles[i].getName());
                    System.out.println("Directory " + listOfFiles[i].getName());
                }
                responseToSend.append("\nContent-Length:" + listOfFilesNames.length());
                responseToSend.append("\n");
                responseToSend.append(listOfFilesNames);


            } else {
                File file = new File(this.serverRequest.uri);
                if (!file.exists()) {
                    System.out.println("Wrong folder 404");
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
                            System.out.println(readLine);
                            linesFromFile.append(readLine);
                            responseToSend.append("\n " + readLine);
                        }
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    responseToSend.append("\nContent-type: text/plain");
                    responseToSend.append("\nContent-Length:" + linesFromFile.length());
                    responseToSend.append("\n");
                    responseToSend.append(linesFromFile);
                }
            }
        }
        if (this.serverRequest.method.toLowerCase().equals("post")) {
            if (!this.serverRequest.uri.equals("")) {
                File file = new File(this.serverRequest.uri);
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
                    writer.write(this.serverRequest.body.toString());
                    responseToSend.append("200 Ok");
                    responseToSend.append("\nDate: " + new Date());
                    responseToSend.append("\n" + SERVER_NAME);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        this.sendResponse();
    }

    public void sendResponse() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.print(this.responseToSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
