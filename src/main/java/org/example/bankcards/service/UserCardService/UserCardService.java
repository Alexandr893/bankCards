package org.example.bankcards.service.UserCardService;

import lombok.AllArgsConstructor;
import org.example.bankcards.dao.entity.Card;
import org.example.bankcards.dao.entity.CardLimit;
import org.example.bankcards.dao.entity.Transaction;
import org.example.bankcards.dao.entity.User;
import org.example.bankcards.dao.repository.CardLimitRepository;
import org.example.bankcards.dao.repository.CardRepository;
import org.example.bankcards.dao.repository.TransactionRepository;
import org.example.bankcards.dao.repository.UserRepository;
import org.example.bankcards.dto.CardDto;
import org.example.bankcards.dto.TransactionDto;
import org.example.bankcards.dto.UserDto;
import org.example.bankcards.pojo.CardStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserCardService implements IUserCardService {


    private CardRepository cardRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
//    private CardLimitRepository cardLimitRepository;



    @Override
    public List<CardDto> getAllUserCards() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        List<Card> cards = cardRepository.findByOwner(user);
        return cards.stream()
                .map(card -> new CardDto(
                        card.getId(),
                        card.getEncryptedCardNumber(),
                        card.getCardholderName(),
                        card.getExpiryDate(),
                        card.getStatus().name(),
                        card.getBalance(),
                        new UserDto(user.getId(), user.getUsername()), // Для простоты
                        card.getTransactions().stream()
                                .map(transaction -> new TransactionDto(
                                        transaction.getId(),
                                        transaction.getTimestamp(),
                                        transaction.getAmount(),
                                        transaction.getDescription()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    // Запрос на блокировку карты
    @Override
    public void blockCard(Long cardId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Карта не найдена"));

        if (!card.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("Вы не можете заблокировать чужую карту");
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }


    @Override
    public void transferMoney(Long fromCardId, Long toCardId, BigDecimal amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Card fromCard = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new IllegalArgumentException("Исходная карта не найдена"));
        Card toCard = cardRepository.findById(toCardId)
                .orElseThrow(() -> new IllegalArgumentException("Целевая карта не найдена"));
        if (!fromCard.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("Вы не можете перевести деньги с чужой карты");
        }


        if (fromCard.getStatus() == CardStatus.BLOCKED || toCard.getStatus() == CardStatus.BLOCKED) {
            throw new IllegalArgumentException("Одна из карт заблокирована");
        }


        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));


        Transaction transactionFrom = new Transaction(LocalDateTime.now(), amount, "Перевод на карту " + toCardId, fromCard);
        Transaction transactionTo = new Transaction(LocalDateTime.now(), amount, "Перевод с карты " + fromCardId, toCard);
        transactionRepository.save(transactionFrom);
        transactionRepository.save(transactionTo);

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }


    @Override
    public List<TransactionDto> getCardTransactions(Long cardId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Карта не найдена"));

        if (!card.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("Вы не можете просматривать транзакции чужой карты");
        }

        return card.getTransactions().stream()
                .map(transaction -> new TransactionDto(
                        transaction.getId(),
                        transaction.getTimestamp(),
                        transaction.getAmount(),
                        transaction.getDescription()))
                .collect(Collectors.toList());
    }


    @Override
    public void withdraw(Long cardId, BigDecimal amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Карта не найдена"));

        if (!card.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("Вы не можете списывать средства с чужой карты");
        }


        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new IllegalArgumentException("Карта заблокирована");
        }


        CardLimit cardLimit = card.getCardLimit();
        if (cardLimit != null) {
            if (amount.compareTo(cardLimit.getDailyLimit()) > 0) {
                throw new IllegalArgumentException("Превышен дневной лимит");
            }
            if (amount.compareTo(cardLimit.getMonthlyLimit()) > 0) {
                throw new IllegalArgumentException("Превышен месячный лимит");
            }
        }


        card.setBalance(card.getBalance().subtract(amount));


        Transaction transaction = new Transaction(LocalDateTime.now(), amount, "Списание средств", card);
        transactionRepository.save(transaction);
        cardRepository.save(card);
    }
}


