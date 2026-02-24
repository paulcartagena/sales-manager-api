package com.salesmanager.api.service;

import com.salesmanager.api.dto.InvoiceItemDTO;
import com.salesmanager.api.exception.*;
import com.salesmanager.api.model.Customer;
import com.salesmanager.api.model.Invoice;
import com.salesmanager.api.model.InvoiceDetail;
import com.salesmanager.api.model.Product;
import com.salesmanager.api.repository.CustomerRepository;
import com.salesmanager.api.repository.InvoiceRepository;
import com.salesmanager.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.13");

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          CustomerRepository customerRepository,
                          ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(int id) {
        if (id <= 0) {
            throw new InvalidException("invoice");
        }

        return invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id));
    }

    @Transactional
    public Invoice createSale(int customerId, List<InvoiceItemDTO> items) {

        if (customerId <= 0) {
            throw new InvalidException("customer");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items list cannot be empty");
        }

             Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        // Map the products by item ids
        List<Integer> productsIds = items.stream()
                .map(InvoiceItemDTO::getProductId)
                .toList();

        // List the map products
        List<Product> productList = productRepository.findAllById(productsIds);

        // Convert list to map
        Map<Integer, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getProductId, p -> p));

        BigDecimal subtotal = BigDecimal.ZERO;

        // Create invoice  (NO save)
        Invoice invoice = new Invoice();
        invoice.setCustomerId(customerId);
        invoice.setInvoiceDate(LocalDateTime.now());

        for (InvoiceItemDTO item : items) {    // item: {productId: #, quantity: #}

            Product product = productMap.get(item.getProductId());
            // product = {id: #, name: , price: , stock: }

            if (product == null) {
                throw new ProductNotFoundException(item.getProductId());
            }
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
            if (product.getStock() < item.getQuantity()) {
                throw new InsufficientStockException(item.getProductId());
            }

            BigDecimal itemSubtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            subtotal = subtotal.add(itemSubtotal);

            InvoiceDetail detail = new InvoiceDetail();
            detail.setInvoice(invoice); // Associate with the invoice
            detail.setProductId(item.getProductId());
            detail.setQuantity(item.getQuantity());
            detail.setUnitPrice(product.getPrice());
            detail.setSubtotal(itemSubtotal);

            invoice.getDetails().add(detail);
        }

        BigDecimal tax = subtotal.multiply(TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = subtotal.add(tax);

        invoice.setSubtotal(subtotal);
        invoice.setTax(tax);
        invoice.setTotal(total);

        // Save the invoice to the database
        // Because of @OneToMany(cascade = CascadeType.ALL), it also saves ALL the details automatically
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Update stock
        for (InvoiceItemDTO item : items) {
            Product product = productMap.get(item.getProductId());
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        return savedInvoice;
    }
}
