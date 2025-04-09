package org.example.bankcards.service.AdminCardService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.bankcards.dao.entity.Card;
import org.example.bankcards.dao.entity.CardLimit;
import org.example.bankcards.dao.entity.User;
import org.example.bankcards.dao.repository.CardLimitRepository;
import org.example.bankcards.dao.repository.CardRepository;
import org.example.bankcards.dao.repository.UserRepository;
import org.example.bankcards.dto.CardDto;
import org.example.bankcards.dto.CardLimitDto;
import org.example.bankcards.mapper.CardLimitMapper;
import org.example.bankcards.mapper.CardMapper;
import org.example.bankcards.pojo.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminCardService implements IAdminCardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardLimitRepository cardLimitRepository;

    @Override
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream()
                .map(CardMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CardDto createCard(CardDto cardDTO) {
        Long ownerId = cardDTO.getOwner().getId();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с id " + ownerId + " не найден"));

        System.out.println("Владелец карты найден: " + owner.getEmail());

        Card card = CardMapper.toEntity(cardDTO, owner);
        card.setStatus(CardStatus.ACTIVE);
        card.setEncryptedCardNumber(encryptCardNumber());

        if (card.getTransactions() == null) {
            card.setTransactions(new ArrayList<>());
        }

        Card savedCard = cardRepository.save(card);

        return CardMapper.toDTO(savedCard);
    }

    @Override
    public void blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта не найдена"));
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Override
    public void activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта не найдена"));
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
    }

    @Override
    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new EntityNotFoundException("Карта не найдена");
        }
        cardRepository.deleteById(cardId);
    }

    private String encryptCardNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }
    @Override
    public CardLimitDto setCardLimit(CardLimitDto dto) {
        Card card = cardRepository.findById(dto.getCardId())
                .orElseThrow(() -> new EntityNotFoundException("Карта не найдена"));

        BigDecimal daily = dto.getDailyLimit();
        BigDecimal monthly = dto.getMonthlyLimit();

        // чтобы дневной не превышал месячный
        if (daily != null && monthly != null && daily.compareTo(monthly) > 0) {
            throw new IllegalArgumentException("Дневной лимит не может превышать месячный лимит");
        }

        CardLimit limit = card.getCardLimit();

        if (limit == null) {
            limit = CardLimitMapper.toEntity(dto, card);
        } else {
            limit.setDailyLimit(daily);
            limit.setMonthlyLimit(monthly);
        }

        cardLimitRepository.save(limit);
        return CardLimitMapper.toDTO(limit);
    }

    @Override
    public CardLimitDto getCardLimitByCardId(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта не найдена"));

        CardLimit limit = card.getCardLimit();
        if (limit == null) throw new EntityNotFoundException("Лимит для карты не установлен");

        return CardLimitMapper.toDTO(limit);
    }


    public Page<CardDto> getFilteredCards(CardStatus status, BigDecimal balance, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id"))); // Пагинация и сортировка

        // Фильтрация карт с учетом переданных параметров
        if (status != null && balance != null) {
            return cardRepository.findByStatusAndBalanceGreaterThanEqual(status, balance, pageable)
                    .map(CardMapper::toDTO);
        } else if (status != null) {
            return cardRepository.findByStatus(status, pageable)
                    .map(CardMapper::toDTO);
        } else if (balance != null) {
            return cardRepository.findByBalanceGreaterThanEqual(balance, pageable)
                    .map(CardMapper::toDTO);
        } else {
            return cardRepository.findAll(pageable)
                    .map(CardMapper::toDTO);
        }
    }



}
