package pl.marchwicki.microjava.repositories.data;

import org.springframework.beans.factory.annotation.Autowired;
import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.repositories.TodoDAO;

import java.util.List;

public class SpringDataTodoRepository implements TodoDAO {

    private final TodoRepository repository;

    @Autowired
    public SpringDataTodoRepository(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void update(long id, Todo todo) {
        repository.saveAndFlush(todo);
    }

    @Override
    public long insert(String title, long order, Boolean completed) {
        Todo todo = repository.saveAndFlush(Todo.TodoBuilder.aTodo()
                .withTitle(title)
                .withOrder(order)
                .isCompleted(completed)
                .build());

        return todo.getId();
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }

    @Override
    public Todo findById(long id) {
        return repository.findOne(id);
    }

    @Override
    public List<Todo> getAllTodos() {
        return repository.findAll();
    }
}
