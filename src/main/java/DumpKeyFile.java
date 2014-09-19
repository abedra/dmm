import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class DumpKeyFile {
    public static void main(String[] args) {
        try {
            byte[] key = Files.readAllBytes(Paths.get("src/main/resources/keyfile"));
            System.out.println(Arrays.toString(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
