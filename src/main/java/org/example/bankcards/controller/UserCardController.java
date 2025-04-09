package org.example.bankcards.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.bankcards.dto.CardDto;
import org.example.bankcards.dto.TransactionDto;
import org.example.bankcards.service.UserCardService.IUserCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/user/cards")
@AllArgsConstructor
@Tag(name = "Работа с картами(пользователь)", description = "Операции пользователя с картами")
public class UserCardController {

    private final IUserCardService cardService;  // Сервис для работы с карта

    @Operation(summary = "Получить все карты")
    @GetMapping
    public ResponseEntity<List<CardDto>> getAllUserCards() {
        List<CardDto> cardDtos = cardService.getAllUserCards();
        return ResponseEntity.ok(cardDtos);
    }

    @Operation(summary = "Запрос на блокировку карты")
    @PostMapping("/{cardId}/block")
    public ResponseEntity<Void> blockCard(@PathVariable Long cardId) {
        cardService.blockCard(cardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Перевод между картами")
    @PostMapping("/transfer")
    public ResponseEntity<Void> transferMoney(@RequestParam Long fromCardId,
                                              @RequestParam Long toCardId,
                                              @RequestParam BigDecimal amount) {
        cardService.transferMoney(fromCardId, toCardId, amount);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "Узнать историю транзакций по карте")
    @GetMapping("/{cardId}/transactions")
    public ResponseEntity<List<TransactionDto>> getCardTransactions(@PathVariable Long cardId) {
        List<TransactionDto> transactions = cardService.getCardTransactions(cardId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Списание средств")
    @PostMapping("/{cardId}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable Long cardId, @RequestParam BigDecimal amount) {
        cardService.withdraw(cardId, amount);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
