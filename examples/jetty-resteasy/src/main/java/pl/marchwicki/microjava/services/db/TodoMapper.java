package pl.marchwicki.microjava.services.db;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import pl.marchwicki.microjava.model.Todo;

import java.util.List;

public interface TodoMapper {

    @Update("update todos set todo_title = #{todo.title}, todo_order = #{todo.order}, todo_completed = #{todo.completed} " +
            "where todo_id = #{id}")
    void update(@Param("id") long id, @Param("todo") Todo todo);

    @Insert("insert into todos (todo_title, todo_order, todo_completed) values (#{todo.title}, #{todo.order}, #{todo.completed})")
    @Options(useGeneratedKeys = true, keyProperty = "todo.id")
    long insert(@Param("todo") Todo data);

    @Delete("delete from todos where todo_id = #{id}")
    void delete(@Param("id") long id);

    @Select("select * from todos where todo_id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "todo_id"),
            @Result(property = "title", column = "todo_title"),
            @Result(property = "order", column = "todo_order"),
            @Result(property = "completed", column = "todo_completed"),
    })
    Todo findById(@Param("id") long id);

    @Select("select * from todos")
    @Results(value = {
            @Result(property = "id", column = "todo_id"),
            @Result(property = "title", column = "todo_title"),
            @Result(property = "order", column = "todo_order"),
            @Result(property = "completed", column = "todo_completed"),
    })
    List<Todo> getAllTodos();

}
