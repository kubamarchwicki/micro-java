package pl.marchwicki.microjava.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.marchwicki.microjava.model.Todo;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class TodoMVCController {

    private final Store store;

    @Autowired
    public TodoMVCController(Store store) {
        this.store = store;
    }

    @RequestMapping(value = "/todos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Todo> getAllTodos() {
        return store.getAll();
    }

    @RequestMapping(value = "/todos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Todo save(@Valid @RequestBody Todo data) {
        return store.save(data);
    }

    @RequestMapping(value = "/todos/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Optional<Todo> update(@PathVariable("id") long id, @Valid @RequestBody Todo data) {
        return store.save(id, data);
    }

    @RequestMapping(value = "/todos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        store.remove(id);
    }




}
