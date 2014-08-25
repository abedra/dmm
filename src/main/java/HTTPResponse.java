public class HTTPResponse {
    private int status;
    private String statusMessage;
    private String content;
    private String response;

    public static int OK = 200;
    public static int NOT_FOUND = 404;
    public static int NOT_ALLOWED = 405;

    public HTTPResponse() { }

    public HTTPResponse(int status, String statusMessage, String content) {
        this.status = status;
        this.statusMessage = statusMessage;
        this.content = content;
        this.response = getFullResponse();
    }

    public String getFullResponse() {
        String CRLF = System.getProperty("line.separator");
        StringBuilder response = new StringBuilder();

        response.append("HTTP/1.0 ").append(status).append(" ").append(statusMessage).append(CRLF);
        response.append("Content-Type: text/html").append(CRLF);
        response.append("Server: Example").append(CRLF);
        response.append(CRLF);
        response.append(this.content);

        this.response = response.toString();
        return this.response;
    }

    public void setOK() {
        this.status = 200;
        this.statusMessage = "OK";
    }

    public void setResponse(String message) {
        this.content = message;
    }

    public String getResponse() {
        if (response == null) {
            return getFullResponse();
        } else {
            return this.response;
        }
    }

    public int getStatus() {
        return this.status;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public String getContent() {
        return this.content;
    }
}