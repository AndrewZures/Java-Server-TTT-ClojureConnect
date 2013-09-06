package tttmiddleware.gameresponders;

import org.andrewzures.javaserver.request.Request;
import org.andrewzures.javaserver.responders.ResponderInterface;
import org.andrewzures.javaserver.response.Response;

import java.io.*;

public class IntroResponder implements ResponderInterface {
    @Override
    public Response respond(Request request) {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("introduction.html");
        if(stream == null) {
            System.out.println("cant find file");
            return null;
        }
        String stringResult = this.convertStreamToString(stream);
        Response response = new Response();
        response.inputStream = new ByteArrayInputStream(stringResult.getBytes());
        response = updateSuccessfulResponse(response);

        return response;
    }

    public Response updateSuccessfulResponse(Response response) {
        response.method = "GET";
        response.path = "/move";
        response.statusCode = "200";
        response.statusText = "OK";
        response.httpType = "HTTP/1.1";
        response.contentType = "text/html";
        return response;
    }

    public String convertStreamToString(InputStream stream) {
        int bufferSize = 4000;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer content = new StringBuffer();
        char[] buffer = new char[bufferSize];
        int n;
        try {
            while ((n = reader.read(buffer)) != -1) {
                content.append(buffer, 0, n);
            }
            return content.toString();
        } catch (IOException ioe) {
            return null;
        }
    }
}
