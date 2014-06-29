package pl.marchwicki.microjava.repositories.jdbc;

import org.springframework.jdbc.object.MappingSqlQuery;
import pl.marchwicki.microjava.model.Todo;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TodoFindAllQuery extends MappingSqlQuery<Todo> {

    public TodoFindAllQuery(DataSource ds) {
        super(ds, "select * from todos");
        compile();
    }

    @Override
    protected Todo mapRow(ResultSet resultSet, int i) throws SQLException {
        return Todo.TodoBuilder.aTodo()
                .withId(resultSet.getLong("todo_id"))
                .withTitle(resultSet.getString("todo_title"))
                .withOrder(resultSet.getLong("todo_order"))
                .isCompleted(resultSet.getBoolean("todo_completed"))
                .build();
    }
}
