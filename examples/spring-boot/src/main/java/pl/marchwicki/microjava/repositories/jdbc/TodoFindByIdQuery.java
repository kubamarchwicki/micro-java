package pl.marchwicki.microjava.repositories.jdbc;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import pl.marchwicki.microjava.model.Todo;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class TodoFindByIdQuery extends MappingSqlQuery<Todo> {

    public TodoFindByIdQuery(DataSource ds) {
        super(ds, "select * from todos where todo_id = ?");
        super.declareParameter(new SqlParameter("id", Types.BIGINT));
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
