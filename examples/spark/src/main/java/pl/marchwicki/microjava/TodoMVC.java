package pl.marchwicki.microjava;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import pl.marchwicki.microjava.services.Store;
import pl.marchwicki.microjava.services.db.DB;
import pl.marchwicki.microjava.services.db.TodoDAO;

import javax.sql.DataSource;
import java.sql.SQLException;

import static spark.Spark.*;

public class TodoMVC {

    public static void main(String[] args) throws SQLException {
        final DataSource dataSource = DB.setup(null, null, null);

        //TODO: close connection?? spark lifecycle
        final DSLContext create = DSL.using(dataSource, SQLDialect.MYSQL);
        final TodoDAO dao = new TodoDAO(create);
        final Store store = new Store(dao);

        get("/hello", (request, response) -> store.getAll(), new JsonTransformer());
    }
}
