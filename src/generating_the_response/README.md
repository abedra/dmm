# Generating the Response


Before we look at our encryption code let's take a look at our response object, `HTTPResponse`.

```java
public class HTTPResponse {
    private int status;
    private String statusMessage;
    private String content;
    private String response;

    public static final int OK = 200;
    public static final int NOT_FOUND = 404;
    public static final int NOT_ALLOWED = 405;
    public static final int ERROR = 500;

    public HTTPResponse() { }

    public void setOK() {
        this.status = OK;
        this.statusMessage = "OK";
    }

    public void setNotFound() {
        this.status = NOT_FOUND;
        this.statusMessage = "Not Found";
        this.content = "<h1>Not Found</h1>";
    }

    public void setNotAllowed() {
        this.status = NOT_ALLOWED;
        this.statusMessage = "Method Not Allowed";
        this.content = "<h1>Method Not Allowed</h1>";
    }

    public void setError() {
        this.status = ERROR;
        this.statusMessage = "Internal Server Error";
        this.content = "<h1>Internal Server Error</h1>";
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

    public void setContent(final String content) {
        this.content = content;
    }

    private String getFullResponse() {
        String crlf = System.getProperty("line.separator");
        StringBuilder response = new StringBuilder();

        response.append("HTTP/1.0 ").append(status).append(" ").append(statusMessage).append(crlf);
        response.append("Content-Type: text/html").append(crlf);
        response.append("Server: Example").append(crlf);
        response.append(crlf);
        response.append(this.content);

        this.response = response.toString();
        return this.response;
    }
}
```

The response prepared in this object is what gets rendered back to the requestor.
