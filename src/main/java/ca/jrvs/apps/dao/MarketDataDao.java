package ca.jrvs.apps.dao;

import ca.jrvs.apps.model.domain.IexQuote;
import ca.jrvs.apps.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Repository
public class MarketDataDao {

    private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);

    private final String BATCH_QUOTE_URL;
    private HttpClientConnectionManager httpClientConnectionManager;

    @Autowired
    public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager) {
        this.httpClientConnectionManager = httpClientConnectionManager;
        BATCH_QUOTE_URL = "https://cloud.iexapis.com/stable/stock/market/batch?symbols=aapl&types=quote&token=pk_3872a146e4c945f4b922051bb8a11859";
    }

    /**
     * @throws DataRetrievalFailureException if unable to get http response
     */
    public List<IexQuote> findIexQuoteByTicker(List<String> tickerList) {
        //convert list into comma Separated String
        String tickers = StringUtils.join(tickerList, ',');
        String uri = String.format(BATCH_QUOTE_URL, tickers);
        logger.info("Get URI:" + uri);
        //Get Http response body in string
        String response = executeHttpGet(uri);
        JSONObject IexQuotesJson = new JSONObject(response);
        if (IexQuotesJson.length() == 0) {
                    }
        //Iex will skip invalid symbols/ticker..we need to check it
        if (IexQuotesJson.length() != tickerList.size()) {
            throw new IllegalArgumentException("Invalid ticker/symbol");
        }

        //Unmarshal JSON object (Try diffrent approach)
        List<IexQuote> iexQuotes = new ArrayList<>();
        IexQuotesJson.keys().forEachRemaining(ticker -> {
              try {
                  String quoteStr = ((JSONObject) IexQuotesJson.get(ticker)).get("quote").toString();
                  IexQuote iexQuote = JsonUtil.toObjectFromJson(quoteStr, IexQuote.class);
                  iexQuotes.add(iexQuote);
              } catch (IOException e) {
                  throw new DataRetrievalFailureException(
                          "Unable parse response:" + IexQuotesJson.get(ticker), e);
              }
        });
        return iexQuotes;
    }

    public IexQuote findIexQuoteByTicker(String ticker) {
        List<IexQuote> quotes = findIexQuoteByTicker(Arrays.asList(ticker));
        if (quotes == null || quotes.size() != 1) {
            throw new DataRetrievalFailureException("Unable to get data");
        }
        return quotes.get(0);
    }

    protected String executeHttpGet(String url) {
        try (CloseableHttpClient httpClient = getHttpClient()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                switch (response.getStatusLine().getStatusCode()) {
                    case 200:
                        //EntityUtils toString will also close inputStream in Entity
                        String body = EntityUtils.toString(response.getEntity());
                        return Optional.ofNullable(body).orElseThrow(
                                () -> new IOException("Unexpected empty http response body"));
                    case 404:
                        throw new Exception("Not found");
                    default:
                        throw new DataRetrievalFailureException(
                                "Unexpected status:" + response.getStatusLine().getStatusCode());
                }
            }
        } catch (Exception e) {
            throw new DataRetrievalFailureException("Unable Http execution error", e);
        }
    }

    private CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                //prevent connectionManager shutdown when calling httpClient.close()
                .setConnectionManagerShared(true)
                .build();
    }


}