package pl.marchwicki.microjava;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

public class TodoMVC {

    public static void main(String... args) throws Exception {
        HandlerList handlers = new HandlerList();

        //servlet - initialize the servlet
        ServletContextHandler servlet = new ServletContextHandler();
        servlet.setContextPath("/todos");
        ServletHolder holder = new ServletHolder(new HttpServletDispatcher());
        holder.setInitParameter("javax.ws.rs.Application", "pl.marchwicki.microjava.TodoMVCApplication");
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
