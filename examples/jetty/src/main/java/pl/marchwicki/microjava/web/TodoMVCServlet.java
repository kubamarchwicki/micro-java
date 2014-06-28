package pl.marchwicki.microjava.web;

import com.google.gson.Gson;
import org.sql2o.Sql2o;
import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.services.db.Store;
import pl.marchwicki.microjava.services.db.TodoDAO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/todos/*")
public class TodoMVCServlet extends HttpServlet {

    private Store store;

    @Override
    public void init(ServletConfig config) throws ServletException {
        final Properties props = new Properties();
        try {
            props.load(TodoMVCServlet.class.getResourceAsStream("/db.properties"));
        } catch (IOException e) {
            throw new ServletException(e);
        }

        final Sql2o ds = new Sql2o(props.getProperty("url"),
                props.getProperty("user"),
                props.getProperty("password"));

        this.store = new Store(new TodoDAO(ds));

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
