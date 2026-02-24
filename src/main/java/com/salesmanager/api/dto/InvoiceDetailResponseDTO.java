package com.salesmanager.api.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailResponseDTO {
    private int productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
