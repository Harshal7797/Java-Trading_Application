package ca.jrvs.apps.controller;

import ca.jrvs.apps.dao.TraderDao;
import ca.jrvs.apps.model.domain.Trader;
import ca.jrvs.apps.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/trader")
public class TraderController {

    TraderDao traderDao;
    RegisterService registerService;

    @Autowired
    public TraderController(TraderDao traderDao, RegisterService registerService){
        this.traderDao = traderDao;
        this.registerService = registerService;
    }


    @DeleteMapping (path = "/traderId/{traderId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteTrader(@PathVariable Integer traderid){
        try {
             traderDao.deleteById(traderid);
        }catch (Exception e){
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
    }

    @PostMapping (path =  "/")
    @ResponseStatus (HttpStatus.CREATED)
    @ResponseBody
    public void createTraderAndAccount(@RequestBody Trader trader){
        try{
            registerService.createTraderAccount(trader);
        }catch (Exception e){
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
    }
   @PostMapping (path = "/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createTrader(@PathVariable String firstname, String lastname, LocalDate dob, String country, String email){
    Trader trader = new Trader();
    trader.setCountry(country);
    trader.setDob(dob);
    trader.setFirstName(firstname);
    trader.setLastName(lastname);
    trader.setEmail(email);
    traderDao.save(trader);

   }



}
