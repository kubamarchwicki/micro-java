package pl.marchwicki.microjava.web;

import com.google.gson.Gson;
import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.services.db.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TodoMVCServlet extends HttpServlet {

    private final Store store;

    public TodoMVCServlet(Store store) {
        this.store = store;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final List<Todo> todos = store.getAll();

        resp.getWriter().write(new Gson().toJson(todos));
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        String body = reader.readLine();

        Todo data = new Gson().fromJson(body, Todo.class);
        Todo todo = store.save(data);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(todo));
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Long> maybeId = getIdFromPath(req.getRequestURI());

        if (maybeId.isPresent()) {
            BufferedReader reader = req.getReader();
            String body = reader.readLine();

            Todo data = new Gson().fromJson(body, Todo.class);
            store.save(maybeId.get(), data);

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Long> maybeId = getIdFromPath(req.getRequestURI());

        if (maybeId.isPresent()) {
            store.remove(maybeId.get());
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private Optional<Long> getIdFromPath(String path) {
        Pattern p = Pattern.compile("/todos\\/([0-9]+)\\/?");
        Matcher m = p.matcher(path);

        if (m.matches()) {
            Long value = Long.valueOf(m.group(1));
            return Optional.of(value);
        }

        return Optional.empty();
    }
}
