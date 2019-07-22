package ca.jrvs.apps.dao;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MarketDataDaoTest {

    @Test
    public void Test(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(50);
        List<String> testi = new ArrayList<>();
        testi.add("AAPL");
        testi.add("MSFT");
        MarketDataDao test =new MarketDataDao(cm);
        test.findIexQuoteByTicker("aapl");
        // test.findIexQuoteByTicker(testi);
    }

}