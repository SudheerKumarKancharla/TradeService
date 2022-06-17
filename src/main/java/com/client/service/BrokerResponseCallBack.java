package com.client.service;

import java.util.UUID;

import com.broker.external.BrokerResponseCallback;

/**
 * CallBack class to handle the response came from External Broker
 */
public class BrokerResponseCallBack implements BrokerResponseCallback {

    @Override
    public void successful(UUID tradeId) {

        TradeService tradeService = ApplicationContextProvider.getApplicationContext().getBean(TradeService.class);

        tradeService.updateTradeStatus(tradeId);

    }

    @Override
    public void unsuccessful(UUID tradeId, String reason) {

        TradeService tradeService = ApplicationContextProvider.getApplicationContext().getBean(TradeService.class);

        tradeService.updateTradeStatus(tradeId, reason);
    }

}
