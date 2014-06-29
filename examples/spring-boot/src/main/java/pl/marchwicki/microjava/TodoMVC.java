package pl.marchwicki.microjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.marchwicki.microjava.repositories.data.SpringDataTodoRepository;
import pl.marchwicki.microjava.repositories.data.TodoRepository;
import pl.marchwicki.microjava.repositories.jpa.JpaTodoRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

public class TodoMVC extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(JpaConfiguration.class, args);
    }

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

}
