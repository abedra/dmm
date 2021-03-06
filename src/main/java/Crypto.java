import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Properties;

public final class Crypto {
    private Crypto() { }

    private static final int IVLENGTH = 16;

    public static byte[] encrypt(final byte[] message) throws CryptoException {
        byte[] k = loadKey().getEncoded();
        SecretKey secretKey = new SecretKeySpec(k, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = generateIv();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] encryptedBytes = cipher.doFinal(message);
            return pack(iv, encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidAlgorithmParameterException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new CryptoException(e.getMessage());
        }
    }

    public static byte[] decrypt(final byte[] encryptedBytes) throws CryptoException {
        byte[] k = loadKey().getEncoded();
        SecretKey secretKey = new SecretKeySpec(k, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(getIv(encryptedBytes)));
            return cipher.doFinal(getMessage(encryptedBytes));
        } catch (NoSuchPaddingException | InvalidAlgorithmParameterException |
                 NoSuchAlgorithmException | IllegalBlockSizeException |
                 BadPaddingException | InvalidKeyException e) {
            throw new CryptoException(e.getMessage());
        }
    }

    private static byte[] pack(final byte[] iv, final byte[] encryptedBytes) {
        byte[] finalBytes = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, finalBytes, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, finalBytes, iv.length, encryptedBytes.length);
        return finalBytes;
    }

    private static byte[] generateIv() {
        byte[] iv = new byte[IVLENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    private static byte[] getIv(final byte[] encryptedBytes) {
        byte[] iv = new byte[IVLENGTH];
        System.arraycopy(encryptedBytes, 0, iv, 0, IVLENGTH);
        return iv;
    }

    private static byte[] getMessage(final byte[] encryptedBytes) {
        int length = (encryptedBytes.length - IVLENGTH);
        byte[] messageBytes = new byte[length];
        System.arraycopy(encryptedBytes, IVLENGTH, messageBytes, 0, length);
        return messageBytes;
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

            if (!keyStore.containsAlias(alias)) {
                throw new RuntimeException("Key alias not found");
            }

            return keyStore.getKey(alias, password.toCharArray());
        } catch (KeyStoreException | CertificateException |
                 NoSuchAlgorithmException | IOException |
                 UnrecoverableKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load key");
        }
    }
}
