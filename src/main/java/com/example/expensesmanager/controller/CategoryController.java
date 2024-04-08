package com.example.expensesmanager.controller;

import com.example.expensesmanager.model.Category;
import com.example.expensesmanager.model.User;
import com.example.expensesmanager.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CategoryController {
    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<Category>> getAllUsers() {
        try {
            List<Category> categories = new ArrayList<>();
            categoryRepo.findAll().forEach(categories::add);

            if (categories.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getCategoryById/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryData = categoryRepo.findById(id);

        if(categoryData.isPresent()) {
            return new ResponseEntity<>(categoryData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteCategoryById/{id}")
    public ResponseEntity<HttpStatus> deleteCategoryById(@PathVariable Long id) {
        categoryRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addCategory")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category categoryObj = categoryRepo.save(category);

        return new ResponseEntity<>(categoryObj, HttpStatus.OK);
    }

    @PostMapping("/updateCategoryById/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Optional<Category> oldCategoryData = categoryRepo.findById(id);
        if(oldCategoryData.isPresent()) {
            Category categoryObj = oldCategoryData.get();
            categoryObj.setName(category.getName());
            categoryObj.setExpenses(category.getExpenses());
            return new ResponseEntity<>(categoryRepo.save(categoryObj), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
