package edu.eci.arep.Services;

public class ImgService implements RESTService {
    @Override
    public String getResponse(){return null;}
    @Override
    public String getHeader(){return "HTTP/1.1 200 OK \r\n" + "Content-Type: text/img \r\n" + "\r\n";}

}
