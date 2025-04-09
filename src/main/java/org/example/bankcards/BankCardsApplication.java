package org.example.bankcards;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Bank Card Management API",
                version = "1.0",
                description = "Документация для API управления банковскими картами"
        )
)
public class BankCardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankCardsApplication.class, args);
    }

}
