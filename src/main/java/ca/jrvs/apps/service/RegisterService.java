package ca.jrvs.apps.service;

import ca.jrvs.apps.dao.*;
import ca.jrvs.apps.model.domain.Account;
import ca.jrvs.apps.model.domain.Position;
import ca.jrvs.apps.model.domain.Trader;
import ca.jrvs.apps.model.view.TraderAccountView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * -create a trader in psql
     * -create an account in psql
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
        if (trader.getId() != null){
            throw new IllegalArgumentException("Id is not allowed as it's auto Generated");
        }

        Trader trader1 = traderDao.save(trader);
        TraderAccountView traderAccountView = new TraderAccountView();
        traderAccountView.setTrader(trader1);
        //Validate Input
//        if (StringUtil.isEmpty(trader.getFirstName(), trader.getLastName(), trader.getCountry(),
//                trader.getEmail()) || trader.getDob() == null) {
//            throw new IllegalArgumentException("Trader property cannot be null or empty");
//        }
        Account account = new Account();
        account.setAmount(0.0);
        account.setTraderId(trader1.getId());
        traderAccountView.setAccount(accountDao.save(account));


        return traderAccountView;
    }

    /**
     * A trader can be deleted if no open position and no cash balance
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

        if (traderID == null){
            throw new IllegalArgumentException("traderID cannot be null");
        }

        Account account = accountDao.findById(traderID);

        if(account.getAmount() != 0){
            throw new IllegalArgumentException("Can't delete Trader due to non-zero account amount");
        }
        List<Position> positions = positionDao.findById(account.getId());
        positions.forEach(position -> {
            if(position.getPosition() != 0){
                throw new IllegalArgumentException("Can't delete Trader due to open position");
            }
        });


            securityOrderDao.deleteById(account.getId());
            accountDao.deleteById(account.getId());
            traderDao.deleteById(traderID);
            if(accountDao.existsById(account.getId())){
                throw new ResourceNotFoundException("Resource not deleted");
            }
    }
}