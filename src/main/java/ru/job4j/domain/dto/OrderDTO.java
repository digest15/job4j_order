package ru.job4j.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.job4j.domain.Customer;
import ru.job4j.domain.Dish;
import ru.job4j.domain.Status;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderDTO {

    @EqualsAndHashCode.Include
    private int id;

    private Customer customer;

    private Set<Dish> dishes = new HashSet<>();

    private Status status;
}
