package ru.job4j.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Order;
import ru.job4j.domain.OrderStatus;
import ru.job4j.domain.dto.OrderStatusDTO;
import ru.job4j.repository.OrderStatusRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka-notification-status-topic}")
    private String notificationStatusTopicName;

    @Override
    public List<OrderStatus> findAll() {
        return orderStatusRepository.findAll();
    }

    @Override
    public Optional<OrderStatus> findById(int id) {
        return orderStatusRepository.findById(id);
    }

    @Override
    public List<OrderStatus> findByOrderId(int id) {
        return orderStatusRepository.findByOrderId(new Order(id));
    }

    @Override
    public Optional<OrderStatus> findLastByOrderId(int id) {
        return orderStatusRepository.findLastByOrderIdOrderByCreationDate(id);
    }

    @Override
    public Optional<OrderStatus> save(OrderStatusDTO orderStatusDto) {
        return save(new OrderStatus(orderStatusDto.getOrderId(), orderStatusDto.getStatus()));
    }

    @Override
    public boolean delete(int id) {
        Optional<OrderStatus> orderStatus = orderStatusRepository.findById(id);
        orderStatus.ifPresent(orderStatusRepository::delete);
        return orderStatus.isPresent();
    }

    @Override
    @KafkaListener(topics = "job4j_orders_status")
    public void receiveStatus(OrderStatus orderStatus) {
        if (log.isDebugEnabled()) {
            log.debug(orderStatus.toString());
        }

        orderStatus.setId(0);
        Optional<OrderStatus> saved = save(orderStatus);

        saved.ifPresent(status ->
                kafkaTemplate.send(notificationStatusTopicName, status)
        );
    }

    private Optional<OrderStatus> save(OrderStatus orderStatus) {
        Optional<OrderStatus> res = Optional.empty();
        try {
            orderStatusRepository.save(orderStatus);
            res = Optional.of(orderStatus);
        } catch (Exception e) {
            log.error("Save or Update was wrong", e);
        }
        return res;
    }
}
