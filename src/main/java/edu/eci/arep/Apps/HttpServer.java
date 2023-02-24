package edu.eci.arep.Apps;

import edu.eci.arep.Component;

import edu.eci.arep.RequestMapping;
import edu.eci.arep.Services.RESTService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {




    /**
     * Método main, inicia un socket recibe la petición get y agrega el nombre t de la película a la URL de la API
     * @param args
     * @throws IOException
     */

    private static HttpServer _instance = new HttpServer();
    //private static Map<String, RESTService> services = new HashMap();
    private HttpServer() {};
    static HttpServer getInstance(){return _instance;}





    private Map<String, Method> ServiceHash = new HashMap<>();

    //
    public  void run(String[] args) throws IOException {
        boolean running = true;
        ArrayList<String> Anotations = myAnnotation();


        for (String className : Anotations) {
            Class<?> c;
            try {
                c = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Method[] myMethods = c.getMethods();

            for (Method m : myMethods) {
                if (m.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping annotation = m.getAnnotation(RequestMapping.class);
                    String file = annotation.value();
                    ServiceHash.put(file,m);
                }
            }

        }


        ServerSocket serverSocket = null;

        String namepath = " ";



        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        while (running){
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean firstLine = true;
            while ((inputLine = in.readLine()) != null) {
                if (firstLine){
                    firstLine = false;
                    namepath = inputLine.split(" ")[1];
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {break;}
            }
            outputLine ="";
            if (ServiceHash.containsKey(namepath)) {
                Method method = ServiceHash.get(namepath);
                try {
                    outputLine = htmlHeader()  + method.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



            out.println(outputLine);

            out.close();
            in.close();
            clientSocket.close();


        }
        serverSocket.close();

    }



    //
    public void addService(String key, RESTService service){
        //services.put(key, service);
    }


    //se encarga de buscar archivos Java con anotaciones de tipo Component
    private ArrayList<String> myAnnotation() {
        Path path = Paths.get("src/main/java/edu/eci/arep");
        DirectoryStream<Path> paths = null;
        ArrayList<String> filesList = new ArrayList<>();

        try {
            paths = Files.newDirectoryStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Path file: paths) {
            if(Files.isRegularFile(file) && file.toString().contains(".java")) {

                String fileName = file.toString().split("\\.")[0].replace("\\", ".").substring(14);
                try {
                    if(Class.forName(fileName).isAnnotationPresent(Component.class)){
                        filesList .add(fileName);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
        return  filesList;
    }

    //
    public static String htmlHeader(){return "HTTP/1.1 200 OK \r\n" + "Content-Type: text/html \r\n" + "\r\n";}

}
