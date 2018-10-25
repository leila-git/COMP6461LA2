package com.COMP6461.server;

import java.util.Scanner;

public class HTTPServer {

    public static void main(String[] args) {
        boolean executeCommand = true;
        System.out.println("Please enter command.");
        Scanner scanner = new Scanner(System.in);
        String arguments[] = scanner.nextLine().split(" ");
        for (String s : arguments) {
            if (s.toLowerCase().equals("exit")) {
                executeCommand = false;
            }
        }
        if (executeCommand) {
            ServerStartingParams httpObject = createHTTPObject(arguments);
            System.out.println(httpObject);
            SocketConnectionFromServer server=new SocketConnectionFromServer(httpObject);
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

    public static String combineData(String args[]) {
        String results = "";
        int startIndex = 0, endIndex = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("'")) {
                startIndex = i;
            }
            if (args[i].endsWith("'")) {
                endIndex = i;
            }

        }
        for (int j = startIndex; j <= endIndex; j++) {
            results += args[j];
        }

        return results;
    }

    public static void helpMethod(String str[]) {
        if (str.length == 2 & str[0].equals("httpfs") & str[1].equals("help")) {
            System.out.println("httpc is a curl-like application but supports HTTP protocol only.\n" + "Usage: \n"
                    + "   httpc command [arguments]\n " + "The commands are:\n"
                    + "   get executes a HTTP GET request and prints the response.\n"
                    + "   post executes a HTTP POST request and prints the response. \n"
                    + "   help prints this screen. \n"
                    + "Use \"httpc help [command]\" for more information about a command.\n");

        } else if (str.length == 3 & str[0].equals("httpc") & str[1].equals("help") & str[2].equals("get")) {
            System.out.println(
                    "usage: httpc get [-v] [-h key:value] URL\n" + "Get executes a HTTP GET request for a given URL.\n"
                            + "   -v            Prints the detail of the response such as protocol, status,\n"
                            + "and headers.\n" + "   -h key:value  Associates headers to HTTP Request with the format\n"
                            + "'key:value'.");

        } else if (str.length == 3 & str[0].equals("httpc") & str[1].equals("help") & str[2].equals("post")) {
            System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\n"
                    + "Post executes a HTTP POST request for a given URL with inline data or from\n" + "file.\n"
                    + "   -v             Prints the detail of the response such as protocol, status,\n"
                    + "and headers.\n" + "   -h key:value   Associates headers to HTTP Request with the format\n"
                    + "'key:value'.\n" + "   -d string      Associates an inline data to the body HTTP POST request.\n"
                    + "   -f file        Associates the content of a file to the body HTTP POST\n" + "request.\n"
                    + "Either [-d] or [-f] can be used but not both.");
        }
    }
}
