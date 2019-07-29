package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.view.PortfolioView;
import ca.jrvs.apps.trading.model.view.SecurityRow;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DashboardService {

    private TraderDao traderDao;
    private PositionDao positionDao;
    private AccountDao accountDao;
    private QuoteDao quoteDao;

    @Autowired
    public DashboardService(TraderDao traderDao, PositionDao positionDao, AccountDao accountDao, QuoteDao quoteDao) {
        this.traderDao = traderDao;
        this.positionDao = positionDao;
        this.accountDao = accountDao;
        this.quoteDao = quoteDao;
    }
    /**
     * Create and retrun a traderAccountView by trader ID
     * -get trader account by id
     * -get trader info by id
     * -create and return a traderAccountView
     *
     *@param traderId trader ID
     *@return traderAccountView
     *@throws ResourceNotFoundException if ticker is not found from IEX
     *@throws org.springframework.dao.DataAccessException if unable to retrieve data
     *@throws IllegalArgumentException for invalid input
     */
    public TraderAccountView getTraderAccount(Integer traderId){
        if(traderId ==null){
            throw new IllegalArgumentException("TraderID cannot be null");
        }
        TraderAccountView traderAccountView = new TraderAccountView();
        Account account = accountDao.findById(traderId);
        Trader trader = traderDao.findById(traderId);
        try{
            traderAccountView.setAccount(account);
            traderAccountView.setTrader(trader);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Could not create traderAccountView ",e);
        }

        return traderAccountView;
    }

    /**
     * create and retrun portfolioView by trader ID
     * -get account by trader id
     * -get position by account id
     * -create and return portfolioView
     * @param traderId trader ID
     * @return portfolioView
     * @throws ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException if unable to retrieve data
     * @throws IllegalArgumentException for invalid input
     */
    public PortfolioView getProfileViewByTraderId(Integer traderId) {
        if(traderId ==null){
            throw new IllegalArgumentException("TraderID cannot be null");
        }
        PortfolioView portfolioView = new PortfolioView();
        Account account = accountDao.findById(traderId);
        List<Position> positions = positionDao.findById(account.getId());
        SecurityRow securityRow = new SecurityRow();

        try{
            positions.forEach(x-> {
                        securityRow.setPosition(x);
                        securityRow.setTicker(x.getTicker());
                        securityRow.setQuote(quoteDao.findById(x.getTicker()));
                        portfolioView.setSecurityRows(Collections.singletonList(securityRow));
            }
            );
        }catch (DataAccessException e){
            throw new ResourceNotFoundException("Resource not Found");
        }

        return  portfolioView;
    }
}