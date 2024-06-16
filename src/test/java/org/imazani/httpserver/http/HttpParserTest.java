package org.imazani.httpserver.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private static HttpParser httpParser;

    @BeforeAll
    public static void beforeClass() {
        httpParser = new HttpParser();
    }


    @Test
    void parseHttpRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateValidTestCase());
        } catch (Exception e) {
            fail(e);
        }

        assertNotNull(request);
        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getRequestTarget(), "/");
        assertEquals(request.getOriginalHttpVersion(), "HTTP/1.1");
        assertEquals(request.getBestCompatibleHttpVersion(), HttpVersion.HTTP_1_1);
    }

    @Test
    void parseHttpRequestInvalidMethod() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateInvalidMethodTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_Implemented);
        }

    }

    @Test
    void parseHttpRequestInvalidMethodLength() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateInvalidMethodLengthTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_Implemented);
        }

    }
    @Test
    void parseHttpRequestInvalidNumberOfRequestLineItems() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateInvalidNumberOfItemsRequestLineTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

    }

    @Test
    void parseHttpRequestInvalidEmptyRequestLine() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateEmptyRequestLineTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

    }

    @Test
    void parseHttpRequestRequestLineMissingLF() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateInvalidTestCaseRequestLineMissingLF());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

    }

    @Test
    void parseHttpRequestInvalidHttpVersion() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateInvalidHttpVersionTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

    }

    @Test
    void parseHttpRequestInvalidUnsupportedHttpVersion() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateInvalidUnsupportedHttpVersionTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }

    }

    @Test
    void parseHttpRequestSupportedHttpVersion() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateSupportedHttpVersionTestCase());
           assertNotNull(request);
           assertEquals(request.getBestCompatibleHttpVersion(), HttpVersion.HTTP_1_1);
           assertEquals(request.getOriginalHttpVersion(), "HTTP/1.2");
        } catch (HttpParsingException e) {
            fail();
        }

    }



    private InputStream generateValidTestCase(){
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }


    private InputStream generateInvalidMethodTestCase(){
        String rawData = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateInvalidMethodLengthTestCase(){
        String rawData = "GETTTT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateInvalidNumberOfItemsRequestLineTestCase(){
        String rawData = "GET / AAAAA HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateEmptyRequestLineTestCase(){
        String rawData = "\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateInvalidTestCaseRequestLineMissingLF(){
        String rawData = "GET / HTTP/1.1\r" +    // Only CR no LF
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateInvalidHttpVersionTestCase(){
        String rawData = "GET / HTtP/1.1\r\n" +    // Only CR no LF
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateInvalidUnsupportedHttpVersionTestCase(){
        String rawData = "GET / HTTP/2.1\r\n" +    // Only CR no LF
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateSupportedHttpVersionTestCase(){
        String rawData = "GET / HTTP/1.2\r\n" +    // Only CR no LF
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }



}