package com.client.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.broker.external.BrokerTrade;
import com.broker.external.BrokerTradeSide;
import com.broker.external.ExternalBroker;
import com.client.entity.Trade;
import com.client.exception.TradeCreationException;
import com.client.exception.TradeNotFoundException;
import com.client.pojo.StatusDTO;
import com.client.respository.TradeRepository;

/**
 * Service class to create, update or get the trades
 * Implements CommandLineRunner to initiate a TimerTask in the background
 * to update the Idle Trades.
 */
@Service
@Scope("prototype")
public class TradeService implements CommandLineRunner {

    @Autowired
    TradeRepository tradeRepository;

    /**
     * Instance for BrokerReponseCallBack
     */
    BrokerResponseCallBack brokerResponseCallBack = new BrokerResponseCallBack();

    /**
     * Instance for External Broker which accepts the trades
     */
    ExternalBroker externalBroker = new ExternalBroker(brokerResponseCallBack);

    /**
     * Trade status constants
     */
    private static final String PENDING_EXECUTION = "PENDING_EXECUTION";
    private static final String EXECUTED = "EXECUTED";
    private static final String NOT_EXECUTED = "NOT_EXECUTED";

    /**
     * Scheduler Time to run which will updated Idle trades
     * which are not updated after 2 mins
     */
    private static final int MINUTES = 180000;

    /**
     * Method to save the Trade
     * 
     * @param trade Trade
     * @param side  BrokderTradeSide BUY/SELL
     * @return newly created Trade
     */
    public Trade saveTrade(Trade trade, BrokerTradeSide side) throws TradeCreationException{
        trade.setStatus(PENDING_EXECUTION);
        trade.setSide(side);
        trade.setTimeStamp(new Date());
        Trade newTrade = tradeRepository.save(trade);
        execute(newTrade);
        return newTrade;
    }

    /**
     * Method to get the Trade by Id
     * 
     * @param id Trade Id
     * @return Trade
     * @throws TradeNotFoundException
     */
    public Trade getTrade(String id) throws TradeNotFoundException {
        Optional<Trade> optionalTrade = tradeRepository.getById(id);

        return optionalTrade.orElseThrow(() -> new TradeNotFoundException("Not Found"));
    }

    /**
     * Method to get the all trades
     * 
     * @return List of Trades
     */
    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }

    /**
     * Method to get the status of a Trade by Id
     * 
     * @param id Trade Id
     * @return StatusDTO
     */
    public StatusDTO getTradeStatus(String id) {
        Trade trade = getTrade(id);

        return new StatusDTO(trade.getStatus());
    }

    /**
     * Method to call the External Broker execute method to
     * submit the trade for execution
     * 
     * @param trade Trade
     */
    public void execute(Trade trade) {
        externalBroker.execute(
                new BrokerTrade(UUID.fromString(trade.getId()), trade.getSymbol(), trade.getQuantity(), trade.getSide(),
                        trade.getPrice()));
    }

    /**
     * Method to update the TradeStatus By Id.
     * This will be used by BrokerCallBackResponse to update the Trade after
     * successfull execution
     * 
     * @param uuid UUID
     */
    public void updateTradeStatus(UUID uuid) {
        Trade trade = getTrade(uuid.toString());
        trade.setStatus(EXECUTED);
        trade.setTimeStamp(new Date());
        tradeRepository.save(trade);
    }

    /**
     * Method to update the TradeStatus By Id with reason
     * This will be used by BrokerCallBackResponse to update the Trade after
     * Unsuccesfull execution
     * 
     * @param uuid UUID
     */
    public void updateTradeStatus(UUID uuid, String reason) {
        Trade trade = getTrade(uuid.toString());
        trade.setStatus(NOT_EXECUTED);
        trade.setTimeStamp(new Date());
        trade.setReason(reason);
        tradeRepository.save(trade);
    }

    /**
     * Method to indentify the trades which are not executed
     * fore more than 2mins.
     * 
     * @return
     */
    public List<Trade> findIdleTrades() {
        return tradeRepository.findIdleTrades();
    }

    /**
     * Method to update the Idle Trades in the Database.
     * Trades which are created but not executed by external borkder with the status
     * more than 2mins. Will be filtered from database and updated.
     * 
     */
    public void updateIdleTrades() {
        List<Trade> trades = findIdleTrades();
        if (trades != null && !trades.isEmpty() && trades.size() > 0)
            trades.forEach(t -> {
                t.setStatus(NOT_EXECUTED);
                t.setReason("trade expired");
            });
        tradeRepository.saveAll(trades);

    }

    /**
     * Run method to initiate a Timer Task to update the Idle Trades
     * Runs for Every 3 mins
     * 
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() { // Function runs every MINUTES minutes.
                updateIdleTrades();
            }
        }, MINUTES, MINUTES);

    }
}
