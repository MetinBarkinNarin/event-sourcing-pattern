package com.example.microserviceeventsourcing.commandmodel;

import com.example.microserviceeventsourcing.commands.coupon.CouponCancelCommand;
import com.example.microserviceeventsourcing.commands.coupon.CouponUseCommand;
import com.example.microserviceeventsourcing.commands.product.ProductDecrementCountCommand;
import com.example.microserviceeventsourcing.commands.product.ProductIncrementCountCommand;
import com.example.microserviceeventsourcing.domain.CouponState;
import com.example.microserviceeventsourcing.events.coupon.CouponUsedEvent;
import com.example.microserviceeventsourcing.events.order.OrderConfirmedEvent;
import com.example.microserviceeventsourcing.events.product.ProductCountDecrementedEvent;
import com.example.microserviceeventsourcing.events.product.ProductCountIncrementedEvent;
import com.example.microserviceeventsourcing.events.product.ProductRemovedEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.EntityId;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Getter
@Setter
@ToString

public class OrderDetail {

    @EntityId
    private final String productId;
    private Integer count;
    private boolean orderConfirmed;

    public OrderDetail(String productId) {
        this.productId = productId;
        this.count = 1;
    }

    @CommandHandler
    public void handle(ProductIncrementCountCommand command) {
        if (orderConfirmed) {
            //throw new OrderAlreadyConfirmedException(command.getOrderId());
        }

        apply(new ProductCountIncrementedEvent(command.getOrderId(), productId));
    }

    @CommandHandler
    public void handle(ProductDecrementCountCommand command) {
        if (orderConfirmed) {
            //throw new OrderAlreadyConfirmedException(command.getOrderId());
        }

        if (count <= 1) {
            apply(new ProductRemovedEvent(command.getOrderId(), productId));
        } else {
            apply(new ProductCountDecrementedEvent(command.getOrderId(), productId));
        }
    }

    @EventSourcingHandler
    public void on(ProductCountIncrementedEvent event) {
        this.count++;
    }

    @EventSourcingHandler
    public void on(ProductCountDecrementedEvent event) {
        this.count--;
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        this.orderConfirmed = true;
    }

}
