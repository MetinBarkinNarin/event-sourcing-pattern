package com.example.microserviceeventsourcing.events.order;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OrderCreatedEvent {

    private final String orderId;

}
