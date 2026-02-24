package com.salesmanager.api.controller;

import com.salesmanager.api.dto.CreateInvoiceDTO;
import com.salesmanager.api.dto.InvoiceDetailResponseDTO;
import com.salesmanager.api.dto.InvoiceResponseDTO;
import com.salesmanager.api.model.Invoice;
import com.salesmanager.api.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponseDTO>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();

        // Map entities to response DTOs
        List<InvoiceResponseDTO> response = invoices.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable int id) {
        Invoice invoice = invoiceService.getInvoiceById(id);

        return ResponseEntity.ok(mapToResponseDTO(invoice));
    }

    // Creates a new sale and returns the generated invoice with details
    @PostMapping("/sales")
    public ResponseEntity<InvoiceResponseDTO> createSale(@RequestBody CreateInvoiceDTO dto) {
        // Call the service and pass the customerId and items
        Invoice invoice = invoiceService.createSale(dto.getCustomerId(), dto.getItems());

        // Convert entity to DTO response
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponseDTO(invoice));
    }

    private InvoiceResponseDTO mapToResponseDTO(Invoice invoice) {

        // Transform invoice details into response DTO list
        List<InvoiceDetailResponseDTO> items = invoice.getDetails()
                .stream()
                .map(detail -> new InvoiceDetailResponseDTO(
                        detail.getProductId(),
                        detail.getQuantity(),
                        detail.getUnitPrice(),
                        detail.getSubtotal()
                ))
                .toList();

        return new InvoiceResponseDTO(
                invoice.getInvoiceId(),
                invoice.getCustomerId(),
                invoice.getInvoiceDate(),
                invoice.getSubtotal(),
                invoice.getTax(),
                invoice.getTotal(),
                items
        );
    }
}
