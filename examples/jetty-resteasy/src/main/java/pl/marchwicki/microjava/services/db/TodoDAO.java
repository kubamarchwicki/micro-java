package pl.marchwicki.microjava.services.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pl.marchwicki.microjava.model.Todo;

import java.util.List;

public class TodoDAO {

    final SqlSessionFactory sf;

    public TodoDAO(SqlSessionFactory sf) {
        this.sf = sf;
    }

    public void update(long id, Todo todo) {
        try (SqlSession session = sf.openSession()) {
            TodoMapper mapper = session.getMapper(TodoMapper.class);
            mapper.update(id, todo);
            session.commit();
        }
    }

    public long insert(Todo data) {
        try (SqlSession session = sf.openSession()) {
            TodoMapper mapper = session.getMapper(TodoMapper.class);
            mapper.insert(data);
            session.commit();
            return data.getId();
        }
    }

    public void delete(long id) {
        try (SqlSession session = sf.openSession()) {
            TodoMapper mapper = session.getMapper(TodoMapper.class);
            mapper.delete(id);
            session.commit();
        }
    }

    public Todo findById(long id) {
        try (SqlSession session = sf.openSession()) {
            TodoMapper mapper = session.getMapper(TodoMapper.class);
            return mapper.findById(id);
        }
    }

    public List<Todo> getAllTodos() {
        try (SqlSession session = sf.openSession()) {
            TodoMapper mapper = session.getMapper(TodoMapper.class);
            return mapper.getAllTodos();
        }
    }

}
