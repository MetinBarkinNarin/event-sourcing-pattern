package com.example.microserviceeventsourcing.events.order;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OrderDeliveredEvent {

    private final String orderId;

}
