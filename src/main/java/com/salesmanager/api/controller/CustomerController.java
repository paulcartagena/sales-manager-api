package com.salesmanager.api.controller;

import com.salesmanager.api.dto.CreateCustomerDTO;
import com.salesmanager.api.dto.CustomerResponseDTO;
import com.salesmanager.api.model.Customer;
import com.salesmanager.api.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerResponseDTO> response = customers.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable int id) {
        Customer customer = customerService.getCustomerById(id);

        return ResponseEntity.ok(mapToResponseDTO(customer));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CreateCustomerDTO dto) {
        Customer customer = mapToEntity(dto);
        Customer created = customerService.createCustomer(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponseDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable int id,
            @RequestBody CreateCustomerDTO dto) {
        Customer customer = mapToEntity(dto);
        Customer updated = customerService.updateCustomer(id, customer);

        return ResponseEntity.ok(mapToResponseDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }

    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }

    private Customer mapToEntity(CreateCustomerDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());

        return customer;
    }
}
