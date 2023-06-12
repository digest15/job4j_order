package ru.job4j.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Customer;
import ru.job4j.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(int id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> save(Customer customer) {
        Optional<Customer> res = Optional.empty();
        try {
            customerRepository.save(customer);
            res = Optional.of(customer);
        } catch (Exception e) {
            log.error("Save or Update was wrong", e);
        }
        return res;
    }

    @Override
    public boolean delete(int id) {
        Optional<Customer> customer = customerRepository.findById(id);
        customer.ifPresent(customerRepository::delete);
        return customer.isPresent();
    }
}
