package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.*;
import ca.jrvs.apps.trading.model.dto.MarketOrderDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    //capture parameter when calling securityOrderDao.save
    @Captor
    ArgumentCaptor<SequrityOrder> captorSecurityOrder;

    //mock all dependencies
    @Mock
    private AccountDao accountDao;
    @Mock
    private SecurityOrderDao securityOrderDao;
    @Mock
    private QuoteDao quoteDao;
    @Mock
    private PositionDao positionDao;

    //injecting mocked dependencies to the testing class via constructor
    @InjectMocks
    private OrderService orderService;

    //setup test data
    private MarketOrderDto orderDto;

    @Before
    public void setup() {
        orderDto = new MarketOrderDto();
        orderDto.setAccountId(1);
        orderDto.setSize(1);
        orderDto.setTicker("AAPL");
    }

    @Test
    public void executeMarketOrderHappyPath() {
        when(quoteDao.existsById(orderDto.getTicker())).thenReturn(true);
        when(accountDao.existsById(orderDto.getAccountId())).thenReturn(true);

        Quote quote = new Quote();
        quote.setAskSize(10);
        quote.setAskPrice(100.00);
        when(quoteDao.findById(orderDto.getTicker())).thenReturn(quote);

        Account account = new Account();
        account.setAmount(100.00);
        account.setId(orderDto.getAccountId());
        when(accountDao.findById(orderDto.getAccountId())).thenReturn(account);

        orderService.executeMarketOrder(orderDto);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SequrityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(OrderStatus.FILLED, captorOrder.getStatus());
    }

    @Test
    public void executeMarketOrderSadPath() {
        when(quoteDao.existsById(orderDto.getTicker())).thenReturn(true);
        when(accountDao.existsById(orderDto.getAccountId())).thenReturn(true);

        Quote quote = new Quote();
        quote.setAskSize(10);
        quote.setAskPrice(100.00);
        when(quoteDao.findById(orderDto.getTicker())).thenReturn(quote);

        Account account = new Account();
        account.setAmount(40.00);
        account.setId(orderDto.getAccountId());
        when(accountDao.findById(orderDto.getAccountId())).thenReturn(account);

        orderService.executeMarketOrder(orderDto);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SequrityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(OrderStatus.CANCELED, captorOrder.getStatus());

    }
@Test
    public void sellHappy(){

        when(quoteDao.existsById(orderDto.getTicker())).thenReturn(true);
        when(accountDao.existsById(orderDto.getAccountId())).thenReturn(true);

        Quote quote = new Quote();
        quote.setAskSize(10);
        quote.setAskPrice(100.00);
        when(quoteDao.findById(orderDto.getTicker())).thenReturn(quote);

        Account account = new Account();
        account.setAmount(40.00);
        account.setId(orderDto.getAccountId());
        when(accountDao.findById(orderDto.getAccountId())).thenReturn(account);

        orderDto.setSize(-5);

        Position position =new Position();
        position.setAccountId(account.getId());
        position.setPosition(10);
        position.setTicker(orderDto.getTicker());
        when(positionDao.findByTickerAndId(orderDto.getTicker(),orderDto.getAccountId())).thenReturn(position);

        orderService.executeMarketOrder(orderDto);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SequrityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(OrderStatus.FILLED, captorOrder.getStatus());

    }

    @Test
    public void sellSad(){
        when(quoteDao.existsById(orderDto.getTicker())).thenReturn(true);
        when(accountDao.existsById(orderDto.getAccountId())).thenReturn(true);

        Quote quote = new Quote();
        quote.setAskSize(10);
        quote.setAskPrice(100.00);
        when(quoteDao.findById(orderDto.getTicker())).thenReturn(quote);

        Account account = new Account();
        account.setAmount(40.00);
        account.setId(orderDto.getAccountId());
        when(accountDao.findById(orderDto.getAccountId())).thenReturn(account);

        orderDto.setSize(-5);

        Position position =new Position();
        position.setAccountId(account.getId());
        position.setPosition(4);
        position.setTicker(orderDto.getTicker());
        when(positionDao.findByTickerAndId(orderDto.getTicker(),orderDto.getAccountId())).thenReturn(position);

        orderService.executeMarketOrder(orderDto);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SequrityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(OrderStatus.CANCELED, captorOrder.getStatus());

    }

}