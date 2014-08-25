import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RequestHandlerTest {
    @Test
    public void notFoundPostRequest() {
        HTTPRequest request = new HTTPRequest("POST /notfound HTTP/1.1");
        RequestHandler handler = new RequestHandler(request);
        HTTPResponse response = handler.handleRequest();

        assertEquals(HTTPResponse.NOT_FOUND, response.getStatus());
        assertEquals("Not Found", response.getStatusMessage());
        assertEquals("<h1>Not Found</h1>", response.getContent());
    }

    @Test
    public void encryptPostRequest() {
        HTTPRequest request = new HTTPRequest("POST /encrypt HTTP/1.1");
        RequestHandler handler = new RequestHandler(request);
        HTTPResponse response = handler.handleRequest();

        assertEquals(HTTPResponse.OK, response.getStatus());
        assertEquals("OK", response.getStatusMessage());
        assertEquals("encrypted data", response.getContent());
    }

    @Test
    public void decryptPostRequest() {
        HTTPRequest request = new HTTPRequest("POST /decrypt HTTP/1.1");
        RequestHandler handler = new RequestHandler(request);
        HTTPResponse response = handler.handleRequest();

        assertEquals(HTTPResponse.OK, response.getStatus());
        assertEquals("OK", response.getStatusMessage());
        assertEquals("decrypted data", response.getContent());
    }

    @Test
    public void notFoundGetRequest() {
        HTTPRequest request = new HTTPRequest("GET /notfound HTTP/1.1");
        RequestHandler handler = new RequestHandler(request);
        HTTPResponse response = handler.handleRequest();

        assertEquals(HTTPResponse.NOT_FOUND, response.getStatus());
        assertEquals("Not Found", response.getStatusMessage());
        assertEquals("<h1>Not Found</h1>", response.getContent());
    }

    @Test
    public void indexGetRequest() {
        HTTPRequest request = new HTTPRequest("GET / HTTP/1.1");
        RequestHandler handler = new RequestHandler(request);
        HTTPResponse response = handler.handleRequest();

        assertEquals(HTTPResponse.OK, response.getStatus());
        assertEquals("OK", response.getStatusMessage());
        assertEquals("<h1>Hello Strangeloop</h1>", response.getContent());
    }

    @Test
    public void unsupportedRequest() {
        HTTPRequest request = new HTTPRequest("HEAD / HTTP/1.1");
        RequestHandler handler = new RequestHandler(request);
        HTTPResponse response = handler.handleRequest();

        assertEquals(HTTPResponse.NOT_ALLOWED, response.getStatus());
        assertEquals("Method Not Allowed", response.getStatusMessage());
        assertEquals("<h1>Method Not Allowed</h1>", response.getContent());
    }
}
