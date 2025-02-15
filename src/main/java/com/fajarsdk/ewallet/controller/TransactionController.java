package com.fajarsdk.ewallet.controller;

import com.fajarsdk.ewallet.model.TransactionRequest;
import com.fajarsdk.ewallet.model.TransactionResponse;
import com.fajarsdk.ewallet.model.enums.TransactionType;
import com.fajarsdk.ewallet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final UserService userService;

    public TransactionController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/credit")
    public ResponseEntity<TransactionResponse> creditBalance(@Valid @RequestBody TransactionRequest request) {
        try {
            TransactionResponse response = userService.processTransaction(request.getUserId(), request.getAmount(), TransactionType.CREDIT);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new TransactionResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/debit")
    public ResponseEntity<TransactionResponse> debitBalance(@Valid @RequestBody TransactionRequest request) {
        try {
            TransactionResponse response = userService.processTransaction(request.getUserId(), request.getAmount(), TransactionType.DEBIT);
            return ResponseEntity.ok(response);
        } catch (Exception  e) {
            return ResponseEntity.badRequest().body(new TransactionResponse("error", e.getMessage()));
        }
    }
}
