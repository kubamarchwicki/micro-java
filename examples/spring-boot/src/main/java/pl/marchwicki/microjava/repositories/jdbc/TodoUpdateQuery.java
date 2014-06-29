package pl.marchwicki.microjava.repositories.jdbc;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class TodoUpdateQuery extends SqlUpdate {

    public TodoUpdateQuery(DataSource ds) {
        setDataSource(ds);
        setSql("update todos set todo_title = :title, todo_order = :order, todo_completed = :completed " +
                "where todo_id = :id");
        declareParameter(new SqlParameter("id", Types.BIGINT));
        declareParameter(new SqlParameter("title", Types.VARCHAR));
        declareParameter(new SqlParameter("order", Types.BIGINT));
        declareParameter(new SqlParameter("completed", Types.BIT));
        compile();
    }
}