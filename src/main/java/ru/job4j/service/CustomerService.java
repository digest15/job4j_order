package ru.job4j.service;

import ru.job4j.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> findAll();

    Optional<Customer> findById(int id);

    Optional<Customer> save(Customer customer);

    boolean delete(int id);

}
