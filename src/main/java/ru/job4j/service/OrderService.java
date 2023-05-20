package ru.job4j.service;

import ru.job4j.domain.Order;
import ru.job4j.domain.dto.OrderDTO;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<OrderDTO> findAll();

    Optional<OrderDTO> findById(int id);

    List<OrderDTO> findByCustomerId(int id);

    Optional<OrderDTO> save(Order order);

    boolean cancel(int id);

}
