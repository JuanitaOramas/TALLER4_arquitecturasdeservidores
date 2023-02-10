package edu.eci.arep.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CssService implements RESTService {
    @Override
    public String getResponse(){

        byte[] fileContent;
        try {
            fileContent = Files.readAllBytes(Paths.get("src/main/resources/style.css"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(fileContent);
    };

    @Override
    public String getHeader() {

        return "HTTP/1.1 200 OK \r\n" + "Content-Type: text/css \r\n" + "\r\n";
    }
}
