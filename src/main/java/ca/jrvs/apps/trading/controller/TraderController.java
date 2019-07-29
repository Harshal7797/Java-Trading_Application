package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.service.FundTransferService;
import ca.jrvs.apps.trading.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/trader")
public class TraderController {

    FundTransferService fundTransferService;
    RegisterService registerService;

    @Autowired
    public TraderController(RegisterService registerService, FundTransferService fundTransferService){
        this.registerService = registerService;
        this.fundTransferService = fundTransferService;
    }

    @DeleteMapping (path = "/traderId/{traderId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteTrader(@PathVariable Integer traderId){
        try {
             registerService.deleteTraderById(traderId);
        }catch (Exception e){
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
    }

    @PostMapping (path =  "/")
    @ResponseStatus (HttpStatus.CREATED)
    @ResponseBody
    public void TraderAndAccountView(@RequestBody Trader trader){
        try{
            registerService.createTraderAccount(trader);
        }catch (Exception e){
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
    }
   @PostMapping (path = "/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createTrader(@PathVariable("firstname") String firstname, @PathVariable("lastname") String lastname,
                             @PathVariable("dob") LocalDate dob, @PathVariable("country") String country,
                             @PathVariable("email")String email){
    Trader trader = new Trader();
    trader.setCountry(country);
    trader.setDob(dob);
    trader.setFirstName(firstname);
    trader.setLastName(lastname);
    trader.setEmail(email);
    try {
        registerService.createTraderAccount(trader);
    }catch (Exception e){
        throw ResponseExceptionUtil.getResponseStatusException(e);
    }
   }

   @PutMapping(path = "/trader/deposit/traderId/{traderId}/amount/{amount}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Account depositToAccount(@PathVariable("traderId") Integer traderId, @PathVariable("amount") Double amount){
        try{
            return fundTransferService.deposit(traderId,amount);
        }catch (Exception e){
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
   }

   @PutMapping(path ="/trader/withdraw/traderId/{traderId}/amount/{amount}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Account withdrawFromAccount(@PathVariable("traderId") Integer traderId, @PathVariable("amount") Double amount){
        try{
            return fundTransferService.withdraw(traderId, amount);
        }catch (Exception e){
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
   }
}
