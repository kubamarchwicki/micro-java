package pl.marchwicki.microjava.repositories.data;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.marchwicki.microjava.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
