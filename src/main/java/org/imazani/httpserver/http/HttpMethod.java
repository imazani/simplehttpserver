package org.imazani.httpserver.http;

public enum HttpMethod {
    GET,
    HEAD,
    // TODO Implement the following
    POST,
    PUT,
    DELETE;

    public static final int MAX_LENGTH;

    static {
        int tempMaxLength = -1;
        for(HttpMethod method : values()) {
            if(method.name().length() > tempMaxLength) {
                tempMaxLength = method.name().length();
            }
        }
        MAX_LENGTH = tempMaxLength;
    }
}
