package org.example.bankcards.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardLimitDto {
    private Long id;

    @NotNull(message = "Daily limit cannot be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "Daily limit must be greater than zero")
    private BigDecimal dailyLimit;

    @NotNull(message = "Monthly limit cannot be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "Monthly limit must be greater than zero")
    private BigDecimal monthlyLimit;

    @NotNull(message = "Card ID cannot be null")
    private Long cardId;
}
