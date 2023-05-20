package ru.job4j.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Customer;
import ru.job4j.domain.Order;
import ru.job4j.domain.OrderStatus;
import ru.job4j.domain.Status;
import ru.job4j.domain.dto.OrderDTO;
import ru.job4j.domain.dto.OrderStatusDTO;
import ru.job4j.repository.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusService orderStatusService;

    @Override
    public List<OrderDTO> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::getWithOrderStatus)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDTO> findById(int id) {
        return orderRepository.findById(id)
                .map(this::getWithOrderStatus);
    }

    @Override
    public List<OrderDTO> findByCustomerId(int id) {
        return orderRepository.findByCustomer(new Customer(id))
                .stream()
                .map(this::getWithOrderStatus)
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
        return order.getId() != 0
                ? Optional.of(getWithOrderStatus(order))
                : Optional.empty();
    }

    @Override
    public boolean cancel(int id) {
        Optional<Order> order = orderRepository.findById(id);
        order.ifPresent(o -> orderStatusService.save(new OrderStatusDTO(o.getId(), Status.CANCELLED)));
        return order.isPresent();
    }

    private OrderDTO getWithOrderStatus(Order order) {
        Optional<OrderStatus> orderStatus = orderStatusService.findLastByOrderId(order.getId());
        return new OrderDTO(
                order.getId(),
                order.getCustomer(),
                order.getDishes(),
                orderStatus.map(OrderStatus::getStatus).orElse(Status.ERROR)
        );
    }
}
