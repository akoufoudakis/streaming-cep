package com.redhat.rhte.cep.kafka.utils;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.redhat.rhte.cep.kafka.model.CreditCardTransaction;
import com.redhat.rhte.cep.kafka.utils.serializer.JsonDeserializer;
import com.redhat.rhte.cep.kafka.utils.serializer.JsonSerializer;

public class StreamsSerdes {

	public static Serde<CreditCardTransaction> CreditCardTransactionSerde() {
		return new CreditCardTransactionSerde();
	}

	public static final class CreditCardTransactionSerde extends WrapperSerde<CreditCardTransaction> {
		public CreditCardTransactionSerde() {
			super(new JsonSerializer<>(), new JsonDeserializer<>(CreditCardTransaction.class));
		}

	}

	private static class WrapperSerde<T> implements Serde<T> {

		private JsonSerializer<T> serializer;
		private JsonDeserializer<T> deserializer;

		WrapperSerde(JsonSerializer<T> serializer, JsonDeserializer<T> deserializer) {
			this.serializer = serializer;
			this.deserializer = deserializer;
		}

		@Override
		public void configure(Map<String, ?> map, boolean b) {

		}

		@Override
		public void close() {

		}

		@Override
		public Serializer<T> serializer() {
			return serializer;
		}

		@Override
		public Deserializer<T> deserializer() {
			return deserializer;
		}
	}
}