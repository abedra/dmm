import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class LoadKeyFromFile {
    public static void main(String[] args) {
        try {
            byte[] key = Files.readAllBytes(Paths.get("../resources/keyfile"));
            System.out.println(Arrays.toString(key));
            SecureErase.zero(key);
            System.out.println(Arrays.toString(key));

            while (true) {
                // Just a noop to keep the program running so I can take a heap dump after the key is loaded
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
