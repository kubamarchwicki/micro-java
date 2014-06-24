package pl.marchwicki.microjava.services.db;

import org.jooq.DSLContext;
import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.services.db.tables.records.TodosRecord;

import java.util.List;

import static pl.marchwicki.microjava.services.db.Tables.*;

public class TodoDAO {

    DSLContext context;

    public TodoDAO(DSLContext ctx) {
        this.context = ctx;
    }

    public void update(long id, Todo todo) {
        context.update(TODOS)
                .set(TODOS.TODO_TITLE, todo.getTitle())
                .set(TODOS.TODO_ORDER, todo.getOrder())
                .set(TODOS.TODO_COMPLETED, todo.isCompleted())
                .where(TODOS.TODO_ID.equal(id))
                .execute();
    }

    public long insert(String title, long order, Boolean completed) {
        TodosRecord todosRecord = context.newRecord(TODOS);
        todosRecord.setTodoTitle(title);
        todosRecord.setTodoOrder(order);
        todosRecord.setTodoCompleted(completed);

        todosRecord.store();
        return todosRecord.getTodoId();
    }

    public void delete(long id) {
        TodosRecord todosRecord = context.fetchOne(TODOS, TODOS.TODO_ID.equal(id));
        todosRecord.delete();
    }

    public Todo findById(long id) {
        return context.select()
                .from(TODOS).where(TODOS.TODO_ID.equal(id))
                .fetchOne().map(new TodoMapper());
    }

    public List<Todo> getAllTodos() {
        return context.select().from(TODOS).fetch().map(new TodoMapper());
    }

}
