package ca.jrvs.apps.service;


import ca.jrvs.apps.dao.MarketDataDao;
import ca.jrvs.apps.dao.QuoteDao;
import ca.jrvs.apps.dao.ResourceNotFoundException;
import ca.jrvs.apps.model.domain.IexQuote;
import ca.jrvs.apps.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@Transactional
public class QuoteService {

    private static final Logger logger= LoggerFactory.getLogger(QuoteService.class);
    private QuoteDao quoteDao;
    private MarketDataDao marketDataDao;

    @Autowired
    public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
    }

    /**
     * Helper method. Map a IexQuote to a Quote entity.
     * Note: `iexQuote.getLatestPrice() == null` if the stock market is closed.
     * Make sure set a default value for number field(s).
     */
    public static Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
        Quote quote = new Quote();
        quote.setAskPrice(Integer.parseInt(iexQuote.getIexAskPrice()));
        quote.setAskSize(Integer.parseInt(iexQuote.getIexAskSize()));
        quote.setBidPrice(Integer.parseInt(iexQuote.getIexBidPrice()));
        quote.setBidSize(Integer.parseInt(iexQuote.getIexBidSize()));
        quote.setBidSize(Integer.parseInt(iexQuote.getLatestPrice()));
        quote.setTicker(iexQuote.getSymbol());
        return quote;
    }

    /**
     * Add a list of new tickers to the quote table. Skip existing ticker(s).
     *  - Get iexQuote(s)
     *  - convert each iexQuote to Quote entity
     *  - persist the quote to db
     *
     * @param tickers a list of tickers/symbols
     * @throws ca.jrvs.apps.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException if unable to retrieve data
     * @throws IllegalArgumentException for invalid input
     */
    public void initQuotes(List<String> tickers) {
        if(tickers.isEmpty()){
            throw new IllegalArgumentException("tickers cannot be null");
        }
        //buildQuoteFromIexQuote helper method is used here
        List<IexQuote> iexQuotes = marketDataDao.findIexQuoteByTicker(tickers);
        List<Quote> quotes = new ArrayList<>();
       try {
           for (IexQuote iexQuote : iexQuotes) {
               quotes.add(buildQuoteFromIexQuote(iexQuote));

           }
       }catch (EmptyResultDataAccessException e){
           logger.debug("Unable to retrieve data",e);
       }

        if(quotes.isEmpty()){
            throw new ResourceNotFoundException("Resource not found");
    }
        quoteDao.update(quotes);
    }

    /**
     * Add a new ticker to the quote table. Skip existing ticker.
     *
     * @param ticker ticker/symbol
     * @throws ca.jrvs.apps.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException if unable to retrieve data
     * @throws IllegalArgumentException for invalid input
     */
    public void initQuote(String ticker) {
        List<String> myList = new ArrayList<String>(Arrays.asList(ticker.split(",")));
        initQuotes(myList);
    }

    /**
     * Update quote table against IEX source
     *  - get all quotes from the db
     *  - foreach ticker get iexQuote
     *  - convert iexQuote to quote entity
     *  - persist quote to db
     *
     * @throws ca.jrvs.apps.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException if unable to retrieve data
     * @throws IllegalArgumentException for invalid input
     */
    public void updateMarketData() {
        quoteDao.findAll().forEach(x->initQuote(x.getTicker()));
    }
}