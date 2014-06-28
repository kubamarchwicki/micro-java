package pl.marchwicki.microjava;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.sql2o.Sql2o;
import pl.marchwicki.microjava.services.db.Store;
import pl.marchwicki.microjava.services.db.TodoDAO;
import pl.marchwicki.microjava.web.TodoMVCServlet;

import java.util.Properties;

public class TodoMVC {

    public static void main(String... args) throws Exception {
        //database
        final Properties props = new Properties();
        props.load(TodoMVCServlet.class.getResourceAsStream("/db.properties"));

        final Sql2o ds = new Sql2o(props.getProperty("url"),
                props.getProperty("user"),
                props.getProperty("password"));

        final Store store = new Store(new TodoDAO(ds));

        WebAppContext root = new WebAppContext();
        root.setConfigurations(new Configuration[]{
                new WebXmlConfiguration(),
        });
        root.setContextPath("/");
        root.setParentLoaderPriority(true);

        String webDir = TodoMVC.class.getClassLoader().getResource("META-INF/resources").toExternalForm();
        root.setResourceBase(webDir);

        ServletHolder holder = new ServletHolder();
        holder.setServlet(new TodoMVCServlet(store));
        root.addServlet(holder, "/todos/*");

        Server server = new Server(8080);
        server.setHandler(root);
        server.start();
        server.join();
    }

}
