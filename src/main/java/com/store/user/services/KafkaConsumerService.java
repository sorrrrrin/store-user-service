package com.store.user.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.user.commons.kafka.events.ProductUpdateEvent;
import com.store.user.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Profile("kafka-enabled")
@Slf4j
public class KafkaConsumerService {
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String event) throws JsonProcessingException {
        try {
            JsonNode rootNode = objectMapper.readTree(event);

            if (!rootNode.has("eventType")) {
                log.debug("Event unknown for message: {}", event);
                return;
            }

            String eventType = rootNode.get("eventType").asText();

            switch (eventType) {
                case Constants.EVENT_TYPE_PRODUCT_UPDATED:
                    ProductUpdateEvent productUpdateEvent = objectMapper.readValue(event, ProductUpdateEvent.class);
                    log.debug("Product updated: {}", productUpdateEvent);
                    break;
                default:
                    log.debug("Unknown event type for message: {}", event);
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing event: {}", event);
            return;
        }


    }
}