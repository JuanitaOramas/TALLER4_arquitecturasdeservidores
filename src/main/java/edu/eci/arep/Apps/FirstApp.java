package edu.eci.arep.Apps;

import edu.eci.arep.Services.CssService;
import edu.eci.arep.Services.HTMLService;
import edu.eci.arep.Services.ImgService;
import edu.eci.arep.Services.JavaScriptService;

import java.io.IOException;

public class FirstApp {
    public static void main(String args[]) throws IOException {
        HttpServer server = HttpServer.getInstance();

        //Agregar servicios
        server.addService("/html", new HTMLService());
        server.addService("/style", new CssService());
        server.addService("/img", new ImgService());
        server.addService("/js", new JavaScriptService());
        server.run(args);
    }


}
