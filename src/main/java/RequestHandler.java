import java.util.Base64;

public class RequestHandler {
    private HTTPRequest request;
    private HTTPResponse response;

    public RequestHandler(HTTPRequest request, HTTPResponse response) {
        this.request = request;
        this.response = response;
    }

    public void handleRequest() {
        switch (request.getMethod()) {
            case "POST":
                switch (request.getAction()) {
                    case "encrypt":
                        encrypt();
                        return;
                    case "decrypt":
                        decrypt();
                        return;
                    default:
                        notFound();
                        return;
                }
            case "GET":
                if (request.getAction().equals("/")) {
                    index();
                    return;
                } else {
                    notFound();
                    return;
                }
            default:
                notAllowed();
        }
    }

    private void notFound() {
        response.setNotFound();
    }

    private void notAllowed() {
        response.setNotAllowed();
    }

    private void index() {
        response.setOK();
        response.setContent("<h1>Hello Strangeloop</h1>");
    }

    private void encrypt() {
        try {
            byte[] encryptedBytes = Crypto.encrypt("ThisIsASecretKey".getBytes(), request.getPostData());
            response.setOK();
            response.setContent(Base64.getEncoder().encodeToString(encryptedBytes));
        } catch (CryptoException e) {
            e.printStackTrace();
        }
    }

    private void decrypt() {
        byte[] decodedBytes = Base64.getDecoder().decode(request.getPostData());
        try {
            byte[] decryptedBytes = Crypto.decrypt("ThisIsASecretKey".getBytes(), decodedBytes);
            response.setOK();
            response.setContent(new String(decryptedBytes));
        } catch (CryptoException e) {
            e.printStackTrace();
        }
    }
}
