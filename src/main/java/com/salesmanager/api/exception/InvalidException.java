package com.salesmanager.api.exception;

public class InvalidException extends RuntimeException {
    public InvalidException(String entity) {
        super("Invalid id for: " + entity);
    }
}
