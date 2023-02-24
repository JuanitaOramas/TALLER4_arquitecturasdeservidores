package edu.eci.arep.Apps;


import java.io.IOException;

public class FirstApp {
    public static void main(String args[]) throws IOException {
        HttpServer server = HttpServer.getInstance();
        server.run(args);
    }


}
