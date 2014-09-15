# Encrypt/Decrypt

For our encrypt and decrypt examples we will be using `AES-128-CBC`, or 128 bit AES encryption using the [Cipher Block Chaining](http://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher-block_chaining_.28CBC.29) mode of operation. This mode requires us to provide an initialization vector unique to each message that will be distributed along with the message. The encryption key is stored in a Java [KeyStore](http://docs.oracle.com/javase/8/docs/api/java/security/KeyStore.html). The key will be loaded during the encrypt/decrypt process. The key will not be loaded with the intent of keeping it in memory for the duration of program execution. Let's take a look at our `Crypto` class:

```java
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
```

First, we encounter our `encrypt()` method. This method takes a `byte[]` containing the message we wish to encrypt. It loads the encryption key, generates the initialization vector, and encrypts the message. Finally, it packs the initialization vector and the encrypted message together and returns it to the caller. Let's take a look at the supporting methods. You will notice that all of the methods in the `Crypto` class are static. This is an attempt to avoid instantiating an object that could possibly hold on to data longer than it needs to. It is also a generally accepted practice for data that is purely derived.

```java
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

```

We have seen the `loadKey()` method previously. We used it to dump our encryption key so we could search for it on the heap.

```java
    private static byte[] generateIv() {
        byte[] iv = new byte[IVLENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    private static byte[] pack(final byte[] iv, final byte[] encryptedBytes) {
        byte[] finalBytes = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, finalBytes, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, finalBytes, iv.length, encryptedBytes.length);
        return finalBytes;
    }
```

Next we have our `generateIv()` and `pack()` methods. They are pretty self explanatory. The only thing to note here is that we are using Java's `SecureRandom` class to produce the data for our initialization vector. Now we can look at our `decrypt()` method.

```java
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
```

As we expect `decrypt()` is a lot like `encrypt()`. It loads the key, extracts the initialization vector and the message, and performs the decrypt operation. the following support methods are used to unpack the message into the proper parts.

```java
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
}
```

There are a few things we are missing in this code. The most noteworthy is that the encryption is not being verified. Normally there would be an additional step where we use `HMAC` to sign the encrypted payload. This ensures that the message was not tampered with. It was intentionally left out of the example because it provides no further demonstration of the problem. Normally you would also see a `Base64` encode/decode operation along with the encryption. That is being done in our `RequestHandler` object. There are many other ciphers and block modes of operation. Some are arguably better at this task. This example code aims to provide a sample that is still considered
sound and valid for use while demonstrating the problem.
