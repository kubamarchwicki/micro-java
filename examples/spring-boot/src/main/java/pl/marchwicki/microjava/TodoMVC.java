package pl.marchwicki.microjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@ComponentScan
@EnableAutoConfiguration
@Configuration
public class TodoMVC extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TodoMVC.class, args);
    }

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
