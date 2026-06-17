package com.mallikarjun.bankmanagement.service;
import java.io.FileOutputStream;


import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mallikarjun.bankmanagement.entity.Account;
import com.mallikarjun.bankmanagement.repository.AccountRepository;
import java.time.LocalDateTime;
import java.util.List;

import com.mallikarjun.bankmanagement.entity.Transaction;
import com.mallikarjun.bankmanagement.repository.TransactionRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    // Register account
    public Account registerAccount(Account account) {
        
        account.setBalance(0);
        account.setStatus("ACTIVE");
        //account.setCreatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    // Deposit money
    public Account depositMoney(int accountNumber, double amount) {

        Account account = accountRepository.findById(accountNumber).orElse(null);

        if(account != null) {

            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);

            Transaction transaction = new Transaction();
            transaction.setFromAccount(accountNumber);
            transaction.setToAccount(accountNumber);
            transaction.setType("DEPOSIT");
            transaction.setAmount(amount);
            transaction.setStatus("SUCCESS");
            transaction.setRemarks("Money deposited");
            transaction.setCreatedAt(LocalDateTime.now());

            transactionRepository.save(transaction);
        }

        return account;
    }
    public Account withdrawMoney(int accountNumber, double amount) {

        Account account = accountRepository.findById(accountNumber).orElse(null);

        if(account != null && account.getBalance() >= amount) {

            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);

            Transaction transaction = new Transaction();
            transaction.setFromAccount(accountNumber);
            transaction.setToAccount(accountNumber);
            transaction.setType("WITHDRAW");
            transaction.setAmount(amount);
            transaction.setStatus("SUCCESS");
            transaction.setRemarks("Money withdrawn");
            transaction.setCreatedAt(LocalDateTime.now());

            transactionRepository.save(transaction);
        }

        return account;
    }
    public double checkBalance(int accountNumber) {

        Account account = accountRepository.findById(accountNumber).orElse(null);

        if(account != null) {
            return account.getBalance();
        }

        return 0;
    }
    public String transferMoney(int fromAccount, int toAccount, double amount) {

        Account sender = accountRepository.findById(fromAccount).orElse(null);
        Account receiver = accountRepository.findById(toAccount).orElse(null);

        if(sender == null || receiver == null) {
            return "Account not found";
        }

        if(sender.getBalance() < amount) {
            return "Insufficient balance";
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setType("TRANSFER");
        transaction.setAmount(amount);
        transaction.setStatus("SUCCESS");
        transaction.setRemarks("Money transferred");
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        return "Transfer successful";
    }
    public List<Transaction> getTransactionHistory(int accountNumber) {
        
        return transactionRepository
                .findByFromAccountOrToAccount(accountNumber, accountNumber);
    }
    public Account login(int accountNumber, String password) {

        Account acc = accountRepository
                .findByAccountNumber(accountNumber)
                .orElse(null);

        if (acc != null && acc.getPasswordHash().equals(password)) {
            return acc;
        }

        return null;
    }
    public String generateStatement(int accountNumber) {
        try {
        	String filePath = "statement_" + accountNumber + ".pdf";

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("BANK STATEMENT"));
            document.add(new Paragraph("Account Number: " + accountNumber));
            document.add(new Paragraph("Statement Generated Successfully"));

            document.close();

            return filePath;

        } catch (Exception e) {
            e.printStackTrace();
            return "PDF Generation Failed: " + e.getMessage() ;
        }
    }
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    }

