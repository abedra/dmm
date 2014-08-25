import sun.misc.Signal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
    private static final String NOT_FOUND = "<h1>Not Found</h1>";
    private static final String UNSUPPORTED = "<h1>Unsupported Method</h1>";
    private static final String ERROR = "<h1>Error</h1>";

    protected static void start(final int port) throws IOException {
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
            Socket remote = null;

            try {
                remote = socket.accept();
                System.out.println("Received connection, sending response");
                BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));

                String input = in.readLine();
                System.out.println(input);
                HTTPRequest request = new HTTPRequest(input);
                String response = handle(request);

                while (!input.equals("")) {
                    input = in.readLine();
                    System.out.println(input);
                }

                writeResponse(remote, response);
                remote.close();
            } catch (UnsupportedException e) {
                System.out.println("Method Error: " + e.getMessage());
                writeResponse(remote, UNSUPPORTED);
            } catch (IOException e) {
                System.out.println("Socket Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Path Error: " + e.getMessage());
                writeResponse(remote, NOT_FOUND);
            } finally {
                if (remote != null) {
                    remote.close();
                }
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

    private static String handle(HTTPRequest request) {
        try {
            switch (request.getMethod()) {
                case "POST":
                    switch (request.getPath()) {
                        case "encrypt":
                            return "encrypted data";
                        case "decrypt":
                            return "decrypted data";
                        default:
                            return NOT_FOUND;
                    }
                case "GET":
                    if (request.getPath().equals("/")) {
                        return "<h1>Hello Strangeloop</h1>";
                    } else {
                        return NOT_FOUND;
                    }
                default:
                    return UNSUPPORTED;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }
}
