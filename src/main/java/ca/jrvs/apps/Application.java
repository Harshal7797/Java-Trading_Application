package ca.jrvs.apps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import javax.sql.DataSource;


@SpringBootApplication(exclude = {
        JdbcTemplateAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
public class Application implements CommandLineRunner {
    @Autowired
    private DataSource dataSource;
    @Override
    public void run(String... args) throws Exception {

    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);


        app.run();
    }
}