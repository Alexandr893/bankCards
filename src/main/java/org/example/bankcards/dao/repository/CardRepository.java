package org.example.bankcards.dao.repository;

import org.example.bankcards.dao.entity.Card;
import org.example.bankcards.dao.entity.User;
import org.example.bankcards.pojo.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByOwnerId(Long ownerId);
    List<Card> findByOwner(User owner);
    Page<Card> findByStatus(CardStatus status, Pageable pageable);

    Page<Card> findByBalanceGreaterThanEqual(BigDecimal balance, Pageable pageable);

    Page<Card> findByStatusAndBalanceGreaterThanEqual(CardStatus status, BigDecimal balance, Pageable pageable);
}
