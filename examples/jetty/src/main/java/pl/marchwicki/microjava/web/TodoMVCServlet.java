package pl.marchwicki.microjava.web;

import com.google.gson.Gson;
import org.sql2o.Sql2o;
import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.services.db.TodoDAO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@WebServlet(urlPatterns = "/todos/*")
public class TodoMVCServlet extends HttpServlet {

    private TodoDAO dao;

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

        this.dao = new TodoDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final List<Todo> todos = dao.getAllTodos();

        resp.getWriter().write(new Gson().toJson(todos));
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ping");
    }
}
