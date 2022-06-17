package com.tradeservice.TradeServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.client.service.IdGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IdGenerateTest {

    private String tradeId = "5d9242c0-6f17-461b-96cc-34442bb8b249";

    @Test
    public void test_idGeneration() {
        IdGenerator idGenerator = mock(IdGenerator.class);
        SharedSessionContractImplementor sharedSessionContractImplementor = null;
        Object o = null;
        when(idGenerator.generate(sharedSessionContractImplementor, o)).thenReturn(tradeId);

        assertEquals(tradeId, idGenerator.generate(sharedSessionContractImplementor, o));
    }
}
