package ru.job4j.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Order;
import ru.job4j.domain.dto.OrderDTO;
import ru.job4j.service.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDTO> finaAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public OrderDTO findById(@PathVariable int id) {
        Optional<OrderDTO> order = orderService.findById(id);
        if (order.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Not found Order with id %d", id)
            );
        }
        return order.get();
    }

    @PostMapping("/")
    @ResponseStatus(code = HttpStatus.OK)
    public OrderDTO create(@RequestBody Order order) {
        Optional<OrderDTO> saved = orderService.save(order);
        if (saved.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There was a problem when save Order");
        }
        return saved.get();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancel(@PathVariable int id) {
        boolean canceled = orderService.cancel(id);
        if (!canceled) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("There was a problem when cancel Order by id: %d", id)
            );
        }
        return canceled
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest()
                    .body(String.format("There was a problem when delete Order by id: %d", id));
    }

}
