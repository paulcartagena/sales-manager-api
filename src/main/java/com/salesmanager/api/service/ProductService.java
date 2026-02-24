package com.salesmanager.api.service;

import com.salesmanager.api.exception.InvalidException;
import com.salesmanager.api.exception.ProductNotFoundException;
import com.salesmanager.api.model.Product;
import com.salesmanager.api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        if (id <= 0) {
            throw new InvalidException("product");
        }

        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (product.getPrice() == null || product.getPrice().signum() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("stock cannot be negative");
        }

        return productRepository.save(product);
    }

    public void restockProduct(int id, int quantity) {
        if (id <= 0) {
            throw new InvalidException("product");
        }

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        int newStock = existing.getStock() + quantity;

        existing.setStock(newStock);
        productRepository.save(existing);
    }

    public Product updateProduct(int id, Product product) {
        if (id <= 0) {
            throw new InvalidException("product");
        }

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (product.getPrice() == null || product.getPrice().signum() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("stock cannot be negative");
        }


        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());

        return productRepository.save(existing);
    }

    public void deleteProduct(int id) {
        if (id <= 0) {
            throw new InvalidException("product");
        }

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(existing);
    }
}
