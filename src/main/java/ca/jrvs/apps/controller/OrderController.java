package ca.jrvs.apps.controller;

import ca.jrvs.apps.model.domain.SequrityOrder;
import ca.jrvs.apps.model.dto.MarketOrderDto;
import ca.jrvs.apps.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PutMapping(path = "/marketOrder")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SequrityOrder makeMarketOrder(MarketOrderDto marketOrderDto){
     try{
         return orderService.executeMarketOrder(marketOrderDto);
     }catch (Exception e){
         throw ResponseExceptionUtil.getResponseStatusException(e);
     }
    }
}
