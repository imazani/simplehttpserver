package org.imazani.httpserver.config;

public class HttpConfigurationException extends RuntimeException {


    public HttpConfigurationException() {
    }

    public HttpConfigurationException(String s) {
        super(s);
    }
    public HttpConfigurationException(String s, Throwable cause) {
        super(s, cause);
    }
    public HttpConfigurationException(Throwable cause) {
        super(cause);
    }

    public static HttpConfigurationException createHttpConfigurationException() {
        return new HttpConfigurationException();
    }
}
