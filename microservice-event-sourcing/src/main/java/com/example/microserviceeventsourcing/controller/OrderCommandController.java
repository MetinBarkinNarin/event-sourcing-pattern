package com.example.microserviceeventsourcing.controller;

import com.example.microserviceeventsourcing.commands.order.OrderConfirmCommand;
import com.example.microserviceeventsourcing.commands.order.OrderCreateCommand;
import com.example.microserviceeventsourcing.commands.order.OrderDeliverCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderCommandController {

    private final CommandGateway commandGateway;

    @PostMapping("/order")
    public CompletableFuture<String> createOrder() {
        return createOrder(UUID.randomUUID().toString());
    }

    @PostMapping("/order/{order-id}")
    public CompletableFuture<String> createOrder(@PathVariable("order-id") String orderId) {
        return commandGateway.send(new OrderCreateCommand(orderId));
    }

    @PostMapping("/order/{order-id}/confirm")
    public CompletableFuture<Void> confirmOrder(@PathVariable("order-id") String orderId) {
        return commandGateway.send(new OrderConfirmCommand(orderId));
    }

    @PostMapping("/order/{order-id}/deliver")
    public CompletableFuture<Void> deliverOrder(@PathVariable("order-id") String orderId) {
        return commandGateway.send(new OrderDeliverCommand(orderId));
    }
}
