import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public final class HTTPServer {
    private HTTPServer() { }

    private static final Logger LOGGER = LogManager.getLogger(HTTPServer.class);

    protected static void start(final int port) throws IOException {
        System.out.println("Starting server on port " + port);
        System.out.println("Press Ctrl-C to abort");

        ServerSocket socket = null;

        try {
            socket = new ServerSocket(port);
        } catch (Exception e) {
            LOGGER.error("Error opening socket: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Waiting for connections...");

        while (true) {
            Socket remote = socket.accept();
            HTTPResponse response = readRequest(remote);
            writeResponse(remote, response);
            remote.close();
        }
    }

    private static HTTPResponse readRequest(final Socket remote) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
            String input = in.readLine();
            HTTPRequest request = new HTTPRequest(input);
            HTTPResponse response = new HTTPResponse();
            RequestHandler handler = new RequestHandler(request, response);

            String[] parts;
            while (!input.equals("")) {
                input = in.readLine();
                parts = input.split(": ");
                if (parts[0].equals("Content-Length")) {
                    request.setContentLength(Long.parseLong(parts[1]));
                }
            }

            if (request.getMethod().equals("POST") && request.getContentLength() > 0) {
                byte[] postData = new byte[(int) request.getContentLength()];
                for (int i = 0; i < request.getContentLength(); i++) {
                    postData[i] = (byte) in.read();
                }
                request.setPostData(postData);
            }

            handler.handleRequest();

            LOGGER.info(request.getMethod() + " " + request.getPath() + " " + response.getStatus());

            return response;
        } catch (IOException e) {
            LOGGER.error("Socket Error: " + e.getMessage());
            return null;
        }
    }

    private static void writeResponse(final Socket socket, final HTTPResponse response) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.print(response.getResponse());
            out.flush();
        } catch (IOException e) {
            LOGGER.error("Error: " + e.getMessage());
        }
    }
}
