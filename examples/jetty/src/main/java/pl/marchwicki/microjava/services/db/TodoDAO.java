package pl.marchwicki.microjava.services.db;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import pl.marchwicki.microjava.model.Todo;

import java.util.List;

public class TodoDAO {

    final Sql2o ds;

    public TodoDAO(Sql2o ds) {
        this.ds= ds;
    }

    public void update(long id, Todo todo) {
    }

    public long insert(String title, long order, Boolean completed) {
        return 0;
    }

    public void delete(long id) {
    }

    public Todo findById(long id) {
        return null;
    }

    public List<Todo> getAllTodos() {
        final String sql = "SELECT * " +
                "from todos";

        try(Connection con = ds.open()) {
            return con.createQuery(sql)
                    .addColumnMapping("todo_id", "id")
                    .addColumnMapping("todo_title", "title")
                    .addColumnMapping("todo_order", "order")
                    .addColumnMapping("todo_completed", "completed")
                    .executeAndFetch(Todo.class);
        }
    }

}
