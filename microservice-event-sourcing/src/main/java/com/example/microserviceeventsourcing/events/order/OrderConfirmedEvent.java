package com.example.microserviceeventsourcing.events.order;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OrderConfirmedEvent {

    private final String orderId;
}
