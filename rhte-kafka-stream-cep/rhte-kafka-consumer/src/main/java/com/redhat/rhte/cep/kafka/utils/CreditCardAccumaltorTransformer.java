package com.redhat.rhte.cep.kafka.utils;

import java.util.Objects;

import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import com.redhat.rhte.cep.kafka.model.CreditCardTransaction;
import com.redhat.rhte.cep.kafka.model.CreditCardTransactionAccumulator;

public class CreditCardAccumaltorTransformer implements ValueTransformer<CreditCardTransaction, CreditCardTransactionAccumulator> {

    private KeyValueStore<String, Integer> stateStore;
    private String storeName;
    private ProcessorContext context;

    public CreditCardAccumaltorTransformer(String storeName) {
        Objects.requireNonNull(storeName,"Store Name can't be null");
        this.storeName = storeName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        this.context = context;
        stateStore = (KeyValueStore) this.context.getStateStore(storeName);
    }
    
    @Override
    public CreditCardTransactionAccumulator transform(CreditCardTransaction value) {
    	CreditCardTransactionAccumulator rewardAccumulator = CreditCardTransactionAccumulator.from(value);

    	System.out.println("Accummulating credit card ID ["+value.getCreditCardId()+"]");
    	
    	Integer accumulatedSoFar = stateStore.get(rewardAccumulator.getCreditCardId());

        if (accumulatedSoFar != null) {
             rewardAccumulator.addTransactions(accumulatedSoFar+1);
        }
        
    	System.out.println("accumulatedSoFar["+rewardAccumulator.getCreditCardId()+"] = "+accumulatedSoFar+" ("+value.getCreditCardId()+")");

        stateStore.put(rewardAccumulator.getCreditCardId(), rewardAccumulator.getTotalNumberOfTransactions());

        return rewardAccumulator;

    }
    

    @Override
    @SuppressWarnings("deprecation")
    public CreditCardTransactionAccumulator punctuate(long timestamp) {
        return null;  //no-op null values not forwarded.
    }

    @Override
    public void close() {
        //no-op
    }
}
