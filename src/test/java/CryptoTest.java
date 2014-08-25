import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class CryptoTest {
    private byte[] key;
    private byte[] message;

    @Before
    public void setUp() {
        key = "ThisIsASecretKey".getBytes();
        message = "You shouldn't read this".getBytes();
    }

    @Test
    public void encryptLengthTest() throws CryptoException {
        byte[] encryptedBytes = Crypto.encrypt(key, message);
        assertEquals(48, encryptedBytes.length);
    }

    @Test
    public void encryptDecryptTest() throws CryptoException {
        byte[] encryptedBytes = Crypto.encrypt(key, message);
        byte[] decryptedBytes = Crypto.decrypt(key, encryptedBytes);

        assertArrayEquals(message, decryptedBytes);
    }
}
