# BankCards

## Описание

Это система управления банковскими картами, которая предоставляет пользователям возможность управлять своими картами, транзакциями, а администраторам — управлять картами пользователей и устанавливать лимиты. Приложение включает следующие ключевые функциональности:

- **Регистрация и авторизация пользователей** с использованием JWT.
- **Просмотр и управление картами** для пользователей и администраторов.
- **Установка лимитов на карты**.
- **Переводы между картами** и **операции списания средств**.
- **Просмотр истории транзакций** по картам.

## Инструкция по запуску

1. Клонируйте репозиторий с **GitHub** с помощью команды:
   git clone https://github.com/Alexandr893/bankCards.git
    cd bankCards
   
2. Запуск **Maven**:
    mvn clean install
    mvn spring-boot:run
  
3. Запуск **Docker**:
    docker-compose build
    docker-compose up

4. Указать переменные для **БД**
    DB_URL=jdbc:postgresql://localhost:5432/your_database
    DB_USERNAME=your_db_username
    DB_PASSWORD=your_db_password

