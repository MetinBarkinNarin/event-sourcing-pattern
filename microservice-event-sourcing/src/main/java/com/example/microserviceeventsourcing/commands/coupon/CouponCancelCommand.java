package com.example.microserviceeventsourcing.commands.coupon;

import com.example.microserviceeventsourcing.domain.CouponState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CouponCancelCommand {

    @TargetAggregateIdentifier
    private final String orderId;
    private final String couponId;
    private final Double discount;
}
