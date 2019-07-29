package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.SequrityOrder;
import ca.jrvs.apps.trading.model.dto.MarketOrderDto;
import ca.jrvs.apps.trading.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    @PostMapping(path = "/marketOrder")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SequrityOrder makeMarketOrder(@RequestBody MarketOrderDto marketOrderDto){
     try{
         return orderService.executeMarketOrder(marketOrderDto);
     }catch (Exception e){
         throw ResponseExceptionUtil.getResponseStatusException(e);
     }
    }
}