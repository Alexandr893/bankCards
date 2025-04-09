package org.example.bankcards.service.UserCardService;

import org.example.bankcards.dao.entity.Card;
import org.example.bankcards.dao.entity.Transaction;
import org.example.bankcards.dao.entity.User;
import org.example.bankcards.dao.repository.CardRepository;
import org.example.bankcards.dao.repository.TransactionRepository;
import org.example.bankcards.pojo.CardStatus;
import org.example.bankcards.pojo.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private UserCardService userCardService;

    private User user;
    private User otherUser;
    private Card fromCard;
    private Card toCard;

    @BeforeEach
    public void setUp() {

        user = new User("user@example.com", "password", Role.USER, null);
        otherUser = new User("otheruser@example.com", "otherpassword", Role.USER, null);


        fromCard = new Card(1L, "123456789", "Иван Иванов", BigDecimal.valueOf(5000), CardStatus.ACTIVE, user);
        toCard = new Card(2L, "987654321", "Петр Петров", BigDecimal.valueOf(1000), CardStatus.ACTIVE, otherUser);


        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void testTransferMoney_Success() {

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));
        when(cardRepository.save(fromCard)).thenReturn(fromCard);
        when(cardRepository.save(toCard)).thenReturn(toCard);

        userCardService.transferMoney(1L, 2L, BigDecimal.valueOf(1000));

        assert (fromCard.getBalance().compareTo(BigDecimal.valueOf(4000)) == 0);
        assert (toCard.getBalance().compareTo(BigDecimal.valueOf(2000)) == 0);

        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    public void testTransferMoney_Failed_NotAuthenticated() {
        SecurityContextHolder.clearContext();

        assertThrows(NullPointerException.class, () -> {
            userCardService.transferMoney(1L, 2L, BigDecimal.valueOf(1000));
        });
    }

    @Test
    public void testTransferMoney_Failed_CardNotFound() {

        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            userCardService.transferMoney(1L, 2L, BigDecimal.valueOf(1000));
            assert false : "Ожидалось исключение";
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void testTransferMoney_Failed_AccessDenied() {

        Authentication authentication = new UsernamePasswordAuthenticationToken(otherUser, "otherpassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        assertThrows(AccessDeniedException.class, () -> {
            userCardService.transferMoney(1L, 2L, BigDecimal.valueOf(1000));
        });
    }
}