package pl.marchwicki.microjava.services.db;

import pl.marchwicki.microjava.model.Todo;

import java.util.List;
import java.util.Optional;


public class Store {

    final private TodoDAO dao;

    public Store(TodoDAO dao) {
        this.dao = dao;
    }

    public Todo save(Todo data) {
        long id = dao.insert(data.getTitle(), data.getOrder(), data.isCompleted());
        return Todo.TodoBuilder.aTodo()
                .withId(id)
                .withTitle(data.getTitle())
                .withOrder(data.getOrder())
                .isCompleted(data.isCompleted())
                .build();
    }

    public Optional<Todo> get(final long id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public List<Todo> getAll() {
        return dao.getAllTodos();
    }

    public Optional<Todo> save(long id, Todo data) {
        Optional<Todo> maybeTodo = get(id);

        if(maybeTodo.isPresent()) {
            dao.update(id, data);

            final Todo newTodo =  Todo.TodoBuilder.aTodo()
                    .withId(id)
                    .withTitle(data.getTitle())
                    .withOrder(data.getOrder())
                    .isCompleted(data.isCompleted())
                    .build();
            return Optional.of(newTodo);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Todo> remove(long id) {
        Optional<Todo> maybeTodo = get(id);

        if(maybeTodo.isPresent()) {
            dao.delete(id);
        }

        return Optional.empty();
    }
}