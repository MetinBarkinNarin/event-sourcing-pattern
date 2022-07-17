package com.example.microserviceeventsourcing.commandmodel;


import com.example.microserviceeventsourcing.commands.coupon.CouponCancelCommand;
import com.example.microserviceeventsourcing.commands.coupon.CouponUseCommand;
import com.example.microserviceeventsourcing.commands.order.OrderConfirmCommand;
import com.example.microserviceeventsourcing.commands.order.OrderCreateCommand;
import com.example.microserviceeventsourcing.commands.order.OrderDeliverCommand;
import com.example.microserviceeventsourcing.domain.CouponState;
import com.example.microserviceeventsourcing.events.coupon.CouponUsedEvent;
import com.example.microserviceeventsourcing.events.order.OrderConfirmedEvent;
import com.example.microserviceeventsourcing.events.order.OrderCreatedEvent;
import com.example.microserviceeventsourcing.events.order.OrderDeliveredEvent;
import com.example.microserviceeventsourcing.events.product.ProductAddedEvent;
import com.example.microserviceeventsourcing.events.product.ProductRemovedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.HashMap;
import java.util.Map;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;


@Aggregate(snapshotTriggerDefinition = "orderAggregateSnapshotTriggerDefinition")
@Slf4j
@NoArgsConstructor
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private boolean orderConfirmed;
    private CouponState couponState;

    @AggregateMember
    private Map<String, OrderDetail> orderDetails;


    @CommandHandler
    public OrderAggregate(OrderCreateCommand command) {
        apply(new OrderCreatedEvent(command.getOrderId()));
    }

    @CommandHandler
    public void handle(OrderConfirmCommand command) {
        if (orderConfirmed) {
            return;
        }

        apply(new OrderConfirmedEvent(orderId));
    }

    @CommandHandler
    public void handle(OrderDeliverCommand command) {
        if (!orderConfirmed) {
            log.error("UnconfirmedOrderException");
        }
        apply(new OrderDeliveredEvent(orderId));
    }

    @CommandHandler
    public void handle(CouponUseCommand command) {
        if (orderConfirmed) {
            //throw new OrderAlreadyConfirmedException(command.getOrderId());
        }

        apply(new CouponUsedEvent(command.getOrderId(), command.getCouponId(), command.getDiscount()));
    }

    @CommandHandler
    public void handle(CouponCancelCommand command) {
        if (orderConfirmed) {
            //throw new OrderAlreadyConfirmedException(command.getOrderId());
        }

        apply(new CouponUsedEvent(command.getOrderId(), command.getCouponId(), command.getDiscount()));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.orderConfirmed = false;
        this.orderDetails = new HashMap<>();
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {

        this.orderConfirmed = true;
    }

    @EventSourcingHandler
    public void on(CouponUsedEvent event) {
        this.couponState = CouponState.USED;
    }

    @EventSourcingHandler
    public void on(CouponCancelCommand event) {
        this.couponState = CouponState.CANCELLED;
    }

    @EventSourcingHandler
    public void on(ProductAddedEvent event) {
        String productId = event.getProductId();
        this.orderDetails.put(productId, new OrderDetail(productId));
    }

    @EventSourcingHandler
    public void on(ProductRemovedEvent event) {
        this.orderDetails.remove(event.getProductId());
    }
}
