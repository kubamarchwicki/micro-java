package pl.marchwicki.microjava.repositories.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.repositories.TodoDAO;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

public class JpaTodoRepository implements TodoDAO {

    private final EntityManager em;

    @Autowired
    public JpaTodoRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void update(long id, Todo todo) {
        em.merge(todo);
        em.flush();
    }

    @Override
    @Transactional
    public long insert(String title, long order, Boolean completed) {
        Todo data = Todo.TodoBuilder.aTodo()
                .withTitle(title)
                .withOrder(order)
                .isCompleted(completed)
                .build();

        em.persist(data);
        em.flush();
        return data.getId();
    }

    @Override
    @Transactional
    public void delete(long id) {
        Todo todo = em.getReference(Todo.class, id);
        em.remove(todo);
        em.flush();
    }

    @Override
    public Todo findById(long id) {
        return em.createNamedQuery(Todo.GET_BY_ID, Todo.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<Todo> getAllTodos() {
        return em.createNamedQuery(Todo.GET_ALL, Todo.class).getResultList();
    }
}
