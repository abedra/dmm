import java.util.Arrays;

public class HTTPRequest {
    private String[] supportedMethods = {"GET", "POST"};
    private String method;
    private String path;
    private String rawRequest;

    public HTTPRequest(String rawRequest) throws Exception {
        this.rawRequest = rawRequest;
        this.path = getPath();
        this.method = getMethod();
    }

    public String getMethod() throws UnsupportedException {
        if (this.method == null) {
            String meth = rawRequest.split(" ")[0];
            if (Arrays.asList(supportedMethods).contains(meth)) {
                this.method = meth;
                return this.method;
            } else {
                throw new UnsupportedException("Method not supported");
            }
        } else {
            return this.method;
        }
    }

    public String getPath() throws BadPathException {
        if (this.path == null) {
            String rawPath = this.rawRequest.split(" ")[1];

            if (rawPath.equals("/")) {
                this.path = rawPath;
                return this.path;
            }

            String path = rawPath.split("/")[1];
            if (path != null) {
                this.path = path;
                return this.path;
            } else {
                throw new BadPathException("Bad Request");
            }
        } else {
            return this.path;
        }
    }
}
