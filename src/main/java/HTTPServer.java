import sun.misc.Signal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {

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
                while (!input.equals("")) {
                    input = in.readLine();
                }
                writeResponse(remote);
                remote.close();
            } catch (IOException e) {
                System.out.println("Error :" + e.getMessage());
            }
        }
    }

    private static void writeResponse(final Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            out.println("HTTP/1.0 200 OK");
            out.println("Content-Type: text/html");
            out.println("Server: Example");
            out.println("");

            out.println("<h1>Hello Strangeloop</h1>");
            out.flush();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
