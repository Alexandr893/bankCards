package org.example.bankcards.service.UserCardService;

import org.example.bankcards.dto.CardDto;
import org.example.bankcards.dto.TransactionDto;

import java.math.BigDecimal;
import java.util.List;

public interface IUserCardService {

    List<CardDto> getAllUserCards();
    void blockCard(Long cardId);
    void transferMoney(Long fromCardId, Long toCardId, BigDecimal amount);
    List<TransactionDto> getCardTransactions(Long cardId);
    void withdraw(Long cardId, BigDecimal amount);

}
