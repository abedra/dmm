public class SecureErase {
    static {
        System.loadLibrary("SecureErase");
    }


    public static native int zero(final byte[] arr);
}
