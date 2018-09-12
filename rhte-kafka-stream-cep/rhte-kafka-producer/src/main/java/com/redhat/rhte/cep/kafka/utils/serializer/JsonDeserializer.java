package com.redhat.rhte.cep.kafka.utils.serializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class JsonDeserializer<T> implements Deserializer<T> {

	protected final ObjectMapper objectMapper;

	protected final Class<T> targetType;

	private volatile ObjectReader reader;

	protected JsonDeserializer() {
		this((Class<T>) null);
	}

	protected JsonDeserializer(ObjectMapper objectMapper) {
		this(null, objectMapper);
	}

	public JsonDeserializer(Class<T> targetType) {
		this(targetType, new ObjectMapper());
		this.objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@SuppressWarnings("unchecked")
	public JsonDeserializer(Class<T> targetType, ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "'objectMapper' must not be null.");
		this.objectMapper = objectMapper;
		if (targetType == null) {
			targetType = (Class<T>) ResolvableType.forClass(getClass()).getSuperType().resolveGeneric(0);
		}
		Assert.notNull(targetType, "'targetType' cannot be resolved.");
		this.targetType = targetType;
	}

	public void configure(Map<String, ?> configs, boolean isKey) {
		// No-op
	}

	public T deserialize(String topic, byte[] data) {
		if (data == null) {
			return null;
		}
		if (this.reader == null) {
			this.reader = this.objectMapper.readerFor(this.targetType);
		}
		try {
			T result = null;
			if (data != null) {
				result = this.reader.readValue(data);
			}
			return result;
		} catch (IOException e) {
			throw new SerializationException(
					"Can't deserialize data [" + Arrays.toString(data) + "] from topic [" + topic + "]", e);
		}
	}

	public void close() {
		// No-op
	}

}
