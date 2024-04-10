package com.example.expensesmanager.repo;

import com.example.expensesmanager.model.Transaction;
import com.example.expensesmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long>{
    List<Transaction> findByUser(User user);
}
