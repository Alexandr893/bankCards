package org.example.bankcards.mapper;

import org.example.bankcards.dao.entity.Card;
import org.example.bankcards.dao.entity.CardLimit;
import org.example.bankcards.dto.CardLimitDto;

public class CardLimitMapper {

    public static CardLimitDto toDTO(CardLimit cardLimit) {
        return new CardLimitDto(
                cardLimit.getId(),
                cardLimit.getDailyLimit(),
                cardLimit.getMonthlyLimit(),
                cardLimit.getCard().getId()
        );
    }

    public static CardLimit toEntity(CardLimitDto dto, Card card) {
        CardLimit limit = new CardLimit();
        limit.setId(dto.getId());
        limit.setDailyLimit(dto.getDailyLimit());
        limit.setMonthlyLimit(dto.getMonthlyLimit());
        limit.setCard(card);
        return limit;
    }
}
