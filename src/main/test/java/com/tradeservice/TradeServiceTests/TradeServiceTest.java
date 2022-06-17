package com.tradeservice.TradeServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.broker.external.BrokerTradeSide;
import com.client.entity.Trade;
import com.client.exception.TradeCreationException;
import com.client.exception.TradeNotFoundException;
import com.client.respository.TradeRepository;
import com.client.service.TradeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeServiceTest {

    @Autowired
    TradeService tradeService;

    @MockBean
    TradeRepository tradeRepository;

    @Autowired
    private Validator validator;

    private String tradeId = "5d9242c0-6f17-461b-96cc-34442bb8b249";

    @Test
    public void test_getAllTrades() {

        List<Trade> trades = Stream.of(new Trade(tradeId, Long.valueOf(10), "USD/JPY",
                BrokerTradeSide.BUY, BigDecimal.valueOf(1.25), "PENDING_EXECUTION", null, new Date()))
                .collect(Collectors.toList());

        when(tradeRepository.findAll()).thenReturn(trades);

        assertEquals(1, tradeService.getAllTrades().size());

    }

    @Test
    public void test_getTradeById() {

        Optional<Trade> optionalTrade = Optional
                .of(new Trade(tradeId, Long.valueOf(10), "USD/JPY",
                        BrokerTradeSide.BUY, BigDecimal.valueOf(1.25), "PENDING_EXECUTION", null, new Date()));

        when(tradeRepository.getById(tradeId)).thenReturn(optionalTrade);

        assertEquals(tradeId, tradeService.getTrade(tradeId).getId());

    }

    @Test(expected = TradeNotFoundException.class)
    public void test_getTradeByIdNotFoundTest() {

        Optional<Trade> optionalTrade = Optional
                .of(new Trade(tradeId, Long.valueOf(10), "USD/JPY",
                        BrokerTradeSide.BUY, BigDecimal.valueOf(1.25), "PENDING_EXECUTION", null, new Date()));

        when(tradeRepository.getById(tradeId)).thenReturn(optionalTrade);

        assertEquals(tradeId, tradeService.getTrade("tradeId").getId());

    }

    @Test
    public void test_getTradeStatus() {

        Optional<Trade> optionalTrade = Optional
                .of(new Trade(tradeId, Long.valueOf(10), "USD/JPY",
                        BrokerTradeSide.BUY, BigDecimal.valueOf(1.25), "NOT_EXECUTED", "No available quotes",
                        new Date()));

        when(tradeRepository.getById(tradeId)).thenReturn(optionalTrade);

        assertEquals("NOT_EXECUTED", tradeService.getTradeStatus(tradeId).getStatus());

    }

    @Test
    public void test_checkViolations() {
        Trade trade = new Trade(tradeId, Long.valueOf(10), "USD/JPY",
                BrokerTradeSide.BUY, BigDecimal.valueOf(1.25), "NOT_EXECUTED", "No available quotes",
                new Date());

        Set<ConstraintViolation<Trade>> violations = validator.validate(trade);
        assertTrue(violations.isEmpty());

        trade.setSymbol("USSD/JDDJ");
        violations = validator.validate(trade);
        assertEquals(1, violations.size());

        trade.setPrice(BigDecimal.valueOf(0));
        violations = validator.validate(trade);
        assertEquals(2, violations.size());

        trade.setQuantity(Long.valueOf(-1));
        violations = validator.validate(trade);
        assertEquals(3, violations.size());

    }

    @Test
    public void test_saveTrade() throws TradeCreationException {
        Trade trade = new Trade(tradeId, Long.valueOf(10), "USD/JPY",
                BrokerTradeSide.BUY, BigDecimal.valueOf(1.25), "NOT_EXECUTED", "No available quotes",
                new Date());

        when(tradeRepository.save(trade)).thenReturn(trade);

        assertEquals(trade, tradeService.saveTrade(trade, BrokerTradeSide.BUY));
    }

    @Test
    public void test_findIdleTrades() {

        List<Trade> trades = Stream.of(new Trade(tradeId, Long.valueOf(10), "USD/JPY",
                BrokerTradeSide.BUY, BigDecimal.valueOf(1.25), "PENDING_EXECUTION", null, new Date()))
                .collect(Collectors.toList());

        when(tradeRepository.findIdleTrades()).thenReturn(trades);

        assertEquals(1, tradeService.findIdleTrades().size());

    }

    @Test
    public void test_updateTradeStatus() {
        Trade trade = new Trade(tradeId, Long.valueOf(10), "USD/JPY",
                BrokerTradeSide.BUY, BigDecimal.valueOf(1.25), "NOT_EXECUTED", "No available quotes",
                new Date());

        when(tradeRepository.getById(tradeId)).thenReturn(Optional.of(trade));
        when(tradeRepository.save(trade)).thenReturn(trade);

        tradeService.updateTradeStatus(UUID.fromString(tradeId));
        tradeService.updateTradeStatus(UUID.fromString(tradeId), "Time execceded");

        verify(tradeRepository, times(2)).getById(tradeId);
        verify(tradeRepository, times(2)).save(trade);
    }

    @Test
    public void test_updateIdleTrades(){

        List<Trade> trades = Stream.of(new Trade(tradeId, Long.valueOf(10), "USD/JPY",
                BrokerTradeSide.BUY, BigDecimal.valueOf(1.25), "PENDING_EXECUTION", null, new Date()))
                .collect(Collectors.toList());

        when(tradeRepository.findIdleTrades()).thenReturn(trades);
        when(tradeRepository.saveAll(trades)).thenReturn(trades);

        tradeService.updateIdleTrades();

        verify(tradeRepository, times(1)).findIdleTrades();
        verify(tradeRepository, times(1)).saveAll(trades);

    }

}
