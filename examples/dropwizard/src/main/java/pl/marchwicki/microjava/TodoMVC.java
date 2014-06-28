package pl.marchwicki.microjava;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import pl.marchwicki.microjava.resources.TodoMVCResource;
import pl.marchwicki.microjava.services.Store;
import pl.marchwicki.microjava.services.db.TodoDAO;

public class TodoMVC extends Application<TodoMVCConfiguration> {

    public static void main(String[] args) throws Exception {
        new TodoMVC().run(args);
    }

    @Override
    public void initialize(Bootstrap<TodoMVCConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/META-INF/resources", "/"));
    }

    @Override
    public void run(TodoMVCConfiguration configuration, Environment environment) throws Exception {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        final TodoDAO dao = jdbi.onDemand(TodoDAO.class);
        final Store store = new Store(dao);

        environment.jersey().setUrlPattern("/todos/*");
        environment.jersey().register(new TodoMVCResource(store));
    }
}
