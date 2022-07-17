package com.example.microserviceeventsourcing.events.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CouponCanceledEvent {

    private final String orderId;
    private final String couponId;
    private final Double discount;
}
