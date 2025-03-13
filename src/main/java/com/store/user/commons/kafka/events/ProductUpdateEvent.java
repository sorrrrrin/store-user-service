package com.store.user.commons.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateEvent {
    private String eventType;
    private String id;
    private String name;
    private String description;
    private String sku;
}
