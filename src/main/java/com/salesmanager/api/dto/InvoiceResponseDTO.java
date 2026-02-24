package com.salesmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDTO {
    private int invoiceId;
    private int customerId;
    private LocalDateTime invoiceDate;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    private List<InvoiceDetailResponseDTO> details;
}
