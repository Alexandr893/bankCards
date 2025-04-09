package org.example.bankcards.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;

    @NotNull(message = "Timestamp cannot be null")
    private LocalDateTime timestamp;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Description cannot be null")
    @Size(min = 1, max = 255, message = "Description is too long")
    private String description;

}
