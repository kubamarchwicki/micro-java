package pl.marchwicki.microjava.services.db;

import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.sql.DataSource;

public class DB {

    public static DataSource setup(String url, String user, String password) {

        PoolProperties p = new PoolProperties();
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUrl(url);
        p.setUsername(user);
        p.setPassword(password);

        return new org.apache.tomcat.jdbc.pool.DataSource(p);
    }

}
