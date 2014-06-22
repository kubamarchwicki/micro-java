package pl.marchwicki.microjava;

import org.skife.jdbi.v2.DBI;
import pl.marchwicki.microjava.services.Store;
import pl.marchwicki.microjava.services.db.DB;
import pl.marchwicki.microjava.services.db.TodoDAO;

import javax.sql.DataSource;

import static spark.Spark.*;

public class TodoMVC {

    public static void main(String[] args) {
        final DataSource dataSource = DB.setup(null, null, null);
        final DBI jdbi = new DBI(dataSource);
        final TodoDAO dao = jdbi.onDemand(TodoDAO.class);
        final Store store = new Store(dao);

        get("/hello", (request, response) -> store.getAll(), new JsonTransformer());
    }
}
