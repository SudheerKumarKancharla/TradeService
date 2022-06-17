package com.client.controller;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.broker.external.BrokerTradeSide;
import com.client.entity.Trade;
import com.client.exception.TradeCreationException;
import com.client.exception.TradeNotFoundException;
import com.client.pojo.StatusDTO;
import com.client.service.TradeService;

/**
 * Controller class to handle the incoming requests
 */
@RestController
public class TradeController {

    @Autowired
    private TradeService tradeService;

    private static final Logger logger = LogManager.getLogger(TradeController.class);

    /**
     * End point to create the trade type of SELL
     * 
     * @param trade    Inputs from request Body
     * @param response HttpServletResponse 
     * @throws TradeCreationException
     */
    @PostMapping("/api/sell")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createSellTrade(@RequestBody @Valid Trade trade, HttpServletResponse response)
            throws TradeCreationException {

        try {
            trade = tradeService.saveTrade(trade, BrokerTradeSide.SELL);
            final URI location = MvcUriComponentsBuilder.fromController(getClass())
                    .path("/api/trades/{tradeId}/status")
                    .buildAndExpand(trade.getId())
                    .toUri();

            response.setHeader("location", location.toString());
        } catch (Exception e) {
            logger.info("Exception while saving the trade for sell :" + e.getMessage());
            throw new TradeCreationException(e.getMessage());
        }

    }

    /**
     * End point to create trade of type BUY
     * 
     * @param trade    Inputs from request Body
     * @param response HttpServletResponse
     * @throws TradeCreationException
     */
    @PostMapping("/api/buy")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createBuyTrade(@RequestBody @Valid Trade trade, HttpServletResponse response)
            throws TradeCreationException {

        try {
            trade = tradeService.saveTrade(trade, BrokerTradeSide.BUY);
            final URI location = MvcUriComponentsBuilder.fromController(getClass())
                    .path("/api/trades/{tradeId}/status")
                    .buildAndExpand(trade.getId())
                    .toUri();

            response.setHeader("location", location.toString());
        } catch (Exception e) {
            logger.info("Exception while saving the trade for sell :" + e.getMessage());
            throw new TradeCreationException(e.getMessage());
        }

    }

    /**
     * End Point to Get the Trade based on Id
     * 
     * @param tradeId UUID
     * @return Trade Object
     * @throws TradeNotFoundException
     */
    @GetMapping("/api/trades/{tradeId}")
    public Trade getTrade(@PathVariable String tradeId) throws TradeNotFoundException {
        return tradeService.getTrade(tradeId);
    }

    /**
     * End Point to Get all available Trades
     * 
     * @return List of Trades
     */
    @GetMapping("/api/trades")
    public List<Trade> getAllTrades() {
        return tradeService.getAllTrades();
    }

    /**
     * End Point to Get Trade Status
     * 
     * @param tradeId UUID
     * @return StatusDTO which contains the status as
     *         "NOT_EXECUTED/EXECUTED/PENDING_EXECUTION"
     * @throws TradeNotFoundException
     */
    @GetMapping("/api/trades/{tradeId}/status")
    public StatusDTO getTradeStatus(@PathVariable String tradeId) throws TradeNotFoundException {
        return tradeService.getTradeStatus(tradeId);
    }

}
