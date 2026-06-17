package com.mallikarjun.bankmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mallikarjun.bankmanagement.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	List<Transaction> findByFromAccountOrToAccount(int fromAccount, int toAccount);

}