package com.example.microserviceeventsourcing.commands.product;

import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductAddCommand {

    @TargetAggregateIdentifier
    private final String orderId;
    private final String productId;

}
