package pl.marchwicki.microjava.services.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import pl.marchwicki.microjava.model.Todo;

import java.util.List;

@RegisterMapper(TodoMapper.class)
public interface TodoDAO {

    @SqlUpdate("update todos set todo_title = :todo.title, todo_order = :todo.order, todo_completed = :todo.completed " +
            "where todo_id = :id")
    void update(@Bind("id") long id, @BindBean("todo") Todo todo);

    @SqlUpdate("insert into todos (todo_title, todo_order, todo_completed) values (:title, :order, :completed)")
    @GetGeneratedKeys
    long insert(@Bind("title") String title, @Bind("order") long order, @Bind("completed") Boolean completed);

    @SqlUpdate("delete from todos where todo_id = :id")
    void delete(@Bind("id") long id);

    @SqlQuery("select * from todos where todo_id = :id")
    Todo findById(@Bind("id") long id);

    @SqlQuery("select * from todos")
    List<Todo> getAllTodos();

}
