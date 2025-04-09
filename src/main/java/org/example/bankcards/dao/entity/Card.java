package org.example.bankcards.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bankcards.pojo.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String encryptedCardNumber;
    private String cardholderName;
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    private BigDecimal balance;

    @ManyToOne
    private User owner;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private CardLimit cardLimit;

    @OneToMany(mappedBy = "card")
    private List<Transaction> transactions;


    public Card(long l, String number, String name, BigDecimal bigDecimal, CardStatus cardStatus, User user, Object o, Object o1) {

        this.encryptedCardNumber = number;
        this.cardholderName = name;
        this.balance = bigDecimal;
        this.owner = user;
        this.status = CardStatus.ACTIVE;
        this.balance = bigDecimal;


    }

    public Card(long l, String number, String name, BigDecimal bigDecimal, CardStatus cardStatus, User otherUser) {
        this.encryptedCardNumber = number;
        this.cardholderName = name;
        this.balance = bigDecimal;
        this.owner = otherUser;
        this.status = CardStatus.ACTIVE;

    }
}
