package pl.marchwicki.microjava;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import pl.marchwicki.microjava.resources.TodoMVCResource;
import pl.marchwicki.microjava.services.Store;

public class TodoMVC extends Application<TodoMVCConfiguration> {

    public static void main(String[] args) throws Exception {
        new TodoMVC().run(args);
    }

    @Override
    public void initialize(Bootstrap<TodoMVCConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/META-INF/resources", "/www"));
    }

    @Override
    public void run(TodoMVCConfiguration configuration, Environment environment) throws Exception {
        final Store store = new Store();
        environment.jersey().register(new TodoMVCResource(store));
    }
}
