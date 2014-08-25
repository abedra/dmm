import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            HTTPServer.start(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
