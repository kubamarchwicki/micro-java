package pl.marchwicki.microjava;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

public class TodoMVC {

    public static void main(String... args) throws Exception {
        WebAppContext root = new WebAppContext();
        root.setConfigurations(new Configuration[]{
                new WebXmlConfiguration(),
        });
        root.setContextPath("/");
        root.setParentLoaderPriority(true);

        String webDir = TodoMVC.class.getClassLoader().getResource("META-INF/resources").toExternalForm();
        root.setResourceBase(webDir);

        ServletHolder holder = new ServletHolder(new HttpServletDispatcher());
        holder.setInitParameter("javax.ws.rs.Application", "pl.marchwicki.microjava.TodoMVCApplication");
        root.addServlet(holder, "/todos/*");

        Server server = new Server(8080);
        server.setHandler(root);
        server.start();
        server.join();
    }

}
