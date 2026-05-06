package org.com.lms_be.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.util.Optional;

/**
 * Jackson deserializer that distinguishes between:
 * - Field absent from JSON → Optional field stays null
 * - Field explicitly set to null → Optional.empty()
 * - Field sent with a value → Optional.of(value)
 */
public class OptionalFieldDeserializer extends ValueDeserializer<Optional<?>> {

    private final Class<?> valueType;

    public OptionalFieldDeserializer() {
        this.valueType = Object.class;
    }

    public OptionalFieldDeserializer(Class<?> valueType) {
        this.valueType = valueType;
    }

    @Override
    public Optional<?> deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        Object value = ctxt.readValue(p, valueType);
        return Optional.ofNullable(value);
    }

    @Override
    public Object getNullValue(DeserializationContext ctxt) {
        return Optional.empty();
    }

    @Override
    public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        Class<?> containedType = property.getType().containedType(0).getRawClass();
        return new OptionalFieldDeserializer(containedType);
    }
}
