package org.example.bankcards.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bankcards.dao.entity.User;
import org.example.bankcards.dao.repository.UserRepository;
import org.example.bankcards.dto.CardDto;
import org.example.bankcards.dto.CardLimitDto;
import org.example.bankcards.pojo.CardStatus;
import org.example.bankcards.service.AdminCardService.IAdminCardService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/cards")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Работа с банковскими картами (админ)", description = "Управление картами и лимитами пользователей")
public class CardAdminController {
    private final UserRepository userRepository;
    private final IAdminCardService cardService;

    @Operation(summary = "Получить все карты")
    @GetMapping
    public ResponseEntity<List<CardDto>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }


    @Operation(summary = "Создать карту")
    @PostMapping
    public ResponseEntity<CardDto> createCard(@RequestBody CardDto cardDTO) {
        Long ownerId = cardDTO.getOwner().getId();

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с id " + ownerId + " не найден"));
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(cardDTO));
    }


    @Operation(summary = "Заблокировать карту")
    @PutMapping("/{cardId}/block")
    public ResponseEntity<Void> blockCard(@PathVariable Long cardId) {
        cardService.blockCard(cardId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Активировать карту")
    @PutMapping("/{cardId}/activate")
    public ResponseEntity<Void> activateCard(@PathVariable Long cardId) {
        cardService.activateCard(cardId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Удалить карту")
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Установить лимит")
    @PostMapping("/limit")
    public ResponseEntity<CardLimitDto> setLimit(@RequestBody CardLimitDto dto) {
        return ResponseEntity.ok(cardService.setCardLimit(dto));
    }



    @Operation(summary = "Узнать лимит по ID карты")
    @GetMapping("/limit/{cardId}")
    public ResponseEntity<CardLimitDto> getLimit(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.getCardLimitByCardId(cardId));
    }


    @Operation(summary = "Пагинация вывода карт с фильтрацией по статусу и балансу ")
    @GetMapping("/filter")
    public ResponseEntity<Page<CardDto>> getFilteredCards(
            @RequestParam(required = false) CardStatus status,
            @RequestParam(required = false) BigDecimal balance,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CardDto> cardPage = cardService.getFilteredCards(status, balance, page, size);
        return ResponseEntity.ok(cardPage);
    }

}
