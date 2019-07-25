package ca.jrvs.apps;

import ca.jrvs.apps.model.config.MarketDataConfig;
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
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/jrvstrading_test";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "password";
//    @Value("${iex.host}")
    private String iex_host;

//    @Bean
//    public PlatformTransactionManager txManager(DataSource dataSource){
//        return null;
//    }

    @Bean
    public MarketDataConfig marketDataConfig() {
        return null;
    }

    @Bean
    public DataSource dataSource(){
        System.out.println("Creating apacheDataSource");
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUrl(JDBC_URL);
        basicDataSource.setUsername(DB_USER);
        basicDataSource.setPassword(DB_PASSWORD);
        return (DataSource) basicDataSource;
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
