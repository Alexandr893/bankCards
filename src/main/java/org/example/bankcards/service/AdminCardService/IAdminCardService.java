package org.example.bankcards.service.AdminCardService;


import org.example.bankcards.dto.CardDto;
import org.example.bankcards.dto.CardLimitDto;
import org.example.bankcards.pojo.CardStatus;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface IAdminCardService {

    List<CardDto> getAllCards();
    CardDto createCard(CardDto cardDTO);
    void blockCard(Long cardId);
    void activateCard(Long cardId);
    void deleteCard(Long cardId);
    CardLimitDto setCardLimit(CardLimitDto dto);
    CardLimitDto getCardLimitByCardId(Long cardId);
    public Page<CardDto> getFilteredCards(CardStatus status, BigDecimal balance, int page, int size);

}
