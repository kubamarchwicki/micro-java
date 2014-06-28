package pl.marchwicki.microjava;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import pl.marchwicki.microjava.model.Todo;
import pl.marchwicki.microjava.resources.TodoMVCResource;
import pl.marchwicki.microjava.services.Store;
import pl.marchwicki.microjava.services.db.DB;
import pl.marchwicki.microjava.services.db.TodoDAO;
import pl.marchwicki.microjava.services.db.TodoMapper;

import javax.sql.DataSource;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class TodoMVCApplication extends Application {
    private static Set services = new HashSet();

    public TodoMVCApplication() throws IOException {
        //database
        final Properties props = new Properties();
        props.load(TodoMVC.class.getResourceAsStream("/db.properties"));

        final DataSource dataSource = DB.setup(props.getProperty("url"),
                props.getProperty("user"),
                props.getProperty("password"));

        final TransactionFactory trxFactory = new JdbcTransactionFactory();
        final Environment env = new Environment("dev", trxFactory, dataSource);
        final Configuration config = new Configuration(env);
        config.getTypeAliasRegistry()
                .registerAlias("todo", Todo.class);
        config.addMapper(TodoMapper.class);

        final SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(config);
        final Store store = new Store(new TodoDAO(factory));
        services.add(new TodoMVCResource(store));
    }

    @Override
    public  Set getSingletons() {
        return services;
    }
}
