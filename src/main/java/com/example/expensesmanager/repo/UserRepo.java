package com.example.expensesmanager.repo;

import com.example.expensesmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    boolean existsByPassword(String password);
}
