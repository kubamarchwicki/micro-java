package pl.marchwicki.microjava.repositories.jdbc;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class TodoDeleteQuery extends SqlUpdate {

    public TodoDeleteQuery(DataSource ds) {
        super(ds, "delete from todos where todo_id = ?");
        super.declareParameter(new SqlParameter("id", Types.BIGINT));
        compile();
    }

}
