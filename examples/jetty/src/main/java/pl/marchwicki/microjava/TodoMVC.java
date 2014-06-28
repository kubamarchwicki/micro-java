package pl.marchwicki.microjava;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
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


        HandlerList handlers = new HandlerList();

        //servlet - initialize the servlet
        ServletContextHandler servlet = new ServletContextHandler();
        servlet.setContextPath("/todos");
        ServletHolder holder = new ServletHolder();
        holder.setServlet(new TodoMVCServlet(store));
        servlet.addServlet(holder, "/*");
        handlers.addHandler(servlet);

        //webcontext - load webResources from jar files
        WebAppContext root = new WebAppContext();
        root.setConfigurations(new Configuration[]{
                new WebXmlConfiguration(),
        });

        root.setContextPath("/");
        root.setParentLoaderPriority(true);

        String webDir = TodoMVC.class.getClassLoader().getResource("META-INF/resources").toExternalForm();
        root.setResourceBase(webDir);
        handlers.addHandler(root);

        Server server = new Server(8080);
        server.setHandler(handlers);
        server.start();
        server.join();
    }

}
