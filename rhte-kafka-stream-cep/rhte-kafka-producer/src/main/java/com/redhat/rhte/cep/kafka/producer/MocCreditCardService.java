package com.redhat.rhte.cep.kafka.producer;

import com.redhat.rhte.cep.kafka.model.CreditCardTransaction;

/**
 * Service interface for name service.
 * 
 */
public interface MocCreditCardService {

    /**
     * Generate welcome app messages
     *
     * @return a string messages
     */
    String generateMessage();
    
    /**
     * Generate credit card transactions
     *
     * @return a CreditCardTransaction
     */
    CreditCardTransaction generateCreditCardTransaction();

}