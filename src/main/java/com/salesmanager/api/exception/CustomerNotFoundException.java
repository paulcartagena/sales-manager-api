package com.salesmanager.api.exception;

public class CustomerNotFoundException extends NotFoundException {
    public CustomerNotFoundException(int id) {
        super("Customer not found with id: " + id);
    }
}
