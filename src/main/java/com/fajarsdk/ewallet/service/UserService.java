package com.fajarsdk.ewallet.service;

import com.fajarsdk.ewallet.entity.Transaction;
import com.fajarsdk.ewallet.entity.User;
import com.fajarsdk.ewallet.model.TransactionResponse;
import com.fajarsdk.ewallet.model.enums.TransactionType;
import com.fajarsdk.ewallet.repository.TransactionRepository;
import com.fajarsdk.ewallet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.math.BigDecimal;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    public UserService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Retryable(value = ObjectOptimisticLockingFailureException.class,maxAttempts = 5, backoff = @Backoff(delay = 500))
    @Transactional
    public TransactionResponse processTransaction(Long userId, BigDecimal amount, TransactionType type) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        if (type == TransactionType.DEBIT && user.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        if (type == TransactionType.CREDIT) {
            user.setBalance(user.getBalance().add(amount));
        } else {
            user.setBalance(user.getBalance().subtract(amount));
        }
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setType(type);
        transactionRepository.save(transaction);
        return new TransactionResponse("success", transaction.getId(), user.getBalance());
    }

    @Recover
    public TransactionResponse recoverFromOptimisticLockingFailure(ObjectOptimisticLockingFailureException e, Long userId, BigDecimal amount, TransactionType type) {
        return new TransactionResponse("error", "Could not process transaction. Please try again.");
    }
}
