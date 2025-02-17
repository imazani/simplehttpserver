package org.imazani.httpserver.http;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0X20; // 32
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();

        try {
            parseRequestLine(reader, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseHeaders(reader, request);
        parseBody(reader, request);

        return request;
    }

    private void parseRequestLine(InputStreamReader inputStreamReader, HttpRequest request) throws IOException, HttpParsingException {

        StringBuilder processingDataBuffer = new StringBuilder();

        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        int _byte;
        while ((_byte = inputStreamReader.read()) >= 0) {
            if (_byte == CR) {
               _byte = inputStreamReader.read();
               if (_byte == LF) {

                   LOGGER.debug("Request Line VERSION to Process : {}", processingDataBuffer.toString());

                   if(!methodParsed || !requestTargetParsed) {
                       throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                   }

                   try {
                       request.setHttpVersion(processingDataBuffer.toString());
                   } catch (BadHttpVersionException e) {
                       throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                   }

                   return;
               } else {
                   throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
               }
            }

            if(_byte == SP) {
                // TODO Process previous data
                if(!methodParsed) {
                    LOGGER.debug("Request Line METHOD to Process : {}", processingDataBuffer.toString());
                    request.setMethod(processingDataBuffer.toString());
                    methodParsed = true;
                } else if (!requestTargetParsed) {
                    LOGGER.debug("Request Line REQUEST TARGET to Process : {}", processingDataBuffer.toString());
                    request.setRequestTarget(processingDataBuffer.toString());
                    requestTargetParsed = true;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }

                //LOGGER.debug("Request Line to Process : {}", processingDataBuffer.toString());
                processingDataBuffer.delete(0, processingDataBuffer.length());
            } else {
                processingDataBuffer.append((char) _byte);

                if(!methodParsed) {
                    if(processingDataBuffer.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_Implemented);
                    }
                }
            }
        }

    }
    private void parseHeaders(InputStreamReader inputStreamReader, HttpRequest request) {

    }

    private void parseBody(InputStreamReader inputStreamReader, HttpRequest request) {
    }


}
