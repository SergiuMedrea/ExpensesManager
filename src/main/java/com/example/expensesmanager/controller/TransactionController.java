package com.example.expensesmanager.controller;

import com.example.expensesmanager.model.Transaction;
import com.example.expensesmanager.model.User;
import com.example.expensesmanager.repo.TransactionRepo;
import com.example.expensesmanager.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }

    @PostMapping
    public ResponseEntity<Map<String, Transaction>> addTransaction(@RequestBody Map<String, Object> body) {
        long userId = Long.parseLong(body.get("user_id").toString());
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(Double.parseDouble(body.get("amount").toString()));
        transaction.setName(body.get("name").toString());
        transaction.setUser(user);

        Transaction newTransaction = transactionRepo.save(transaction);
        Map<String, Transaction> response = new HashMap<>();
        response.put("transaction", newTransaction);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        List<Transaction> transactions = transactionRepo.findByUser(user);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
