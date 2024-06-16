package org.imazani.httpserver.http;

import org.imazani.httpserver.config.HttpConfigurationException;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion; // version literal string
    private HttpVersion bestCompatibleHttpVersion;

    HttpRequest(){
    }

    public HttpMethod getMethod(){
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public HttpVersion getBestCompatibleHttpVersion() {
        return bestCompatibleHttpVersion;
    }

    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }


    void setMethod(String methodName) throws HttpParsingException {
        for (HttpMethod method : HttpMethod.values()) {
            if(methodName.equals(method.name())) {
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException( HttpStatusCode.SERVER_ERROR_501_NOT_Implemented);
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if(requestTarget == null || requestTarget.length()==0){
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }

    void setHttpVersion(String literalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = literalHttpVersion;
        this.bestCompatibleHttpVersion = HttpVersion.getBestCompatibleVersion(literalHttpVersion);

        if (this.bestCompatibleHttpVersion == null) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }
}
