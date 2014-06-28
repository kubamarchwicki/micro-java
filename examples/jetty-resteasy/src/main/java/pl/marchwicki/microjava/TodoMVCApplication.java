package pl.marchwicki.microjava;

import org.sql2o.Sql2o;
import pl.marchwicki.microjava.resources.TodoMVCResource;
import pl.marchwicki.microjava.services.db.Store;
import pl.marchwicki.microjava.services.db.TodoDAO;

import javax.ws.rs.core.Application;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class TodoMVCApplication extends Application {
    private static Set services = new HashSet();

    public TodoMVCApplication() throws IOException {
        //database
        final Properties props = new Properties();
        props.load(TodoMVC.class.getResourceAsStream("/db.properties"));

        final Sql2o ds = new Sql2o(props.getProperty("url"),
                props.getProperty("user"),
                props.getProperty("password"));

        final Store store = new Store(new TodoDAO(ds));
        services.add(new TodoMVCResource(store));
    }

    @Override
    public  Set getSingletons() {
        return services;
    }
}
