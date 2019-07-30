package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class AppConfig {
    private Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private static final String JDBC_URL = System.getenv("PSQL_URL");
    private static final String DB_USER = System.getenv("PSQL_USER");
    private static final String DB_PASSWORD = System.getenv("PSQL_PASSWORD");
//    @Value("${iex.host}")
    private String iex_host;


    @Bean
    public MarketDataConfig marketDataConfig() {
        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setUrl("https://cloud.iexapis.com/stable/");
        marketDataConfig.setPublicToken(System.getenv("public_token"));

        return marketDataConfig;
    }

    @Bean
    public DataSource dataSource(){
        System.out.println("Creating apacheDataSource");
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUrl(JDBC_URL);
        basicDataSource.setUsername(DB_USER);
        basicDataSource.setPassword(DB_PASSWORD);
        return  basicDataSource;
    }

    //http://bit.ly/2tWTmzQ connectionPool
    @Bean
    public HttpClientConnectionManager httpClientConnectionManager(){
        PoolingHttpClientConnectionManager cm =new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(50);
        return cm;
    }
}