package org.example.ServiceTests;

import org.example.CustomException.AccountAlreadyExistException;
import org.example.CustomException.UserNotFoundException;
import org.example.DTO.Request.AccountDtoRequest;
import org.example.DTO.Request.TransactionDtoRequest;
import org.example.DTO.Response.AccountDtoResponse;
import org.example.Entity.Account;
import org.example.Entity.User;
import org.example.Repository.AccountRepository;
import org.example.Repository.UserRepository;
import org.example.Service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AccountService accountService;

    private AccountDtoRequest validRequest;
    private Account savedAccount;
    private User savedUser;

    @BeforeEach
    public void start(){
        validRequest = AccountDtoRequest.builder()
                .totalAccount(BigDecimal.valueOf(5000))
                .bank("Sber")
                .cardNumber("1234123412341234")
                .userId(1L)
                .build();

        savedUser = User.builder()
                .id(1L)
                .username("User")
                .email("email@mail.ru")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .password("somePassword")
                .build();

        savedAccount = Account.builder()
                .id(1L)
                .totalAccount(BigDecimal.valueOf(5000))
                .bank("Sber")
                .cardNumber("1234123412341234")
                .isActive(true)
                .user(savedUser)
                .build();
    }

    @Test
    public void createAccount_ShouldCreate_WhenDataIsValid(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        AccountDtoResponse response = accountService.createAccount(validRequest);

        assertThat(response.getTotalAccount()).isEqualTo(BigDecimal.valueOf(5000));
        assertThat(response.getBank()).isEqualTo("Sber");
        assertThat(response.getCardNumber()).isEqualTo("1234123412341234");
        assertThat(response.getUserId()).isEqualTo(1L);

        verify(userRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void createAccount_ShouldThrowException_WhenCardNumberIsExists(){
        when(accountRepository.existsByCardNumber("1234123412341234"))
                .thenReturn(true);

        assertThatThrownBy(() -> accountService.createAccount(validRequest))
                .isInstanceOf(AccountAlreadyExistException.class);

        verify(accountRepository).existsByCardNumber("1234123412341234");
        verify(accountRepository, never()).save(any());
    }

    @Test
    public void createAccount_ShouldThrowException_WhenUserIsNull(){
        AccountDtoRequest invalidRequest = AccountDtoRequest.builder()
                .totalAccount(BigDecimal.valueOf(5000))
                .bank("Sber")
                .cardNumber("1234123412341234")
                .userId(2L)
                .build();
        when(accountRepository.existsByCardNumber("1234123412341234")).thenReturn(false);
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.createAccount(invalidRequest))
                .isInstanceOf(UserNotFoundException.class);

        verify(accountRepository).existsByCardNumber("1234123412341234");
        verify(userRepository).findById(2L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void createAccount_ShouldChangeActive_WhenAccountIsFirst(){
        when(accountRepository.existsByCardNumber("1234123412341234")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        accountService.createAccount(validRequest);

        verify(accountRepository).findAll();

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();
        assertThat(capturedAccount.isActive()).isTrue();
    }

}
