package pl.marchwicki.microjava.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.marchwicki.microjava.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
