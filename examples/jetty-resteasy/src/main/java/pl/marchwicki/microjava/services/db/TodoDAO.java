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
        String sql = "UPDATE todos SET todo_title = :title, " +
                "todo_order = :order, " +
                "todo_completed = :completed " +
                "where todo_id = :id";

        try (Connection con = ds.open()) {
            con.createQuery(sql, true)
                    .addParameter("id", id)
                    .addParameter("title", todo.getTitle())
                    .addParameter("order", todo.getOrder())
                    .addParameter("completed", todo.isCompleted())
                    .executeUpdate();
        }
    }

    public long insert(String title, long order, Boolean completed) {
        String sql = "INSERT into todos (todo_title, todo_order, todo_completed) " +
                "values (:title, :order, :completed)";

        try (Connection con = ds.open()) {
            Object key = con.createQuery(sql, true)
                    .addParameter("title", title)
                    .addParameter("order", order)
                    .addParameter("completed", completed)
                    .executeUpdate()
                    .getKey();

            return (Long) key;
        }
    }

    public void delete(long id) {
        String sql = "DELETE from todos where todo_id = :id";

        try (Connection con = ds.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public Todo findById(long id) {
        final String sql = "SELECT * " +
                "from todos where todo_id = :id";

        try(Connection con = ds.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .addColumnMapping("todo_id", "id")
                    .addColumnMapping("todo_title", "title")
                    .addColumnMapping("todo_order", "order")
                    .addColumnMapping("todo_completed", "completed")
                    .executeAndFetchFirst(Todo.class);
        }
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
