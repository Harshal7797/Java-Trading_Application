package ca.jrvs.apps.service;

import ca.jrvs.apps.dao.*;
import ca.jrvs.apps.model.domain.Account;
import ca.jrvs.apps.model.domain.Trader;
import ca.jrvs.apps.model.view.TraderAccountView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private TraderDao traderDao;
    private AccountDao accountDao;
    private PositionDao positionDao;
    private SecurityOrderDao securityOrderDao;

    @Autowired
    public RegisterService(TraderDao traderDao, AccountDao accountDao, PositionDao positionDao,
                           SecurityOrderDao securityOrderDao){
        this.traderDao = traderDao;
        this.accountDao = accountDao;
        this.positionDao = positionDao;
        this.securityOrderDao = securityOrderDao;
    }
    /**
     * Create a new trader and initialize a new account with 0 amount.
     * -Validate user input (all fields must not be empty)
     * -create a trader
     * -create an account
     * -create, setup and return a new traderAccountView
     *
     * @param trader trader info
     * @return traderAccountView
     * @throws ca.jrvs.apps.dao.ResourceNotFoundException if ticker is not found
     * @throws org.springframework.dao.DataAccessException if unable to retrieve data
     * @throws IllegalArgumentException if invalid input
     *
     */

    public TraderAccountView createTraderAccount(Trader trader){
        if (trader == null){
            throw new IllegalArgumentException("Trader can't be null");
        }

        TraderAccountView traderAccountView = new TraderAccountView();
        Account account = new Account();
        account.setAmount(0.0);
        try {
            account.setTraderId(trader.getId());
        }catch (DataAccessException e){
            logger.debug("Unable to Retrieve Data:" , e);
        }
        if(account.getId() == null){
            throw new ResourceNotFoundException("Resource not found");
        }
        traderAccountView.setTrader(trader);
        traderAccountView.setAccount(account);
        return traderAccountView;
    }

    /**
     * A trader can be deleted if no open position and no cache balance
     * -validate traderID
     * -get trader account by traderId and check account balance
     * -get positions by accountiD and check positions
     * -delete all sequrityOrders, account , trader (in this order)
     *
     *@param traderID
     *@throws ca.jrvs.apps.dao.ResourceNotFoundException if ticker is not found
     *@throws org.springframework.dao.DataAccessException if unable to retrieve data
     * @throws IllegalArgumentException for invalid input
     */
    public void deleteTraderById(Integer traderID){


    }
}