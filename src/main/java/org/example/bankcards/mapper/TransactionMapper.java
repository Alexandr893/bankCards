package org.example.bankcards.mapper;

import org.example.bankcards.dao.entity.Card;
import org.example.bankcards.dao.entity.Transaction;
import org.example.bankcards.dto.TransactionDto;

import java.time.LocalDateTime;

public class TransactionMapper {

    public TransactionDto toDto(Transaction transaction) {

        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setTimestamp(transaction.getTimestamp());
        return dto;
    }


    public Transaction toEntity(TransactionDto dto, Card card) {

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setDescription(dto.getDescription());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setCard(card);
        return transaction;
    }
}
