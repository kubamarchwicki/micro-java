package pl.marchwicki.microjava.resources;

import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.services.Store;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TodoMVCResource {

    private final Store store;

    public TodoMVCResource(Store store) {
        this.store = store;
    }

    @GET
    public List<Todo> getAll() {
        return store.getAll();
    }

    @POST
    public Todo create(Todo todo) {
        return store.save(todo);
    }

    @PUT
    @Path("/{id}")
    public Optional<Todo> update(@PathParam("id") long id, Todo todo){
        return store.save(id, todo);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id){
        store.remove(id);
    }

}
