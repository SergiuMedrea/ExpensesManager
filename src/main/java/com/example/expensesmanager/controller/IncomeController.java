package com.example.expensesmanager.controller;

import com.example.expensesmanager.model.Income;
import com.example.expensesmanager.repo.IncomeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class IncomeController {
    @Autowired
    private IncomeRepo incomeRepo;

    @GetMapping("/getAllIncomes")
    public ResponseEntity<List<Income>> getAllIncomes() {
        try {
            List<Income> incomes = new ArrayList<>();
            incomeRepo.findAll().forEach(incomes::add);

            if (incomes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(incomes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getIncomeById/{id}")
    public ResponseEntity<Income> getIncomeById(@PathVariable Long id) {
        Optional<Income> incomeData = incomeRepo.findById(id);

        if(incomeData.isPresent()) {
            return new ResponseEntity<>(incomeData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteIncomeById/{id}")
    public ResponseEntity<HttpStatus> deleteIncomeById(@PathVariable Long id) {
        incomeRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addIncome")
    public ResponseEntity<Income> addIncome(@RequestBody Income income) {
        Income incomeObj = incomeRepo.save(income);

        return new ResponseEntity<>(incomeObj, HttpStatus.OK);
    }

    @PostMapping("/updateIncomeById/{id}")
    public ResponseEntity<Income> updateIncome(@PathVariable Long id, @RequestBody Income income) {
        Optional<Income> oldIncomeData = incomeRepo.findById(id);

        if(oldIncomeData.isPresent()) {
            Income incomeObj = oldIncomeData.get();
            incomeObj.setAmount(income.getAmount());
            incomeObj.setSource(income.getSource());
            incomeObj.setDate(income.getDate());
            incomeObj.setUser(income.getUser());

            return new ResponseEntity<>(incomeRepo.save(incomeObj), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
