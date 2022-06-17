# TradeService
Spring Boot microservice that will expose a set of  REST APIs for submitting trade requests as well as retrieving details about the statuses of the  requests

# Technologies
Java11, JUnit 4.13, Mokito, H2 

# Instructions to Build the Project
- mvn clean install

# Execution
> Navigate to <floder.zip>/target folder
> java -jar tradeservice-1.0.jar


Submit Buy/Sell trade request
-----

APIs for submitting a buy or sell trade request

  
```
Sample Request: POST http://{HOST}:{PORT}/api/buy
{
 "symbol": "EUR/USD",
 "quantity": 1000,
 "price": 1.123
}
Sample response:
HTTP Status: 201 - CREATED
Location: http://localhost:8080/api/trades/2b42f60f-c794-43d8-b4a3-da709f3d2fa6/status
```

```
Sample Request: POST http://{HOST}:{PORT}/api/sell
{
 "symbol": "EUR/USD",
 "quantity": 1000,
 "price": 1.123
}
Sample response:
HTTP Status: 201 - CREATED
Location: http://localhost:8080/api/trades/2b42f60f-c794-43d8-b4a3-da709f3d2fa6/status
```

Get trade status
-----

API for retrieving the trade status based on the id.

```
Sample Request: GET http://{HOST}:{PORT}/api/trades/{tradeId}/status
Sample response:
HTTP Status: 200 -OK
{
 "status": "NOT_EXECUTED"
}
```

Get trade details
-----

API for retrieving trade details by id

```
Sample Request: GET http://{HOST}:{PORT}/api/trades/{tradeId}
<tradeId> : TradeId
Sample response:
HTTP Status: 200 -OK
{
 "id": "2b42f60f-c794-43d8-b4a3-da709f3d2fa6",
 "quantity": 1000,
 "symbol": "EUR/USD",
 "side": "BUY",
 "price": 1.123,
 "status": "PENDING_EXECUTION",
 "reason": null,
 "timestamp": "2022-02-14T17:55:05.68645"
}
```

Get all trades
-----

API for retrieving all trades

```
Sample Request: GET http://{HOST}:{PORT}/api/trades
Sample response:
HTTP Status: 200 -OK
[
{
 "id": "2b42f60f-c794-43d8-b4a3-da709f3d2fa6",
 "quantity": 1000,
 "symbol": "EUR/USD",
 "side": "BUY",
 "price": 1.123,
 "status": "PENDING_EXECUTION",
 "reason": null,
 "timestamp": "2022-02-14T17:55:05.68645"
}, 
{
 "id": "e22ea403-79cc-403f-869d-8f3481e8582d",
 "quantity": 10000000,
 "symbol": "USD/JPY",
 "side": "SELL",
 "price": 1.00,
 "status": "NOT_EXECUTED",
 "reason": "No available quotes",
 "timestamp": "2022-02-14T17:22:52.27468"
 }
 ]
```

