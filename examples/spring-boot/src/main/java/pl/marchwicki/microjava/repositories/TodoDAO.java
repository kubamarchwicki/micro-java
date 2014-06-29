package pl.marchwicki.microjava.repositories;

import pl.marchwicki.microjava.model.Todo;

import java.util.List;

public interface TodoDAO {

    public void update(long id, Todo todo);

    public long insert(String title, long order, Boolean completed);

    public void delete(long id);

    public Todo findById(long id);

    public List<Todo> getAllTodos();

}
