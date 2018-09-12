package com.redhat.rhte.cep.kafka.consumer.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.StateStoreSupplier;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

import com.redhat.rhte.cep.kafka.model.CreditCardTransaction;
import com.redhat.rhte.cep.kafka.model.CreditCardTransactionAccumulator;
import com.redhat.rhte.cep.kafka.utils.CreditCardAccumaltorTransformer;
import com.redhat.rhte.cep.kafka.utils.CreditCardTransactionPartitioner;
import com.redhat.rhte.cep.kafka.utils.StreamsSerdes;
import com.redhat.rhte.cep.kafka.utils.TransactionPatterns;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KStreamsConfig {

	private static String ccTransactionsStateStoreName = "ccTransactionsStore";

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${kafka.topic}")
	private String kafkaTopic;

	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public StreamsConfig kStreamsConfigs() {
		Map<String, Object> props = new HashMap<String, Object>();

		props.put(StreamsConfig.CLIENT_ID_CONFIG, "AddingStateConsumer");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "AddingStateGroupId");
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "AddingStateAppId");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				StreamsSerdes.CreditCardTransactionSerde().getClass().getName());
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
				StreamsSerdes.CreditCardTransactionSerde().getClass().getName());

		props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);

		/*
		 * props.put(StreamsConfig.APPLICATION_ID_CONFIG, "join_driver_application");
		 * props.put(ConsumerConfig.GROUP_ID_CONFIG, "join_driver_group");
		 * props.put(ConsumerConfig.CLIENT_ID_CONFIG, "join_driver_client");
		 * props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		 * props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");
		 * props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		 * props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "1");
		 * props.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "10000");
		 * props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
		 * "org.apache.kafka.common.serialization.StringDeserializer");
		 * props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
		 * "org.apache.kafka.common.serialization.StringDeserializer");
		 * props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
		 * props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
		 * TransactionTimestampExtractor.class)
		 */
		System.out.println("asdasdasasdsadsadsadsada - 1");

		return new StreamsConfig(props);
	}

	@Bean
	public KStream<?, ?> processStream(KStreamBuilder streamBuilder) {
		System.out.println("asdasdasasdsadsadsadsada - 3");

		KStream<String, CreditCardTransaction> stream = streamBuilder.stream(kafkaTopic);
		
		// STATELESS RULE to filter on banned countries (implicit new stream) followed by filtering on Invalid Hour of Day (another implicit new stream) 
		KStream<String, CreditCardTransaction> filteredStream = stream.filter(TransactionPatterns.bannedCountries).filter(TransactionPatterns.bannedCountries).filter(TransactionPatterns.InvalidHourOfDay);

		//		filteredStream.print(Printed.<String, CreditCardTransaction>toSysOut().withLabel("filteredCCTrans"));
		// SINK (EXPLICIT) ALL found ILLEGAL transactions to a new TOPIC
		filteredStream.to("illegal-trans");

		
		CreditCardTransactionPartitioner streamPartitioner = new CreditCardTransactionPartitioner();
		//streamBuilder.addStateStore(storeBuilder);
		
		// create store
		StateStoreSupplier<KeyValueStore<String, Integer>>  storeSupplier =Stores.create(ccTransactionsStateStoreName)
		    .withKeys(Serdes.String())
		    .withValues(Serdes.Integer())
		    .persistent()
		    //.windowed(windowSize, retentionPeriod, numSegments, retainDuplicates)
		    .build();
			     
		
		// register store
		streamBuilder.addStateStore(storeSupplier);

		// SINK (EXPLICIT) ALL found ILLEGAL transactions to a new TOPIC partitioned by Credit Card ID (so all single credit card transations found in the same partition)
		//KStream<String, CreditCardTransaction> transByCreditCard = stream.through("by-cc-trans", Produced.with(Serdes.String(), StreamsSerdes.CreditCardTransactionSerde(), streamPartitioner));
		KStream<String, CreditCardTransaction> transByCreditCard = filteredStream.through("by-cc-trans", Produced.with(Serdes.String(), StreamsSerdes.CreditCardTransactionSerde(), streamPartitioner));
		
		transByCreditCard.print(Printed.<String, CreditCardTransaction>toSysOut().withLabel("by-cc-trans"));

		
		KStream<String, CreditCardTransactionAccumulator> statefulRewardAccumulator = transByCreditCard.transformValues(() -> new CreditCardAccumaltorTransformer(ccTransactionsStateStoreName), ccTransactionsStateStoreName);

		statefulRewardAccumulator.print(Printed.<String, CreditCardTransactionAccumulator>toSysOut().withLabel("ccTransactionsAccumulator"));
		
		/*statefulRewardAccumulator.to("illegal-trans",
				Produced.with(Serdes.String(), StreamsSerdes.CreditCardTransactionAccumulatorSerde()));*/


		return stream;
	}

	/*
	 * @Bean
	 * 
	 * @Autowired
	 * 
	 * @Qualifier("originatingCountryStream") public KStream<?, ?>
	 * timeOfTransactionStream(KStream<String, CreditCardTransaction> kStream,
	 * StreamsBuilder streamBuilder) {
	 * 
	 * KStream<String, CreditCardTransaction> stream =
	 * kStream.filter(TransactionPatterns.InvalidHourOfDay);
	 * 
	 * return stream; }
	 * 
	 * @Bean public StoreBuilder ccTransactionsStore() {
	 * 
	 * KeyValueBytesStoreSupplier storeSupplier =
	 * Stores.inMemoryKeyValueStore(ccTransactionsStateStoreName);
	 * StoreBuilder<KeyValueStore<String, Integer>> storeBuilder =
	 * Stores.keyValueStoreBuilder(storeSupplier, Serdes.String(),
	 * Serdes.Integer()); return storeBuilder; }
	 * 
	 * @Bean
	 * 
	 * @Autowired
	 * 
	 * @Qualifier("timeOfTransactionStream") public KStream<?, ?> xx(KStream stream,
	 * StreamsBuilder streamBuilder, StoreBuilder storeBuilder) { // adding State to
	 * processor
	 * 
	 * CreditCardTransactionPartitioner streamPartitioner = new
	 * CreditCardTransactionPartitioner();
	 * 
	 * streamBuilder.addStateStore(storeBuilder);
	 * 
	 * KStream<String, CreditCardTransaction> transByCreditCard =
	 * stream.through("customer_transactions", Produced.with(Serdes.String(),
	 * StreamsSerdes.CreditCardTransactionSerde(), streamPartitioner));
	 * 
	 * KStream<String, CreditCardTransactionAccumulator> statefulRewardAccumulator =
	 * transByCreditCard .transformValues(() -> new
	 * CreditCardAccumaltorTransformer(ccTransactionsStateStoreName),
	 * ccTransactionsStateStoreName);
	 * 
	 * statefulRewardAccumulator.print(Printed.<String,
	 * CreditCardTransactionAccumulator>toSysOut().withLabel(
	 * "ccTransactionsAccumulator")); statefulRewardAccumulator.to("rewards",
	 * Produced.with(Serdes.String(),
	 * StreamsSerdes.CreditCardTransactionAccumulatorSerde()));
	 * 
	 * return stream; }
	 */

}