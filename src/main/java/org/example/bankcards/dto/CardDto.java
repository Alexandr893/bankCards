package org.example.bankcards.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private Long id;

    @NotNull(message = "Card number cannot be null")
    @Size(min = 13, max = 19, message = "Card number must be between 13 and 19 characters")
    private String encryptedCardNumber;

    @NotNull(message = "Cardholder name cannot be null")
    @Size(min = 1, max = 255, message = "Cardholder name is too long")
    private String cardholderName;

    @NotNull(message = "Expiry date cannot be null")
    private LocalDate expiryDate;

    @NotNull(message = "Status cannot be null")
    @Pattern(regexp = "ACTIVE|BLOCKED", message = "Status must be either ACTIVE or BLOCKED")
    private String status;

    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "Balance must be greater than zero")
    private BigDecimal balance;

    private UserDto owner;

    private List<TransactionDto> transactions;
}
