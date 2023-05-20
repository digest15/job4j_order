package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.domain.Customer;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    @Override
    List<Customer> findAll();
}
