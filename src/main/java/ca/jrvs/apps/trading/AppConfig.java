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

        String JDBC_URL=null;
        String DB_USER=null;
        String DB_PASSWORD=null;
        if(System.getenv("RDS_DB_NAME").isEmpty() && System.getenv("RDS_USERNAME").isEmpty()
        && System.getenv("RDS_PASSWORD").isEmpty() && System.getenv("RDS_HOSTNAME").isEmpty()
        && System.getenv("RDS_PORT").isEmpty()){
            JDBC_URL = "jdbc:postgresql://"+ System.getenv("RDS_HOSTNAME") +":" + System.getenv("RDS_PORT")
                    + "/" + System.getenv("RDS_DB_NAME");
            DB_USER = System.getenv("RDS_USERNAME");
            DB_PASSWORD = System.getenv("RDS_PASSWORD");


        }
        else {
             JDBC_URL = System.getenv("PSQL_URL");
              DB_USER = System.getenv("PSQL_USER");
              DB_PASSWORD = System.getenv("PSQL_PASSWORD");
        }
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