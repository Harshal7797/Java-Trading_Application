package ca.jrvs.apps.controller;

import ca.jrvs.apps.dao.MarketDataDao;
import ca.jrvs.apps.dao.QuoteDao;
import ca.jrvs.apps.model.domain.IexQuote;
import ca.jrvs.apps.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/quote")
public class QuoteController {
    private QuoteService quoteService;
    private QuoteDao quoteDao;
    private MarketDataDao marketDataDao;

    @Autowired
    public QuoteController (QuoteService quoteService, QuoteDao quoteDao, MarketDataDao marketDataDao){
        this.quoteService = quoteService;
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
    }

    @PutMapping(path = "/iexMarketData")
    @ResponseStatus(HttpStatus.OK)
    public void updateMarketData() {
        try {
            quoteService.updateMarketData();
        } catch (Exception e) {
            throw ResponseExceptionUtil.getResponlseStatusException(e);
        }
    }

@GetMapping(path = "/iex/ticker/{ticker}")
@ResponseStatus(HttpStatus.OK)
@ResponseBody
    public IexQuote getQuote(@PathVariable String ticker) {
        try {
            return marketDataDao.findIexQuoteByTicker(ticker);
        } catch (Exception e) {
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
    }

}
