import sun.misc.Signal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
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
                RequestHandler handler = new RequestHandler(request);
                HTTPResponse response = handler.handleRequest();

                while (!input.equals("")) {
                    input = in.readLine();
                    System.out.println(input);
                }

                writeResponse(remote, response);
                remote.close();
            } catch (IOException e) {
                System.out.println("Socket Error: " + e.getMessage());
            } finally {
                if (remote != null) {
                    remote.close();
                }
            }
        }
    }

    private static void writeResponse(final Socket socket, HTTPResponse response) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.print(response.getResponse());
            out.flush();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
