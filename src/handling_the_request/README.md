# Handling the Request

We pass our request into a `RequestHandler` class that dispatches based on the action requested.

```java
import java.util.Base64;

public class RequestHandler {
    private HTTPRequest request;
    private HTTPResponse response;

    public RequestHandler(final HTTPRequest request, final HTTPResponse response) {
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
            byte[] encryptedBytes = Crypto.encrypt(request.getPostData());
            response.setOK();
            response.setContent(Base64.getEncoder().encodeToString(encryptedBytes));
        } catch (CryptoException e) {
            response.setError();
        }
    }

    private void decrypt() {
        byte[] decodedBytes = Base64.getDecoder().decode(request.getPostData());
        try {
            byte[] decryptedBytes = Crypto.decrypt(decodedBytes);
            response.setOK();
            response.setContent(new String(decryptedBytes));
        } catch (CryptoException e) {
            response.setError();
        }
    }
}
```

The `encrypt()` and `decrypt()` methods here are the methods we will focus on. They call our
`Crypto` class which performs the actual encryption operations. We will examine these methods
shortly.
