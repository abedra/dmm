public class RequestHandler {
    private HTTPRequest request;

    public RequestHandler(HTTPRequest request) {
        this.request = request;
    }

    public HTTPResponse handleRequest() {
        switch (request.getMethod()) {
            case "POST":
                switch (request.getAction()) {
                    case "encrypt":
                        return encrypt();
                    case "decrypt":
                        return decrypt();
                    default:
                        return notFound();
                }
            case "GET":
                if (request.getAction().equals("/")) {
                    return index();
                } else {
                    return notFound();
                }
            default:
                return notAllowed();

        }
    }

    private HTTPResponse notFound() {
        return new HTTPResponse(404, "Not Found", "<h1>Not Found</h1>");
    }

    private HTTPResponse notAllowed() {
        return new HTTPResponse(405, "Method Not Allowed", "<h1>Method Not Allowed</h1>");
    }

    private HTTPResponse index() {
        return new HTTPResponse(200, "OK", "<h1>Hello Strangeloop</h1>");
    }

    private HTTPResponse encrypt() {
        HTTPResponse response = new HTTPResponse();

        // TODO: do some encryption

        response.setOK();
        response.setResponse("encrypted data");

        return response;
    }

    private HTTPResponse decrypt() {
        HTTPResponse response = new HTTPResponse();

        // TODO: do some decryption

        response.setOK();
        response.setResponse("decrypted data");

        return response;
    }
}
