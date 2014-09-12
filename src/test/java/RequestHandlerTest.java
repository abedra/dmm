import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@PrepareForTest(Crypto.class)
@RunWith(PowerMockRunner.class)

public class RequestHandlerTest {
    byte[] textBytes = "testing".getBytes();
    String encodedText = Base64.getEncoder().encodeToString("testing".getBytes());
    HTTPResponse response;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Crypto.class);
        response = new HTTPResponse();
    }

    @Test
    public void notFoundPostRequest() {
        HTTPRequest request = new HTTPRequest("POST /notfound HTTP/1.1");
        RequestHandler handler = new RequestHandler(request, response);
        handler.handleRequest();

        assertEquals(HTTPResponse.NOT_FOUND, response.getStatus());
        assertEquals("Not Found", response.getStatusMessage());
        assertEquals("<h1>Not Found</h1>", response.getContent());
    }

    @Test
    public void encryptPostRequest() throws CryptoException {
        when(Crypto.encrypt(textBytes)).thenReturn(textBytes);
        HTTPRequest request = new HTTPRequest("POST /encrypt HTTP/1.1");
        request.setPostData(textBytes);
        RequestHandler handler = new RequestHandler(request, response);
        handler.handleRequest();

        assertEquals(HTTPResponse.OK, response.getStatus());
        assertEquals("OK", response.getStatusMessage());
        assertEquals(encodedText, response.getContent());
    }

    @Test
    public void decryptPostRequest() throws CryptoException {
        when(Crypto.decrypt(textBytes)).thenReturn(textBytes);
        HTTPRequest request = new HTTPRequest("POST /decrypt HTTP/1.1");
        RequestHandler handler = new RequestHandler(request, response);
        request.setPostData(encodedText.getBytes());
        handler.handleRequest();

        assertEquals(HTTPResponse.OK, response.getStatus());
        assertEquals("OK", response.getStatusMessage());
        assertEquals("testing", response.getContent());
    }

    @Test
    public void notFoundGetRequest() {
        HTTPRequest request = new HTTPRequest("GET /notfound HTTP/1.1");
        RequestHandler handler = new RequestHandler(request, response);
        handler.handleRequest();

        assertEquals(HTTPResponse.NOT_FOUND, response.getStatus());
        assertEquals("Not Found", response.getStatusMessage());
        assertEquals("<h1>Not Found</h1>", response.getContent());
    }

    @Test
    public void indexGetRequest() {
        HTTPRequest request = new HTTPRequest("GET / HTTP/1.1");
        RequestHandler handler = new RequestHandler(request, response);
        handler.handleRequest();

        assertEquals(HTTPResponse.OK, response.getStatus());
        assertEquals("OK", response.getStatusMessage());
        assertEquals("<h1>Hello Strangeloop</h1>", response.getContent());
    }

    @Test
    public void unsupportedRequest() {
        HTTPRequest request = new HTTPRequest("HEAD / HTTP/1.1");
        RequestHandler handler = new RequestHandler(request, response);
        handler.handleRequest();

        assertEquals(HTTPResponse.NOT_ALLOWED, response.getStatus());
        assertEquals("Method Not Allowed", response.getStatusMessage());
        assertEquals("<h1>Method Not Allowed</h1>", response.getContent());
    }
}
