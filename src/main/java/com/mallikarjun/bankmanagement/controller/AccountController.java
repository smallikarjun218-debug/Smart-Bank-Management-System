package com.mallikarjun.bankmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.mallikarjun.bankmanagement.entity.Transaction;

import com.mallikarjun.bankmanagement.entity.Account;
import com.mallikarjun.bankmanagement.service.AccountService;
import java.io.File;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Register account
    @PostMapping("/register")
    public Account registerAccount(@RequestBody Account account) {
        System.out.println(account.getName());
        System.out.println(account.getEmail());

        return accountService.registerAccount(account);
    }

    // Deposit money
    @PostMapping("/deposit")
    public Account depositMoney(
            @RequestParam int accountNumber,
            @RequestParam double amount) {

        return accountService.depositMoney(accountNumber, amount);
    }
    @PostMapping("/withdraw")
    public Account withdrawMoney(
            @RequestParam int accountNumber,
            @RequestParam double amount) {

        return accountService.withdrawMoney(accountNumber, amount);
    }
    @GetMapping("/balance/{accountNumber}")
    public double checkBalance(@PathVariable int accountNumber) {
        return accountService.checkBalance(accountNumber);
    }
    @PostMapping("/transfer")
    public String transferMoney(
            @RequestParam int fromAccount,
            @RequestParam int toAccount,
            @RequestParam double amount) {

        return accountService.transferMoney(fromAccount, toAccount, amount);
    }
    @GetMapping("/transactions/{accountNumber}")
    public List<Transaction> getTransactions(@PathVariable int accountNumber) {
        return accountService.getTransactionHistory(accountNumber);
    }
    
    @PostMapping("/login")
    public Account login(@RequestBody Account loginRequest) {

        return accountService.login(
                loginRequest.getAccountNumber(),
                loginRequest.getPasswordHash()
        );
    }
    @GetMapping("/statement/{accountNumber}")
    public ResponseEntity<Resource> downloadStatement(
            @PathVariable int accountNumber) throws Exception {

        String filePath = accountService.generateStatement(accountNumber);

        File file = new File(filePath);
        Resource resource = new UrlResource(file.toURI());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + file.getName())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
    
}