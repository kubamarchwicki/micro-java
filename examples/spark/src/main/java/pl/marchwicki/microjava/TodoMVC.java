package pl.marchwicki.microjava;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.services.Store;
import pl.marchwicki.microjava.services.db.DB;
import pl.marchwicki.microjava.services.db.TodoDAO;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static spark.Spark.*;

public class TodoMVC {

    public static void main(String[] args) throws SQLException, IOException {
        final Properties props = new Properties();
        props.load(TodoMVC.class.getResourceAsStream("/db.properties"));
        final DataSource dataSource = DB.setup(props.getProperty("url"),
                props.getProperty("user"),
                props.getProperty("password"));

        //TODO: close connection?? spark lifecycle
        final DSLContext create = DSL.using(dataSource, SQLDialect.MYSQL);
        final TodoDAO dao = new TodoDAO(create);
        final Store store = new Store(dao);

        staticFileLocation("META-INF/resources/");

        get("/todos", (request, response) -> store.getAll(), new JsonTransformer());

        post("/todos", (req, resp) -> {
            Todo data = new Gson().fromJson(req.body(), Todo.class);
            Todo todo = store.save(data);

            resp.status(200);
            resp.header("Content-Type", "application/json");
            return todo;
        }, new JsonTransformer());

        put("/todos/:id", (req, resp) -> {
            long id = Long.parseLong(req.params(":id"));
            Todo data = new Gson().fromJson(req.body(), Todo.class);
            Optional<Todo> todo = store.save(id, data);

            resp.status(204);
            resp.header("Content-Type", "application/json");
            return todo;
        }, new JsonTransformer());

        delete("/todos/:id", (req, resp) -> {
            long id = Long.parseLong(req.params(":id"));
            store.remove(id);

            halt(204);
            return null;
        });
    }
}
