package ru.job4j.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.OrderStatus;
import ru.job4j.domain.dto.OrderStatusDTO;
import ru.job4j.service.OrderStatusService;

import java.util.Optional;

@RestController
@RequestMapping("/status")
@AllArgsConstructor
@Slf4j
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public OrderStatus changeOrderStatus(@RequestBody OrderStatusDTO status) {
        Optional<OrderStatus> saved = orderStatusService.save(status);
        if (saved.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There was a problem when save Order");
        }
        return saved.get();
    }
}
