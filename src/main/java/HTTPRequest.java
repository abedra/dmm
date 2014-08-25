import java.util.Arrays;

public class HTTPRequest {
    private String[] supportedMethods = {"GET", "POST"};
    private String method;
    private String path;
    private String action;
    private String rawRequest;

    public HTTPRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.path = getPath();
        this.action = getAction();
        this.method = getMethod();
    }

    public String getMethod() {
        if (this.method == null) {
            String meth = rawRequest.split(" ")[0];
            if (Arrays.asList(supportedMethods).contains(meth)) {
                this.method = meth;
                return this.method;
            } else {
                this.method = "";
                return this.method;
            }
        } else {
            return this.method;
        }
    }

    public String getPath() {
        if (this.path == null) {
            this.path = this.rawRequest.split(" ")[1];
            return this.path;
        } else {
            return this.path;
        }
    }

    public String getAction() {
        String path = getPath();

        if (path.equals("/")) {
            this.action = path;
            return this.action;
        }

        String[] parts = path.split("/");

        this.action = parts[parts.length - 1];
        return this.action;
    }
}
