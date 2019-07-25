package ca.jrvs.apps;

import ca.jrvs.apps.controller.QuoteController;
import ca.jrvs.apps.dao.MarketDataDao;
import ca.jrvs.apps.dao.QuoteDao;
import ca.jrvs.apps.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


@SpringBootApplication(exclude = {
        JdbcTemplateAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
        //scanBasePackages = "ca.jrvs.apps.trading")
public class Application implements CommandLineRunner {

    @Value("A")
    private static String[] initDailyList;
    private QuoteController quoteController;
    private QuoteService quoteService;
    private QuoteDao quoteDao;
    private MarketDataDao marketDataDao;

    @Autowired
    public Application(QuoteController quoteController, QuoteService quoteService, QuoteDao quoteDao, MarketDataDao marketDataDao) {
        this.quoteController = quoteController;
        this.quoteService = quoteService;
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
    }

    @Override
    public void run(String... args) throws Exception {

    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);


        app.run();
    }
}