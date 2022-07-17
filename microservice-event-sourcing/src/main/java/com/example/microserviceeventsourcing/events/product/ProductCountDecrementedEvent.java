package com.example.microserviceeventsourcing.events.product;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductCountDecrementedEvent {

    private final String orderId;
    private final String productId;

}
