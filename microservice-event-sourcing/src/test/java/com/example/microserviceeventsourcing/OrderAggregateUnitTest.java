package com.example.microserviceeventsourcing;


import com.example.microserviceeventsourcing.commandmodel.OrderAggregate;
import com.example.microserviceeventsourcing.commands.order.OrderConfirmCommand;
import com.example.microserviceeventsourcing.commands.order.OrderCreateCommand;
import com.example.microserviceeventsourcing.commands.order.OrderDeliverCommand;
import com.example.microserviceeventsourcing.commands.product.ProductAddCommand;
import com.example.microserviceeventsourcing.commands.product.ProductDecrementCountCommand;
import com.example.microserviceeventsourcing.commands.product.ProductIncrementCountCommand;
import com.example.microserviceeventsourcing.events.order.OrderConfirmedEvent;
import com.example.microserviceeventsourcing.events.order.OrderCreatedEvent;
import com.example.microserviceeventsourcing.events.order.OrderDeliveredEvent;
import com.example.microserviceeventsourcing.events.product.ProductAddedEvent;
import com.example.microserviceeventsourcing.events.product.ProductCountDecrementedEvent;
import com.example.microserviceeventsourcing.events.product.ProductCountIncrementedEvent;
import com.example.microserviceeventsourcing.events.product.ProductRemovedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class OrderAggregateUnitTest {

    private static final String ORDER_ID = UUID.randomUUID().toString();
    private static final String PRODUCT_ID = UUID.randomUUID().toString();

    private FixtureConfiguration<OrderAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(OrderAggregate.class);
    }

    @Test
    void giveNoPriorActivity_whenCreateOrderCommand_thenShouldPublishOrderCreatedEvent() {
        fixture.givenNoPriorActivity()
               .when(new OrderCreateCommand(ORDER_ID))
               .expectEvents(new OrderCreatedEvent(ORDER_ID));
    }

    @Test
    void givenOrderCreatedEvent_whenAddProductCommand_thenShouldPublishProductAddedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID))
               .when(new ProductAddCommand(ORDER_ID, PRODUCT_ID))
               .expectEvents(new ProductAddedEvent(ORDER_ID, PRODUCT_ID));
    }

//    @Test
//    void givenOrderCreatedEventAndProductAddedEvent_whenAddProductCommandForSameProductId_thenShouldThrowDuplicateOrderLineException() {
//        fixture.given(new OrderCreatedEvent(ORDER_ID), new ProductAddedEvent(ORDER_ID, PRODUCT_ID))
//               .when(new ProductAddCommand(ORDER_ID, PRODUCT_ID))
//               .expectException(DuplicateOrderLineException.class)
//               .expectExceptionMessage(Matchers.predicate(message -> ((String) message).contains(PRODUCT_ID)));
//    }

    @Test
    void givenOrderCreatedEventAndProductAddedEvent_whenIncrementProductCountCommand_thenShouldPublishProductCountIncrementedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID), new ProductAddedEvent(ORDER_ID, PRODUCT_ID))
               .when(new ProductIncrementCountCommand(ORDER_ID, PRODUCT_ID))
               .expectEvents(new ProductCountIncrementedEvent(ORDER_ID, PRODUCT_ID));
    }

    @Test
    void givenOrderCreatedEventProductAddedEventAndProductCountIncrementedEvent_whenDecrementProductCountCommand_thenShouldPublishProductCountDecrementedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID),
                      new ProductAddedEvent(ORDER_ID, PRODUCT_ID),
                      new ProductCountIncrementedEvent(ORDER_ID, PRODUCT_ID))
               .when(new ProductDecrementCountCommand(ORDER_ID, PRODUCT_ID))
               .expectEvents(new ProductCountDecrementedEvent(ORDER_ID, PRODUCT_ID));
    }

    @Test
    void givenOrderCreatedEventAndProductAddedEvent_whenDecrementProductCountCommand_thenShouldPublishProductRemovedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID), new ProductAddedEvent(ORDER_ID, PRODUCT_ID))
               .when(new ProductDecrementCountCommand(ORDER_ID, PRODUCT_ID))
               .expectEvents(new ProductRemovedEvent(ORDER_ID, PRODUCT_ID));
    }

    @Test
    void givenOrderCreatedEvent_whenConfirmOrderCommand_thenShouldPublishOrderConfirmedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID))
               .when(new OrderConfirmCommand(ORDER_ID))
               .expectEvents(new OrderConfirmedEvent(ORDER_ID));
    }

    @Test
    void givenOrderCreatedEventAndOrderConfirmedEvent_whenConfirmOrderCommand_thenExpectNoEvents() {
        fixture.given(new OrderCreatedEvent(ORDER_ID), new OrderConfirmedEvent(ORDER_ID))
               .when(new OrderConfirmCommand(ORDER_ID))
               .expectNoEvents();
    }

//    @Test
//    void givenOrderCreatedEvent_whenShipOrderCommand_thenShouldThrowUnconfirmedOrderException() {
//        fixture.given(new OrderCreatedEvent(ORDER_ID))
//               .when(new OrderDeliverCommand(ORDER_ID))
//               .expectException(UnconfirmedOrderException.class);
//    }

    @Test
    void givenOrderCreatedEventAndOrderConfirmedEvent_whenShipOrderCommand_thenShouldPublishOrderShippedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID), new OrderConfirmedEvent(ORDER_ID))
               .when(new OrderDeliverCommand(ORDER_ID))
               .expectEvents(new OrderDeliveredEvent(ORDER_ID));
    }

//    @Test
//    void givenOrderCreatedEventProductAndOrderConfirmedEvent_whenAddProductCommand_thenShouldThrowOrderAlreadyConfirmedException() {
//        fixture.given(new OrderCreatedEvent(ORDER_ID), new OrderConfirmedEvent(ORDER_ID))
//               .when(new ProductAddCommand(ORDER_ID, PRODUCT_ID))
//               .expectException(OrderAlreadyConfirmedException.class)
//               .expectExceptionMessage(Matchers.predicate(message -> ((String) message).contains(ORDER_ID)));
//    }
//
//    @Test
//    void givenOrderCreatedEventProductAddedEventAndOrderConfirmedEvent_whenIncrementProductCountCommand_thenShouldThrowOrderAlreadyConfirmedException() {
//        fixture.given(new OrderCreatedEvent(ORDER_ID),
//                      new ProductAddedEvent(ORDER_ID, PRODUCT_ID),
//                      new OrderConfirmedEvent(ORDER_ID))
//               .when(new ProductIncrementCountCommand(ORDER_ID, PRODUCT_ID))
//               .expectException(OrderAlreadyConfirmedException.class)
//               .expectExceptionMessage(Matchers.predicate(message -> ((String) message).contains(ORDER_ID)));
//    }
//
//    @Test
//    void givenOrderCreatedEventProductAddedEventAndOrderConfirmedEvent_whenDecrementProductCountCommand_thenShouldThrowOrderAlreadyConfirmedException() {
//        fixture.given(new OrderCreatedEvent(ORDER_ID),
//                      new ProductAddedEvent(ORDER_ID, PRODUCT_ID),
//                      new OrderConfirmedEvent(ORDER_ID))
//               .when(new ProductDecrementCountCommand(ORDER_ID, PRODUCT_ID))
//               .expectException(OrderAlreadyConfirmedException.class)
//               .expectExceptionMessage(Matchers.predicate(message -> ((String) message).contains(ORDER_ID)));
//    }
}
