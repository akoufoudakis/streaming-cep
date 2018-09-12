package com.redhat.rhte.cep.kafka.producer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

/**
 * A simple Camel REST DSL route that implements the greetings service.
 * 
 */
@Component
public class CamelRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// @formatter:off
		restConfiguration().apiContextPath("/api-doc").apiProperty("api.title", "Greeting REST API")
				.apiProperty("api.version", "1.0").apiProperty("cors", "true").apiProperty("base.path", "camel/")
				.apiProperty("api.path", "/").apiProperty("host", "")
				// .apiProperty("schemes", "")
				.apiContextRouteId("doc-api").component("servlet").bindingMode(RestBindingMode.json);

		from("timer://msgTimer?fixedRate=true&period=60").to("bean:mocCreditCardService?method=generateCreditCardTransaction")
			.routeId("ToKafka")
			.to("kafka:{{producer.topic}}"
					+"?serializerClass=com.redhat.rhte.cep.kafka.utils.serializer.JsonSerializer"
					/*+"&partitioner=com.redhat.rhte.cep.kafka.utils.CreditCardTransactionPartitioner"*/)
			.log("${body}")/*.log("${headers}")*/;

		// @formatter:on
	}

}