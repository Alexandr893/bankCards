package org.example.bankcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiExceptionResponse {
    private LocalDateTime timestamp;
    private int status;
    private String message;
}
