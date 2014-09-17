import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

public final class LoadKeyFromStore {
    private LoadKeyFromStore() { }

    public static void main(final String[] args) {
        try {
            String location = "../resources/aes-keystore.jck";
            String alias = "slaes";
            String password = "strangeloop";
            InputStream inputStream = new FileInputStream(location);
            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            keyStore.load(inputStream, password.toCharArray());
            inputStream.close();

            byte[] key = keyStore.getKey(alias, password.toCharArray()).getEncoded();
            SecureErase.zero(key);
            System.out.println(Arrays.toString(key));
        } catch (KeyStoreException | CertificateException |
                NoSuchAlgorithmException | IOException |
                UnrecoverableKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load key");
        }

        while (true) {
            // Just a noop to keep the program running so I can take a heap dump after the key is loaded
        }
    }
}

