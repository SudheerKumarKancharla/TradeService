package com.client.service;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * Generator class to generate the Unique UUID
 * for the created trade.
 */
public class IdGenerator implements IdentifierGenerator {

    public static final String generatorName = "idGenerator";

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object)
            throws HibernateException {
        return UUID.randomUUID().toString();
    }
}
