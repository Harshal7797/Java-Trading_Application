package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.*;
import ca.jrvs.apps.trading.model.dto.MarketOrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.Math.abs;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private AccountDao accountDao;
    private SecurityOrderDao securityOrderDao;
    private QuoteDao quoteDao;
    private PositionDao positionDao;

    @Autowired
    public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao,
                        QuoteDao quoteDao, PositionDao positionDao) {
        this.accountDao = accountDao;
        this.securityOrderDao = securityOrderDao;
        this.quoteDao = quoteDao;
        this.positionDao = positionDao;
    }

    /**
     * Execute a market order
     * <p>
     * -validate the order (e.g size, ticker)
     * -create a SequrityOrder (for security_order table)
     * -Handle buy or sell order
     * -buy order : check account balance
     * -sell order: check position for the ticker/symbol
     * -(please don't forget to update securityOrder.status)
     * -save and retrun sequrityOrder;
     * <p>
     * NOTE: you will need to some helper method (protected or private)
     *
     * @param orderDto market order
     * @return SecurityOrder from Security_order table
     * @throws org.springframework.dao.DataAccessException if unable to get data from DAO
     * @throws IllegalArgumentException                    for invalid input
     */
    public SequrityOrder executeMarketOrder(MarketOrderDto orderDto) {

        if(orderDto.getSize() ==0 || orderDto.getTicker() ==null) {
            throw new IllegalArgumentException("orderDto fields cannot be empty");
        }

        SequrityOrder sequrityOrder = new SequrityOrder();
        Account account =accountDao.findById(orderDto.getAccountId());
        Quote quote = quoteDao.findById(orderDto.getTicker());

        sequrityOrder.setTicker(orderDto.getTicker());
        sequrityOrder.setAccountId(orderDto.getAccountId());
        sequrityOrder.setId(sequrityOrder.getId());
        sequrityOrder.setSize(orderDto.getSize());
        sequrityOrder.setPrice(quote.getAskPrice());
        sequrityOrder.setStatus(OrderStatus.PENDING);

        if(sequrityOrder.getSize() > 0){
            sequrityOrder.setStatus(buyStock(account,quote,orderDto));
        }
        else{
            sequrityOrder.setStatus(sellStock(account,quote,orderDto));
        }
        securityOrderDao.save(sequrityOrder);


        return sequrityOrder;
    }

    private OrderStatus buyStock(Account account,Quote quote, MarketOrderDto orderDto){
        double tobuy = abs(orderDto.getSize()) * quote.getAskPrice();
        if(account.getAmount() >= tobuy){
            accountDao.updateAmountById(account.getId(),account.getAmount() - tobuy);
            return OrderStatus.FILLED;
        }
        else{
            return OrderStatus.CANCELED;
        }

    }
    private OrderStatus sellStock( Account account, Quote quote, MarketOrderDto orderDto){
        double toSell = abs(orderDto.getSize())* quote.getAskPrice();
        Position position = positionDao.findByTickerAndId(orderDto.getTicker(),orderDto.getAccountId());
        if (position.getPosition() > abs(orderDto.getSize())){
            accountDao.updateAmountById(account.getId(),account.getAmount() - toSell);
            return OrderStatus.FILLED;
        }

        else {
            return OrderStatus.CANCELED;
        }

    }
}

