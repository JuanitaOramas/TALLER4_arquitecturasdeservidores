package edu.eci.arep;

@Component
public class Name {


    @RequestMapping("/name")
    public static String hello() {
        return "juanita";
    }

}