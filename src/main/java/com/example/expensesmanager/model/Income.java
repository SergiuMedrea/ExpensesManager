package com.example.expensesmanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Incomes")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double amount;
    private String source;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
