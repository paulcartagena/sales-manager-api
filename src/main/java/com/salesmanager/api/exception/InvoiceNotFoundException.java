package com.salesmanager.api.exception;

public class InvoiceNotFoundException extends NotFoundException {
    public InvoiceNotFoundException(int id) {
        super("Invoice not found with id: " + id);
    }
}
