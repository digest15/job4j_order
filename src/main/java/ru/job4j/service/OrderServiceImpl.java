package ru.job4j.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.job4j.client.DishClient;
import ru.job4j.domain.*;
import ru.job4j.domain.dto.DishDTO;
import ru.job4j.domain.dto.OrderDTO;
import ru.job4j.domain.dto.OrderStatusDTO;
import ru.job4j.repository.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusService orderStatusService;
    private final DishClient dishClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka-order-topic}")
    private String orderTopicName;

    @Override
    public List<OrderDTO> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapOrderToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDTO> findById(int id) {
        return orderRepository.findById(id)
                .map(this::mapOrderToOrderDTO);
    }

    @Override
    public List<OrderDTO> findByCustomerId(int id) {
        return orderRepository.findByCustomer(new Customer(id))
                .stream()
                .map(this::mapOrderToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDTO> save(Order order) {
        try {
            orderRepository.save(order);
            orderStatusService.save(new OrderStatusDTO(order.getId(), Status.NEW));

        } catch (Exception e) {
            log.error("Save or Update was wrong", e);
        }

        Optional<OrderDTO> optOrder = order.getId() != 0
                ? Optional.of(mapOrderToOrderDTO(order))
                : Optional.empty();

        optOrder.ifPresent(o ->
                kafkaTemplate.send(orderTopicName, o)
        );

        return optOrder;
    }

    @Override
    public boolean cancel(int id) {
        Optional<Order> order = orderRepository.findById(id);
        order.ifPresent(o -> orderStatusService.save(new OrderStatusDTO(o.getId(), Status.CANCELLED)));
        return order.isPresent();
    }

    private OrderDTO mapOrderToOrderDTO(Order order) {
        List<DishDTO> dishesDto = order.getDishes().stream()
                .map(dish -> dishClient.getDishByName(dish.getName()))
                .toList();

        Optional<OrderStatus> orderStatus = orderStatusService.findLastByOrderId(order.getId());

        return new OrderDTO(
                order.getId(),
                order.getCustomer(),
                dishesDto,
                orderStatus.map(OrderStatus::getStatus).orElse(Status.ERROR)
        );
    }
}
