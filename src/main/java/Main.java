import java.io.IOException;

public final class Main {
    private Main() { }

    private static final int PORT = 8080;

    public static void main(final String[] args) {
        try {
            HTTPServer.start(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
