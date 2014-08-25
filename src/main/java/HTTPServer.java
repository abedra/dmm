import sun.misc.Signal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class HTTPServer {
    private final static String[] supportedMethods = {"GET", "POST"};

    protected static void start(final int port) {
        System.out.println("Starting server on port " + port);
        System.out.println("Press Ctrl-C to abort");

        Signal.handle(new Signal("INT"), (signal) -> {
            System.out.println("\nReceived INT, shutting down");
            System.exit(0);
        });

        ServerSocket socket = null;

        try {
            socket = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("Error opening socket: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Waiting for connections...");

        while (true) {
            try {
                Socket remote = socket.accept();
                System.out.println("Received connection, sending response");
                BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));

                String input = in.readLine();
                String response = handle(remote, getURI(input));

                while (!input.equals("")) {
                    input = in.readLine();
                    System.out.println(input);
                }

                writeResponse(remote, response);
                remote.close();
            } catch (IOException e) {
                System.out.println("Error :" + e.getMessage());
            }
        }
    }

    private static void writeResponse(final Socket socket, final String response) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            out.println("HTTP/1.0 200 OK");
            out.println("Content-Type: text/html");
            out.println("Server: Example");
            out.println("");

            out.println(response);
            out.flush();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getURI(final String queryString) {
        String[] params = queryString.split(" ");

        if (Arrays.asList(supportedMethods).contains(params[0])) {
            return params[1];
        }

        return "";
    }

    private static String handle(final Socket socket, final String uri) {
        String[] parts = uri.split("/");

        if (parts.length > 1) {
            switch (uri.split("/")[1]) {
                case "encrypt":
                    return "encrypted output";
                case "decrypt":
                    return "decrypted output";
                default:
                    return "<h1>Not Found</h1>";
            }
        } else {
            return "<h1>Hello Strangeloop</h1>";
        }
    }
}
