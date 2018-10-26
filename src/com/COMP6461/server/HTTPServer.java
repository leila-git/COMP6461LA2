package com.COMP6461.server;

import java.util.Scanner;

public class HTTPServer {

    public static void main(String[] args) {
        boolean executeCommand = true;
        System.out.println("Please enter command to start server...");
        Scanner scanner = new Scanner(System.in);
        String arguments[] = scanner.nextLine().split(" ");
        for (String s : arguments) {
            if (s.toLowerCase().equals("exit")) {
                executeCommand = false;
            }
        }
        if (executeCommand) {
            ServerStartingParams httpObject = createHTTPObject(arguments);
            SocketConnectionFromServer server = new SocketConnectionFromServer(httpObject);
            server.initiateServer();
        }
    }

    public static ServerStartingParams createHTTPObject(String ar[]) {
        ServerStartingParams http = new ServerStartingParams();
        for (int i = 0; i < ar.length; i++) {
            if (ar[i].equals("-v")) {
                http.printDebugMessage = true;
            }
            if (ar[i].equals("-p")) {
                http.port = Integer.parseInt(ar[i + 1]);
            }
            if (ar[i].equals("-d")) {
                http.filePath = ar[i + 1].replace("'", "");
            }
        }
        return http;
    }
}
