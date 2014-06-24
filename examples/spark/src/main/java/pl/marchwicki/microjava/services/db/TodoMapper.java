package pl.marchwicki.microjava.services.db;

import org.jooq.Record;
import org.jooq.RecordMapper;
import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.services.db.tables.records.TodosRecord;

public class TodoMapper implements RecordMapper<Record, Todo> {

    @Override
    public Todo map(Record record) {
        TodosRecord todosRecord = (TodosRecord) record;
        return Todo.TodoBuilder.aTodo()
                .withId(todosRecord.getTodoId())
                .withTitle(todosRecord.getTodoTitle())
                .withOrder(todosRecord.getTodoOrder())
                .isCompleted(todosRecord.getTodoCompleted())
                .build();
    }
}
