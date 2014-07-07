package pl.marchwicki.microjava.repositories.jdbc;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TodoInsertQuery extends SqlUpdate {

    public TodoInsertQuery(DataSource ds) {
        setDataSource(ds);
        setSql("insert into todos (todo_title, todo_order, todo_completed) values (:title, :order, :completed)");
        declareParameter(new SqlParameter("title", Types.VARCHAR));
        declareParameter(new SqlParameter("order", Types.BIGINT));
        declareParameter(new SqlParameter("completed", Types.BIT));
        setReturnGeneratedKeys(true);
        compile();
    }

    public long run(final String title, final long order, final Boolean completed) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("title", title);
            put("completed", completed);
            put("order", order);
        }};

        updateByNamedParam(params, keyHolder);
        return keyHolder.getKey().longValue();
    }
}