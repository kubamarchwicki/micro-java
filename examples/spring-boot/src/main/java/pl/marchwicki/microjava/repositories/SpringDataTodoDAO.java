package pl.marchwicki.microjava.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.marchwicki.microjava.model.Todo;

import java.util.List;

@Component
public class SpringDataTodoDAO implements TodoDAO {

    private final TodoRepository repository;

    @Autowired
    public  SpringDataTodoDAO(TodoRepository repository) {
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
