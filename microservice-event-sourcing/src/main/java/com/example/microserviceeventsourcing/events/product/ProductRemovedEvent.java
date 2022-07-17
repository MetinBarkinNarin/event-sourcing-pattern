package com.example.microserviceeventsourcing.events.product;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductRemovedEvent {

    private final String orderId;
    private final String productId;

}
