package com.client.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.client.entity.Trade;

/**
 * Repository class to handle the DB requests
 */
public interface TradeRepository extends JpaRepository<Trade, Long> {

    Optional<Trade> getById(String id);

    List<Trade> findAll();

    @Query(value = "SELECT * FROM TRADE WHERE STATUS = 'PENDING_EXECUTION' AND TIMESTAMPDIFF(MINUTE, TIME_STAMP ,NOW())> 2", nativeQuery = true)
    List<Trade> findIdleTrades();

}
