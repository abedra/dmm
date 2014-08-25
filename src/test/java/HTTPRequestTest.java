import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTTPRequestTest {
    @Test
    public void getMethodGETRequest() {
        HTTPRequest request = new HTTPRequest("GET / HTTP/1.1");
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void getMethodPOSTRequest() {
        HTTPRequest request = new HTTPRequest("POST /encrypt HTTP/1.1");
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void getMethodPUTRequest() {
        HTTPRequest request = new HTTPRequest("PUT / HTTP/1.1");
        assertEquals("", request.getMethod());
    }

    @Test
    public void getMethodDELETERequest() {
        HTTPRequest request = new HTTPRequest("DELETE / HTTP/1.1");
        assertEquals("", request.getMethod());
    }

    @Test
    public void getMethodHEADRequest() {
        HTTPRequest request = new HTTPRequest("HEAD / HTTP/1.1");
        assertEquals("", request.getMethod());
    }

    @Test
    public void getPathSimple() {
        HTTPRequest request = new HTTPRequest("GET / HTTP/1.1");
        assertEquals("/", request.getPath());
    }

    @Test
    public void getPathSingle() {
        HTTPRequest request = new HTTPRequest("POST /encrypt HTTP/1.1");
        assertEquals("/encrypt", request.getPath());
    }

    @Test
    public void getPathMultiple() {
        HTTPRequest request = new HTTPRequest("POST /users/foo/encrypt HTTP/1.1");
        assertEquals("/users/foo/encrypt", request.getPath());
    }

    @Test
    public void getActionSimple() {
        HTTPRequest request = new HTTPRequest("GET / HTTP/1.1");
        assertEquals("/", request.getAction());
    }

    @Test
    public void getActionSingle() {
        HTTPRequest request = new HTTPRequest("POST /encrypt HTTP/1.1");
        assertEquals("encrypt", request.getAction());
    }

    @Test
    public void getActionMultiple() {
        HTTPRequest request = new HTTPRequest("POST /users/foo/encrypt HTTP/1.1");
        assertEquals("encrypt", request.getAction());
    }
}
