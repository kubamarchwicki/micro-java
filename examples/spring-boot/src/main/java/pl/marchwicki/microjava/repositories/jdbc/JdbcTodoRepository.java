package pl.marchwicki.microjava.repositories.jdbc;

import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.repositories.TodoDAO;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcTodoRepository implements TodoDAO {

    private final TodoFindAllQuery findAllQuery;
    private final TodoFindByIdQuery findByIdQuery;
    private final TodoUpdateQuery updateQuery;
    private final TodoInsertQuery insertQuery;
    private final TodoDeleteQuery deleteQuery;

    public JdbcTodoRepository(DataSource ds) {
        this.findAllQuery = new TodoFindAllQuery(ds);
        this.findByIdQuery = new TodoFindByIdQuery(ds);
        this.updateQuery = new TodoUpdateQuery(ds);
        this.insertQuery = new TodoInsertQuery(ds);
        this.deleteQuery = new TodoDeleteQuery(ds);
    }

    @Override
    public void update(final long id, final Todo todo) {
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("id", id);
            put("title", todo.getTitle());
            put("completed", todo.isCompleted());
            put("order", todo.getOrder());
        }};

        int execute = updateQuery.updateByNamedParam(params);
    }

    @Override
    public long insert(String title, long order, Boolean completed) {
        return insertQuery.run(title, order, completed);
    }

    @Override
    public void delete(long id) {
        deleteQuery.update(id);
    }

    @Override
    public Todo findById(long id) {
        return findByIdQuery.findObject(id);
    }

    @Override
    public List<Todo> getAllTodos() {
        return findAllQuery.execute();
    }
}
