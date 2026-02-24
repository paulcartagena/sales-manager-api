package com.salesmanager.api.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(int id) {
        super("Insufficient stock for product with id: " + id);
    }
}
