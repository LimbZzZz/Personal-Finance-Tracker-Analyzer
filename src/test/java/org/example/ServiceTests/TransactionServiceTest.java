package org.example.ServiceTests;

import org.example.CustomException.TransactionNotFoundException;
import org.example.CustomException.ValidationTransactionException;
import org.example.DTO.Request.TransactionDtoRequest;
import org.example.DTO.Response.TransactionDtoResponse;
import org.example.Entity.Category;
import org.example.Entity.Transaction;
import org.example.Entity.User;
import org.example.Enum.TransactionType;
import org.example.Repository.CategoryRepository;
import org.example.Repository.CompanyRepository;
import org.example.Repository.TransactionRepository;
import org.example.Repository.UserRepository;
import org.example.Service.AccountService;
import org.example.Service.TransactionService;
import org.example.Validator.TransactionValidator.TransactionBusinessValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionBusinessValidator validator;

    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private TransactionService transactionService;

    private User testUser;
    private Category testCategory;
    private Transaction testTransaction;
    private TransactionDtoRequest validRequest;
    private final String testEmail = "test@mail.ru";

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("TestUser")
                .email(testEmail)
                .build();

        testCategory = Category.builder()
                .id(1L)
                .name("Еда")
                .build();

        testTransaction = Transaction.builder()
                .id(1L)
                .sum(new BigDecimal("1000"))
                .type(TransactionType.EXPENSE)
                .description("Тестовая транзакция")
                .date(LocalDateTime.now())
                .user(testUser)
                .category(testCategory)
                .company(null)  // ← ЯВНО УКАЗЫВАЕМ null
                .build();

        validRequest = TransactionDtoRequest.builder()
                .sum(new BigDecimal("1000"))
                .type(TransactionType.EXPENSE)
                .categoryId(1L)
                .description("Тестовая транзакция")
                .companyId(null)
                .build();
    }

    @Test
    void createTransaction_ShouldSaveAndReturnDto_WhenDataValid() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        doNothing().when(validator).fullValidate(any(TransactionDtoRequest.class));

        TransactionDtoResponse response = transactionService.createTransaction(validRequest, testEmail);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getSum()).isEqualTo(new BigDecimal("1000"));
        assertThat(response.getType()).isEqualTo(TransactionType.EXPENSE);
        assertThat(response.getDescription()).isEqualTo("Тестовая транзакция");

        verify(validator).fullValidate(validRequest);
        verify(userRepository).findByEmail(testEmail);
        verify(categoryRepository).findById(1L);
        verify(transactionRepository).save(any(Transaction.class));
        verify(accountService).subtractFromAccount(validRequest.getSum(), testEmail);
        verify(companyRepository, never()).findById(any());
    }

    @Test
    void createTransaction_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.createTransaction(validRequest, testEmail))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(testEmail);

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void createTransaction_ShouldThrowException_WhenCategoryNotFound() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.createTransaction(validRequest, testEmail))
                .isInstanceOf(RuntimeException.class);

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void createTransaction_ShouldThrowException_WhenValidationFails() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));

        doThrow(new ValidationTransactionException(List.of()))
                .when(validator).fullValidate(any(TransactionDtoRequest.class));

        assertThatThrownBy(() -> transactionService.createTransaction(validRequest, testEmail))
                .isInstanceOf(ValidationTransactionException.class);

        verify(userRepository).findByEmail(testEmail);
        verify(validator).fullValidate(any(TransactionDtoRequest.class));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void getTransactionById_ShouldReturnDto_WhenTransactionExists() {
        Long transactionId = 1L;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(testTransaction));

        TransactionDtoResponse response = transactionService.getTransactionById(transactionId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(transactionId);
        assertThat(response.getUserMap()).containsKey(testUser.getId());
        assertThat(response.getCategoryMap()).containsKey(testCategory.getId());

        verify(transactionRepository).findById(transactionId);
    }

    @Test
    void getTransactionById_ShouldThrowException_WhenTransactionNotFound() {
        Long transactionId = 999L;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getTransactionById(transactionId))
                .isInstanceOf(TransactionNotFoundException.class);

        verify(transactionRepository).findById(transactionId);
    }

    @Test
    void getAllTransactions_ShouldReturnList_WhenTransactionsExist() {
        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .sum(new BigDecimal("500"))
                .type(TransactionType.INCOME)
                .user(testUser)
                .category(testCategory)
                .build();

        when(transactionRepository.findAll()).thenReturn(List.of(testTransaction, transaction2));

        List<TransactionDtoResponse> responses = transactionService.getAllTransactions();

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(TransactionDtoResponse::getId)
                .containsExactly(1L, 2L);

        verify(transactionRepository).findAll();
    }

    @Test
    void getAllTransactions_ShouldReturnEmptyList_WhenNoTransactions() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        List<TransactionDtoResponse> responses = transactionService.getAllTransactions();

        assertThat(responses).isEmpty();
        verify(transactionRepository).findAll();
    }
}