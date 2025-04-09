package org.example.bankcards.mapper;

import org.example.bankcards.dao.entity.Card;
import org.example.bankcards.dao.entity.Transaction;
import org.example.bankcards.dao.entity.User;
import org.example.bankcards.dto.CardDto;
import org.example.bankcards.dto.TransactionDto;
import org.example.bankcards.dto.UserDto;
import org.example.bankcards.pojo.CardStatus;

import java.util.List;
import java.util.stream.Collectors;

public class CardMapper {

    public static CardDto toDTO(Card card) {
        UserDto ownerDTO = new UserDto(card.getOwner().getId(), card.getOwner().getEmail(), card.getOwner().getRole());
        List<TransactionDto> transactionDtos = card.getTransactions().stream()
                .map(TransactionMapper::toDto)
                .collect(Collectors.toList());

        return new CardDto(
                card.getId(),
                card.getEncryptedCardNumber(),
                card.getCardholderName(),
                card.getExpiryDate(),
                card.getStatus().name(),
                card.getBalance(),
                ownerDTO,
                transactionDtos
        );
    }

    public static Card toEntity(CardDto cardDTO, User owner) {
        Card card = new Card();
        card.setId(cardDTO.getId());
        card.setEncryptedCardNumber(cardDTO.getEncryptedCardNumber());
        card.setCardholderName(cardDTO.getCardholderName());
        card.setExpiryDate(cardDTO.getExpiryDate());
        card.setStatus(CardStatus.valueOf(cardDTO.getStatus()));
        card.setBalance(cardDTO.getBalance());
        card.setOwner(owner);
        return card;
    }

    public class TransactionMapper {
        public static TransactionDto toDto(Transaction transaction) {
            TransactionDto dto = new TransactionDto();
            dto.setId(transaction.getId());
            dto.setTimestamp(transaction.getTimestamp());
            dto.setAmount(transaction.getAmount());
            dto.setDescription(transaction.getDescription());
            return dto;
        }
    }
}



