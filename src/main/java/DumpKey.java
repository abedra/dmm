import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Properties;

public final class DumpKey {
    private DumpKey() { }

    public static void main(final String[] args) {
        Key key = loadKey();
        System.out.println(Arrays.toString(key.getEncoded()));
    }

    private static Key loadKey() {
        try {
            Properties properties = new Properties();
            InputStream in = Crypto.class.getResourceAsStream("server.properties");
            properties.load(in);
            in.close();
            String location = properties.getProperty("keystore.location");
            String alias = properties.getProperty("keystore.alias");
            String password = properties.getProperty("keystore.password");
            InputStream inputStream = new FileInputStream(location);
            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            keyStore.load(inputStream, password.toCharArray());
            inputStream.close();

            return keyStore.getKey(alias, password.toCharArray());
        } catch (KeyStoreException | CertificateException |
                NoSuchAlgorithmException | IOException |
                UnrecoverableKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load key");
        }
    }
}
