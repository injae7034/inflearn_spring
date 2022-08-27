package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {

    private final Environment env;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;

    private final OrderProducer orderProducer;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    // order-service/{userId}/orders
    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable String userId,
                                                     @RequestBody RequestOrder requestOrder) {
        log.info("Before add orders data");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(userId);
        //jpa
//        OrderDto createdOrder = orderService.createOrder(orderDto);
//        ResponseOrder responseOrder = mapper.map(createdOrder, ResponseOrder.class);

        //kafka
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(requestOrder.getQty() * requestOrder.getUnitPrice());

        //send this order to the kafka
        kafkaProducer.send("example-catalog-topic", orderDto);
        orderProducer.send("orders", orderDto);

        ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);
        log.info("After added orders data");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getAllOrders(@PathVariable String userId) throws Exception {
        log.info("Before retrieve orders data");
        Iterable<OrderEntity> ordersByUserId = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        ordersByUserId.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseOrder.class));
        });

        try {
            Thread.sleep(1000);
            throw new Exception("장애 발생");
        } catch (InterruptedException ex) {
            log.warn(ex.getMessage());

        }

        log.info("After retrieved orders data");

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<ResponseOrder> getOrder(@PathVariable String orderId) {
        OrderDto orderByOrderId = orderService.getOrderByOrderId(orderId);

        ResponseOrder responseOrder = new ModelMapper().map(orderByOrderId, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseOrder);
    }
}
