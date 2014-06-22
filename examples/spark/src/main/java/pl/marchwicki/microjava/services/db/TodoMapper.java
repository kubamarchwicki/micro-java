package pl.marchwicki.microjava.services.db;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import pl.marchwicki.microjava.model.Todo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TodoMapper implements ResultSetMapper<Todo> {

    @Override
    public Todo map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return Todo.TodoBuilder.aTodo()
                .withId(resultSet.getLong("todo_id"))
                .withTitle(resultSet.getString("todo_title"))
                .withOrder(resultSet.getLong("todo_order"))
                .isCompleted(resultSet.getBoolean("todo_completed"))
                .build();
    }
}
