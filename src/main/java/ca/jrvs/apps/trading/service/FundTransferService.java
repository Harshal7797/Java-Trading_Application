package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.ResourceNotFoundException;
import ca.jrvs.apps.trading.model.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundTransferService {

    private AccountDao accountDao;

    @Autowired
    public FundTransferService(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    /**
     * Deposit a fund to the account which is associated with the traderId
     * -validate User Input
     * -account = accountDao.findTraderId
     * -accountDAo.updateAmountByID
     *
     * @param traderId trader id
     * @param fund found amount (can't be zero)
     * @return updated Account object
     * @throw ResourceNotFoundException  if ticker is not found from IEX
     * @throw org.springframework.dao.DataAccessException if unable to retrieve data
     * @throw IllegalArgumentException for invalid input
     */

    public Account deposit(Integer traderId, Double fund){
        if(fund == 0 || traderId ==null ){
            throw new IllegalArgumentException("fund cannot be zero");
        }
        Account account;
        if(accountDao.existsById(traderId)){
             account = accountDao.findById(traderId);
             accountDao.updateAmountById(account.getId(), account.getAmount()+ fund);
             account.setAmount(account.getAmount() +fund);
        }
        else{
            throw new ResourceNotFoundException("Account not found with this id" + traderId);
        }

        return account;
    }

    /**
     * Withdraw a fund from the account which is associated with the traderId
     * -validate user input
     * -account = accountDao.findByTraderId
     * -account  = accountDAo.updateAmountById
     *
     * @param traderId trader ID
     * @Param fund amount can't be 0
     * @return updated Account object
     * @throw ResourceNotFoundException if ticker is not found from IEX
     * @throw org.springframework.dao.DataAccessException if unable to retrieve data
     * @throw IllegalArgumentException for invalid input
     */

    public Account withdraw(Integer traderId, Double fund) {
        if(fund == 0 || traderId ==null ){
            throw new IllegalArgumentException("fund cannot be zero");
        }
        Account account;
        if(accountDao.existsById(traderId)){
            account = accountDao.findById(traderId);
            accountDao.updateAmountById(account.getId(), account.getAmount()- fund);
            account.setAmount(account.getAmount()-fund);
        }
        else{
            throw new ResourceNotFoundException("Account not found with this id" + traderId);
        }

        return account;
    }
}