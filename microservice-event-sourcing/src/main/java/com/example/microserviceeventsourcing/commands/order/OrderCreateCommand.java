package com.example.microserviceeventsourcing.commands.order;

import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OrderCreateCommand {

    @TargetAggregateIdentifier
    private final String orderId;
}
