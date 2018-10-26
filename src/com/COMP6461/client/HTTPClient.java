package com.COMP6461.client;

import java.util.Scanner;

public class HTTPClient {

    public static void main(String[] args) {
        boolean keepGoing = true;
        boolean executeCommand = true;
        while (keepGoing) {
            System.out.println("Please enter command from client..");
            Scanner scanner = new Scanner(System.in);
            String arguments[] = scanner.nextLine().split(" ");
            for (String s : arguments) {
                if (s.toLowerCase().equals("exit")) {
                    keepGoing = false;
                    executeCommand = false;
                }
            }
            if (executeCommand) {
                RequestLineParams httpObject = createHTTPObject(arguments);
                SocketConnectionFromClient.execute(httpObject);
            }
        }
    }

    public static RequestLineParams createHTTPObject(String ar[]) {
        RequestLineParams http = new RequestLineParams();
        for (int i = 0; i < ar.length; i++) {
            if (i == 1) {
                if (ar[i].toLowerCase().equals("get")) {
                    http.typeRequest = "GET";
                } else if (ar[i].toLowerCase().equals("post")) {
                    http.typeRequest = "POST";
                } else {
                    System.out.println("Un-Supported Type");
                }
            }
            if (ar[i].equals("-v")) {
                http.isVerbose = true;
            }
            if (ar[i].equals("-h")) {
                http.headers.add(ar[i + 1]);
            }
            if (ar[i].equals("-d")) {
                http.isInline = true;
                http.data = ar[i + 1].replace("'", "");
            }
            if (ar[i].equals("-f")) {
                http.readFromFile = true;
                http.fileName = ar[i + 1].replace("'", "");
            }
            if (ar[i].startsWith("127")) {
                http.url = ar[i];
                String urlParts[] = http.url.split("/");
                if (urlParts.length > 1) {
                    http.urlParameters += urlParts[1];
                }
                http.host = urlParts[0].split(":")[0];
                http.port = Integer.parseInt(urlParts[0].split(":")[1]);

            }
            if (ar[i].equals("-o")) {
                http.writeTofile = true;
                http.fileName = ar[i + 1];
            }
        }
        return http;
    }
}

