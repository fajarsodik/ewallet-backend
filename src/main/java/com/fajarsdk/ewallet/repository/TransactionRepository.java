package com.fajarsdk.ewallet.repository;

import com.fajarsdk.ewallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
