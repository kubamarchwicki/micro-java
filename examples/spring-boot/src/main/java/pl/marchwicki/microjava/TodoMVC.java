package pl.marchwicki.microjava;

import org.apache.naming.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.marchwicki.microjava.repositories.data.SpringDataTodoRepository;
import pl.marchwicki.microjava.repositories.data.TodoRepository;
import pl.marchwicki.microjava.repositories.jdbc.JdbcTodoRepository;
import pl.marchwicki.microjava.repositories.jdbc.TodoFindAllQuery;
import pl.marchwicki.microjava.repositories.jdbc.TodoFindByIdQuery;
import pl.marchwicki.microjava.repositories.jdbc.TodoUpdateQuery;
import pl.marchwicki.microjava.repositories.jpa.JpaTodoRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

public class TodoMVC extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringDataConfiguration.class, args);
    }

    @Configuration
    @ComponentScan
    @EnableAutoConfiguration
    public static class GenericConfiguration {

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
            ds.setUrl("jdbc:mysql://localhost:3306/microjava");
            ds.setUsername("root");
            ds.setPassword("root");
            return ds;
        }

    }

    @Configuration
    @ComponentScan
    @EnableAutoConfiguration
    public static class SpringDataConfiguration extends GenericConfiguration {

        @Bean
        @Autowired
        public SpringDataTodoRepository getRepository(TodoRepository repository) {
            return new SpringDataTodoRepository(repository);
        }
    }

    @Configuration
    @ComponentScan
    @EnableAutoConfiguration
    @EnableTransactionManagement
    public static class JpaConfiguration extends GenericConfiguration {

        @Bean
        @Autowired
        public JpaTodoRepository getRepository(EntityManager em) {
            return new JpaTodoRepository(em);
        }
    }

    @Configuration
    @ComponentScan
    @EnableAutoConfiguration
    public static class JdbcConfiguration extends GenericConfiguration {

        @Bean
        @Autowired
        public JdbcTodoRepository getRepository(DataSource ds) {
            return new JdbcTodoRepository(ds);
        }
    }

}
