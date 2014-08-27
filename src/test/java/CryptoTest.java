import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class CryptoTest {
    private byte[] message;

    @Before
    public void setUp() {
        message = "You shouldn't read this".getBytes();
    }

    @Test
    public void encryptLengthTest() throws CryptoException {
        byte[] encryptedBytes = Crypto.encrypt(message);
        assertEquals(48, encryptedBytes.length);
    }

    @Test
    public void encryptDecryptTest() throws CryptoException {
        byte[] encryptedBytes = Crypto.encrypt(message);
        byte[] decryptedBytes = Crypto.decrypt(encryptedBytes);

        assertArrayEquals(message, decryptedBytes);
    }
}
