package ca.jrvs.apps.service;

import ca.jrvs.apps.dao.AccountDao;
import ca.jrvs.apps.dao.PositionDao;
import ca.jrvs.apps.dao.QuoteDao;
import ca.jrvs.apps.dao.SecurityOrderDao;
import ca.jrvs.apps.model.domain.SequrityOrder;
import ca.jrvs.apps.model.dto.MarketOrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
     * -Handle byt or sell order
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

        SequrityOrder sequrityOrder = new SequrityOrder();

        return sequrityOrder;
    }

}