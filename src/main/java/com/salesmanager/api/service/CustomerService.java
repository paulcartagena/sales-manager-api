package com.salesmanager.api.service;

import com.salesmanager.api.exception.CustomerNotFoundException;
import com.salesmanager.api.exception.InvalidException;
import com.salesmanager.api.model.Customer;
import com.salesmanager.api.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(int id) {
        if (id <= 0) {
            throw new InvalidException("customer");
        }

        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public Customer createCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer is required");
        }
        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new IllegalArgumentException("Customer email is required");
        }
        if (customer.getPhone() == null || customer.getPhone().isBlank()) {
            throw new IllegalArgumentException("Customer phone is required");
        }
        if (!customer.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        return customerRepository.save(customer);
    }

    public Customer updateCustomer(int id, Customer customer) {
        if (id <= 0) {
            throw new InvalidException("customer");
        }

        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new IllegalArgumentException("Customer email is required");
        }
        if (customer.getPhone() == null || customer.getPhone().isBlank()) {
            throw new IllegalArgumentException("Customer phone is required");
        }
        if (!customer.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        existing.setName(customer.getName());
        existing.setEmail(customer.getEmail());
        existing.setPhone(customer.getPhone());

        return customerRepository.save(existing);
    }

    public void deleteCustomer(int id) {
        if (id <= 0) {
            throw new InvalidException("customer");
        }

        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customerRepository.delete(existing);
    }
}
