package org.example.bankcards.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    public Transaction(LocalDateTime timestamp, BigDecimal amount, String description, Card card) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.description = description;
        this.card = card;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private BigDecimal amount;

    private String description;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;



}
