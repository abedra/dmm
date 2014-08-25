import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Crypto {
    public static byte[] encrypt(byte[] key, byte[] message) throws CryptoException {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        try{
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

    public static byte[] decrypt(byte[] key, byte[] encryptedBytes) throws CryptoException {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
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

    public static byte[] pack(byte[] iv, byte[] encryptedBytes) {
        byte[] finalBytes = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, finalBytes, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, finalBytes, iv.length, encryptedBytes.length);
        return finalBytes;
    }

    private static byte[] generateIv() {
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    private static byte[] getIv(byte[] encryptedBytes) {
        byte[] iv = new byte[16];
        System.arraycopy(encryptedBytes, 0,iv, 0, 16);
        return iv;
    }

    private static byte[] getMessage(byte[] encryptedBytes) {
        int length = (encryptedBytes.length - 16);
        byte[] messageBytes = new byte[length];
        System.arraycopy(encryptedBytes, 16, messageBytes, 0 ,length);
        return messageBytes;
    }
}
