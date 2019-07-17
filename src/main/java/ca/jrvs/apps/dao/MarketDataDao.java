package ca.jrvs.apps.dao;

import ca.jrvs.apps.model.config.MarketDataConfig;
import ca.jrvs.apps.model.domain.IexQuote;
import ca.jrvs.apps.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MarketDataDao {

    private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
    public final String BATCH_QUOTE_URL;

    private HttpClientConnectionManager httpClientConnectionManager;
    public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager, MarketDataConfig marketDataConfig) {
        this.httpClientConnectionManager= httpClientConnectionManager;
        BATCH_QUOTE_URL=null;

    }

    public List<IexQuote> findIexQuoteByTicker(List<String> tickerList){
    //convert list into comma Separated String
        String ticker = StringUtils.join(tickerList, ',');
        String uri = String.format(BATCH_QUOTE_URL, ticker);
        logger.info("Get URI" + uri);
        //Get Http response body in string
        String response = executeHttpGet(uri);
        //Iex will skip invalid symbols/ticker..we need to check it
        JSONObject IexQuoteJson = new JSONObject(response);
        if (IexQuoteJson.length()!= tickerList.size()){
            throw new IllegalArgumentException("Invalid ticker/sumbol");
        }

        //Unmarshal JSOn object
        List<IexQuote> iexQuotes = new ArrayList<>();
        try{
            JSONArray jsonArray= new JSONArray();
            jsonArray = jsonArray.put(IexQuoteJson);
            if (jsonArray != null){
                for (int i = 0; i <jsonArray.length() ; i++) {
                    String quoteStr = ((JSONObject) IexQuoteJson.get(ticker)).get("quote").toString();
                    IexQuote iexQuote = JsonUtil.toObjectFromJson(quoteStr,IexQuote.class );
                    iexQuotes.add(i,iexQuote);
                }
            }
        }catch (IOException e){
            throw new DataRetrievalFailureException("Unable to parse Resposne:"+ IexQuoteJson.get(ticker),e);
        }
        return iexQuotes;
    }

    private String executeHttpGet(String url) {
        try (CloseableHttpClient httpClient = getHttpClient()){
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)){
                switch (response.getStatusLine().getStatusCode()){
                    case 200:
                        //EntityUtils toString will also close inputStream in Entity
                        String body = EntityUtils.toString(response.getEntity());
                        return Optional.ofNullable(body).orElseThrow(
                                () -> new IOException("Unexpected empty http response body"));
                    case 404:
                        throw new ResourceNotFoundException("Not found");
                    default:
                        throw new DataRetrievalFailureException(
                                "Unexpected status:" + response.getStatusLine().getStatusCode());
                }
            }
        } catch (IOException | ResourceNotFoundException e) {
            throw new DataRetrievalFailureException("Unable Http execution error", e);
        }
    }

    public IexQuote findIexQuoteByTicker(String ticker){
        List<IexQuote> quotes =findIexQuoteByTicker(Arrays.asList(ticker));
        if(quotes == null || quotes.size() != 1){
            throw new DataRetrievalFailureException("Unable to get data");
        }
        return quotes.get(0);
    }

    private CloseableHttpClient getHttpClient(){
        return HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                //prevent connectionManager shutdown when calling httpClient.clase()
                .setConnectionManagerShared(true)
                .build();
    }
}