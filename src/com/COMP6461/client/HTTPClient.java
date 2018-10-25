package com.COMP6461.client;

import java.util.Scanner;

public class HTTPClient {

    public static void main(String[] args) {
        boolean keepGoing = true;
        boolean executeCommand = true;
        while (keepGoing) {
            System.out.println("Please enter command.");
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
                // System.out.println(httpObject);
                SocketConnectionFromClient.execute(httpObject);
            }
        }



//        Socket s = new Socket("localHost", 8080);
//        String body = new String();
//        String response_header = new String();
//        OutputStreamWriter output = new OutputStreamWriter(s.getOutputStream());
//        PrintWriter out = new PrintWriter(output);
//        String header = "POST /home/negar/new/ HTTP/1.0\r\nContent-Length: 3\r\nContent-Type: text/plain\r\n\r\nnow";
//        // System.out.println(header);
//        out.print(header);
//        out.flush();
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
//        String message = "";
//        String outRead = "";
//        //     output = in.read
//        // Scanner scanner = new Scanner(in.readLine());
//        while (true) {
//
//            String line = in.readLine();
//
//            if (line.isEmpty()) {
//                break;
//            }
//            response_header = response_header + line + "\n";
//        }
//        while (true) {
//
//            String line = in.readLine();
//            // System.out.println(line);
//            if (line == null)
//                break;
//            body = body + line + "\n";
//
//        }
//        System.out.print(response_header + "\r\n" + body);
    }

    public static RequestLineParams createHTTPObject(String ar[]) {
        RequestLineParams http = new RequestLineParams();
        for (int i = 0; i < ar.length; i++) {

            // System.out.println("arguments : " + args[i]);
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
            if (ar[i].startsWith("'http://")) {
                http.url = ar[i].replace("'", "");
                http.host = http.url.split("/")[2];
                http.urlParamaters = http.url.split("/")[3];
            }
            if (ar[i].equals("-o")) {
                http.writeTofile = true;
                http.fileName = ar[i + 1];
            }
        }
        return http;
    }

}

