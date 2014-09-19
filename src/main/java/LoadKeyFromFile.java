import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class LoadKeyFromFile {
    public static void main(String[] args) {
        try {
            ByteBuffer key = ByteBuffer.allocateDirect(16);
            key = ByteBuffer.wrap(Files.readAllBytes(Paths.get("src/main/resources/keyfile")));
            for (int i = 0; i < 16; i++) {
                key.put((byte)0);
                key.position(i++);
            }
            while (true) {
                // Just a noop to keep the program running so I can take a heap dump after the key is loaded
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
