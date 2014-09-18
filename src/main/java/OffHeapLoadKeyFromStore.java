import tr.com.serkanozal.jillegal.config.annotation.JillegalAware;
import tr.com.serkanozal.jillegal.offheap.config.provider.annotation.OffHeapArray;
import tr.com.serkanozal.jillegal.offheap.config.provider.annotation.OffHeapObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;

@JillegalAware
public class OffHeapLoadKeyFromStore {
    @OffHeapObject
    private static KeyStore keyStore;

    @OffHeapArray(length = 16)
    private static byte[] key;

    public static void main(String[] args) {
        tr.com.serkanozal.jillegal.Jillegal.init();
        try {
            String location = "src/main/resources/aes-keystore.jck";
            String alias = "slaes";
            String password = "strangeloop";
            InputStream inputStream = new FileInputStream(location);
            keyStore = KeyStore.getInstance("JCEKS");
            keyStore.load(inputStream, password.toCharArray());
            inputStream.close();

            key = keyStore.getKey(alias, password.toCharArray()).getEncoded();
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
