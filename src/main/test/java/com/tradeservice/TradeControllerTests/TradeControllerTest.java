package com.tradeservice.TradeControllerTests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.broker.external.BrokerTradeSide;
import com.client.controller.TradeController;
import com.client.entity.Trade;
import com.client.exception.TradeCreationException;
import com.client.pojo.StatusDTO;
import com.client.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for TradeController
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class TradeControllerTest {

    @InjectMocks
    TradeController tradeController;

    @MockBean
    TradeService tradeService;

    @Autowired
    private MockMvc mockMvc;

    private String tradeId = "5d9242c0-6f17-461b-96cc-34442bb8b249";

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Test case for end point "/api/sell"
     * SELL Trade creation
     * 
     * @throws Exception
     */
    @Test
    public void test_createSellTrade() throws Exception {

        Trade trade = new Trade(tradeId, Long.valueOf(10), "USD/JPY",
                BrokerTradeSide.SELL, BigDecimal.valueOf(1.25), "PENDING_FOR_EXECUTION", null,
                new Date());

        when(tradeService.saveTrade(trade, BrokerTradeSide.SELL)).thenReturn(trade);

        String json = mapper.writeValueAsString(trade);
        MvcResult mvcResult = mockMvc
                .perform(post("/api/sell").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(mvcResult.getResponse().getHeader("location").length() > 0);
        assertTrue(mvcResult.getResponse().getHeader("location").contains(tradeId));

    }

    /**
     * Test case for end point "/api/buy"
     * BUY Trade creation
     * 
     * @throws Exception
     */
    @Test
    public void test_createBuyTrade() throws Exception {

        Trade trade = new Trade(tradeId, Long.valueOf(15), "EUR/USD",
                BrokerTradeSide.BUY, BigDecimal.valueOf(2.25), "PENDING_FOR_EXECUTION", null,
                new Date());

        when(tradeService.saveTrade(trade, BrokerTradeSide.BUY)).thenReturn(trade);

        String json = mapper.writeValueAsString(trade);
        MvcResult mvcResult = mockMvc
                .perform(post("/api/buy").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(mvcResult.getResponse().getHeader("location").length() > 0);
        assertTrue(mvcResult.getResponse().getHeader("location").contains(tradeId));
    }

    /**
     * Test case for end point "/api/trades/{tradeId}"
     * 
     * @throws Exception
     */
    @Test
    public void test_getTrade() throws Exception {

        Trade trade = new Trade(tradeId, Long.valueOf(15), "EUR/USD",
                BrokerTradeSide.BUY, BigDecimal.valueOf(2.25), "PENDING_FOR_EXECUTION", null,
                new Date());

        when(tradeService.getTrade(tradeId)).thenReturn(trade);

        mockMvc.perform(get("/api/trades/" + tradeId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(tradeId)));
    }

    /**
     * Test case for end point "/api/trades"
     * 
     * @throws Exception
     */

    @Test
    public void test_getAllTrades() throws Exception {
        List<Trade> trades = Stream.of(new Trade(tradeId, Long.valueOf(10), "USD/JPY",
                BrokerTradeSide.BUY, BigDecimal.valueOf(1.25), "PENDING_EXECUTION", null, new Date()))
                .collect(Collectors.toList());

        when(tradeService.getAllTrades()).thenReturn(trades);

        mockMvc.perform(get("/api/trades")).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(tradeId)));
    }

    /**
     * Test case for end point "/api/trades/{tradeId}/status"
     * 
     * @throws Exception
     */
    @Test
    public void test_getTradeStatus() throws Exception {
        StatusDTO statusDTO = new StatusDTO("NOT_EXECUTED");

        when(tradeService.getTradeStatus(tradeId)).thenReturn(statusDTO);

        mockMvc.perform(get("/api/trades/" + tradeId + "/status")).andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.equalTo("NOT_EXECUTED")));
    }

    /**
     * Test case for Trade Quantity validation
     * 
     * @throws Exception
     */
    @Test
    public void test_tradeValidation_forQuantity() throws Exception {
        Trade trade = new Trade();
        trade.setSymbol("USD/JPY");
        trade.setPrice(BigDecimal.valueOf(1.25));
        trade.setQuantity(0);

        when(tradeService.saveTrade(trade, BrokerTradeSide.BUY)).thenReturn(trade);

        String json = mapper.writeValueAsString(trade);
        mockMvc.perform(post("/api/buy").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath(
                        "$.message", Matchers.equalTo("quantity must be greater than 0 and less than or equal to 1M")));
    }

    /**
     * Test case for Trade Price validation
     * 
     * @throws Exception
     */
    @Test
    public void test_tradeValidation_forPrice() throws Exception {
        Trade trade = new Trade();
        trade.setSymbol("USD/JPY");
        trade.setPrice(BigDecimal.valueOf(0));
        trade.setQuantity(10);

        when(tradeService.saveTrade(trade, BrokerTradeSide.BUY)).thenReturn(trade);

        String json = mapper.writeValueAsString(trade);
        mockMvc.perform(post("/api/buy").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath(
                        "$.message", Matchers.equalTo("price must be greater than 0")));
    }

    /**
     * Test case for Trade Symbol validation
     * 
     * @throws Exception
     */
    @Test
    public void test_tradeValidation_forSymbol() throws Exception {
        Trade trade = new Trade();
        trade.setSymbol("RUPEE/EURO");
        trade.setPrice(BigDecimal.valueOf(2.25));
        trade.setQuantity(10);

        when(tradeService.saveTrade(trade, BrokerTradeSide.BUY)).thenReturn(trade);

        String json = mapper.writeValueAsString(trade);
        mockMvc.perform(post("/api/buy").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath(
                        "$.message", Matchers.equalTo("symbol valid values: USD/JPY, EUR/USD")));
    }

}
