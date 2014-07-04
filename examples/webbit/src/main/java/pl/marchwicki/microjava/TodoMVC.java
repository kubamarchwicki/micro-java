package pl.marchwicki.microjava;

import com.google.gson.Gson;
import org.skife.jdbi.v2.DBI;
import org.webbitserver.WebServer;
import org.webbitserver.handler.EmbeddedResourceHandler;
import org.webbitserver.handler.logging.LoggingHandler;
import org.webbitserver.handler.logging.SimpleLogSink;
import org.webbitserver.netty.NettyWebServer;
import org.webbitserver.rest.Rest;
import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.services.Store;
import pl.marchwicki.microjava.services.db.DB;
import pl.marchwicki.microjava.services.db.TodoDAO;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class TodoMVC {

    public static void main(String... args) throws Exception {
        final Properties props = new Properties();
        props.load(TodoMVC.class.getResourceAsStream("/db.properties"));
        final DataSource dataSource = DB.setup(props.getProperty("url"),
                props.getProperty("user"),
                props.getProperty("password"));

        final DBI jdbi = new DBI(dataSource);
        final TodoDAO dao = jdbi.onDemand(TodoDAO.class);
        final Store store = new Store(dao);

        WebServer server = new NettyWebServer(9991)
                .add(new LoggingHandler(new SimpleLogSink()))
                .add(new EmbeddedResourceHandler("META-INF/resources"))
                .add("/", new EmbeddedResourceHandler("META-INF/resources/index.html"));

        Rest rest = new Rest(server);

        rest.GET("/todos", (req, resp, control) -> {
            List<Todo> todos = store.getAll();
            String json = new Gson().toJson(todos);

            resp.header("Content-Type", "application/json")
                    .content(json)
                    .end();
        });

        rest.POST("/todos", (req, resp, control) -> {
            Todo data = new Gson().fromJson(req.body(), Todo.class);
            Todo todo = store.save(data);
            String json = new Gson().toJson(todo);

            resp.header("Content-Type", "application/json")
                    .status(201)
                    .content(json)
                    .end();
        });

        rest.PUT("/todos/{id}", (req, resp, control) -> {
            final long id = Rest.intParam(req, "id");

            Todo data = new Gson().fromJson(req.body(), Todo.class);
            Optional<Todo> todo = store.save(id, data);
            String json = new Gson().toJson(todo);

            resp.header("Content-Type", "application/json")
                    .status(200)
                    .content(json)
                    .end();
        });

        rest.DELETE("/todos/{id}", (req, resp, control) -> {
            final long id = Rest.intParam(req, "id");
            store.remove(id);

            resp.status(204)
                    .end();
        });

        server.start().get();
        System.out.println("Listening on " + server.getUri());
    }

}
