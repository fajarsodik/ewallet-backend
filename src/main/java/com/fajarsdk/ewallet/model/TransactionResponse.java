package com.fajarsdk.ewallet.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {
    private String status;
    private String message;
    private Long transactionId;
    private BigDecimal newBalance;

    public TransactionResponse(String status, Long transactionId, BigDecimal newBalance) {
        this.status = status;
        this.transactionId = transactionId;
        this.newBalance = newBalance;
    }

    public TransactionResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
