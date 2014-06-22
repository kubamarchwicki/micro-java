package pl.marchwicki.microjava;

import static spark.Spark.*;

public class TodoMVC {

    public static void main(String[] args) {
        get("/hello",(request, response) -> "Hello World!");
    }
}
