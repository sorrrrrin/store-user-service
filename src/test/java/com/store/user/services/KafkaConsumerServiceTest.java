package com.store.user.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.store.user.commons.kafka.events.ProductUpdateEvent;
import com.store.user.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Test
    void consumeProductUpdateEventTest() throws JsonProcessingException {
        ProductUpdateEvent productUpdateEvent = new ProductUpdateEvent();
        productUpdateEvent.setId("123");
        productUpdateEvent.setEventType(Constants.EVENT_TYPE_PRODUCT_UPDATED);

        ObjectMapper tempObjectgMapper = new ObjectMapper();

        String event = tempObjectgMapper.writeValueAsString(productUpdateEvent);
        JsonNode jsonNode = tempObjectgMapper.readTree(event);

        when(objectMapper.readTree(event)).thenReturn(jsonNode);
        when(objectMapper.readValue(event, ProductUpdateEvent.class)).thenReturn(productUpdateEvent);

        kafkaConsumerService.consume(event);

        verify(objectMapper, times(1)).readValue(anyString(), eq(ProductUpdateEvent.class));
    }

    @Test
    void consumeUnknownEventTest() throws JsonProcessingException {
        ProductUpdateEvent productUpdateEvent = new ProductUpdateEvent();
        productUpdateEvent.setId("123");
        productUpdateEvent.setEventType("unknown");

        String event = new ObjectMapper().writeValueAsString(productUpdateEvent);
        JsonNode jsonNode = new ObjectMapper().readTree(event);

        when(objectMapper.readTree(event)).thenReturn(jsonNode);

        kafkaConsumerService.consume(event);

        verify(objectMapper, times(0)).readValue(anyString(), eq(ProductUpdateEvent.class));
    }

    @Test
    void consumeInvalidEventTest() throws JsonProcessingException {
        ProductUpdateEvent productUpdateEvent = new ProductUpdateEvent();
        productUpdateEvent.setId("123");

        String event = new ObjectMapper().writeValueAsString(productUpdateEvent);

        when(objectMapper.readTree(event)).thenThrow(new JsonProcessingException("Invalid JSON") {});

        kafkaConsumerService.consume(event);

        verify(objectMapper, times(0)).readValue(anyString(), eq(ProductUpdateEvent.class));
    }

    @Test
    void consumeNoEventTest() throws JsonProcessingException {
        ProductUpdateEvent productUpdateEvent = new ProductUpdateEvent();
        productUpdateEvent.setId("123");

        String event = new ObjectMapper().writeValueAsString(productUpdateEvent);
        JsonNode jsonNode = new ObjectMapper().readTree(event);
        if (jsonNode.isObject()) {
            ((ObjectNode) jsonNode).remove("eventType");
        }

        when(objectMapper.readTree(event)).thenReturn(jsonNode);

        kafkaConsumerService.consume(event);

        verify(objectMapper, times(0)).readValue(anyString(), eq(ProductUpdateEvent.class));
    }
}