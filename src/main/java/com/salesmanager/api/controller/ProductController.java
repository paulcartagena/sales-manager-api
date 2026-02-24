package com.salesmanager.api.controller;

import com.salesmanager.api.dto.CreateProductDTO;
import com.salesmanager.api.dto.ProductResponseDTO;
import com.salesmanager.api.dto.RestockDTO;
import com.salesmanager.api.exception.InvalidException;
import com.salesmanager.api.model.Customer;
import com.salesmanager.api.model.Product;
import com.salesmanager.api.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(this::mapToRespondDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);

        return ResponseEntity.ok(mapToRespondDTO(product));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody CreateProductDTO dto) {
        Product product = mapToEntity(dto);
        Product created = productService.createProduct(product);

        return  ResponseEntity.status(HttpStatus.CREATED).body(mapToRespondDTO(created));
    }

    @PatchMapping("/{id}/restock")
    public ResponseEntity<ProductResponseDTO> restockProduct(
            @PathVariable int id,
            @RequestBody RestockDTO dto) {
     productService.restockProduct(id, dto.getQuantity());
     Product updated = productService.getProductById(id);

     return ResponseEntity.ok(mapToRespondDTO(updated));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable int id,
            @RequestBody CreateProductDTO dto) {
        Product product = mapToEntity(dto);
        Product updated = productService.updateProduct(id, product);

        return ResponseEntity.ok(mapToRespondDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    private ProductResponseDTO mapToRespondDTO(Product product) {
        return new ProductResponseDTO(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }

    private Product mapToEntity(CreateProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        return product;
    }
}
