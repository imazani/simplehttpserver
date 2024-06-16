package org.imazani.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread{
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            // TODO we would read

            // TODO we would write
            String html = "<html><head><title>Simple Java HTTP Servers</title></head><body>This page was served using my simple HTTP Server</body></html>";

            final String CRLF = "\n\r"; //13, 10
            String response =
                    "HTTP/1.1 200 OK" + CRLF + //Status Line : HTTP VERSION RESPONSE_CODE RESPONSE MESSAGE
                            "Content-Length: " + html.getBytes().length + CRLF + // Header
                            CRLF +
                            html +
                            CRLF + CRLF;

            outputStream.write(response.getBytes());




            LOGGER.info("*Connection Processing Finished.");
        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //throw new RuntimeException(e);
                }
            }

            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    //throw new RuntimeException(e);
                }
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    //throw new RuntimeException(e);
                }
            }
        }
    }
}
