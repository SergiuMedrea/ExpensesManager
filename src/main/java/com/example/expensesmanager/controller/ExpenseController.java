package com.example.expensesmanager.controller;

import com.example.expensesmanager.model.Expense;
import com.example.expensesmanager.repo.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ExpenseController {
    @Autowired
    private ExpenseRepo expenseRepo;

    @GetMapping("/getAllExpenses")
    public ResponseEntity<List<Expense>> getAllExpenses() {
        try {
            List<Expense> expenses = new ArrayList<>();
            expenseRepo.findAll().forEach(expenses::add);

            if (expenses.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(expenses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getExpenseById/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Optional<Expense> expenseData = expenseRepo.findById(id);

        if(expenseData.isPresent()) {
            return new ResponseEntity<>(expenseData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteExpenseById/{id}")
    public ResponseEntity<HttpStatus> deleteExpenseById(@PathVariable Long id) {
        expenseRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addExpense")
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        Expense expenseObj = expenseRepo.save(expense);

        return new ResponseEntity<>(expenseObj, HttpStatus.OK);
    }

    @PostMapping("/updateExpenseById/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        Optional<Expense> oldExpenseData = expenseRepo.findById(id);

        if(oldExpenseData.isPresent()) {
            Expense _expense = oldExpenseData.get();
            _expense.setName(expense.getName());
            _expense.setAmount(expense.getAmount());
            _expense.setDate(expense.getDate());
            _expense.setCategories(expense.getCategories());
            return new ResponseEntity<>(expenseRepo.save(_expense), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
