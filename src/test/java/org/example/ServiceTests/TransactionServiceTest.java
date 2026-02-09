package org.example.ServiceTests;

import org.example.CustomException.TransactionExceptions.ValidationTransactionException;
import org.example.DTO.Transaction.TransactionDtoRequest;
import org.example.DTO.Transaction.TransactionDtoResponce;
import org.example.Entity.Transaction;
import org.example.Entity.User;
import org.example.Enum.CategoryType;
import org.example.Enum.TransactionType;
import org.example.Repository.TransactionRepository;
import org.example.Repository.UserRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionBusinessValidator businessValidator;

    @InjectMocks
    private TransactionService transactionService;
    private User savedUser;
    private TransactionDtoRequest validRequest;
    private Transaction savedTransaction;

    @BeforeEach
    void initialize(){
        savedUser = User.builder()
                .id(1L)
                .name("SomeName")
                .email("SomeEmail@mail.ru")
                .password("SomePassword")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        validRequest = TransactionDtoRequest.builder()
                .sum(BigDecimal.valueOf(1000))
                .type(TransactionType.EXPENCE)
                .category(CategoryType.PRODUCTS)
                .description("Some description")
                .userId(1L)
                .build();

        savedTransaction = Transaction.builder()
                .id(1L)
                .sum(BigDecimal.valueOf(1000))
                .type(TransactionType.EXPENCE)
                .category(CategoryType.PRODUCTS)
                .date(LocalDateTime.now())
                .description("Some description")
                .user(savedUser)
                .build();
    }

    @Test
    public void createTransaction_shouldSaveTransaction_WhenDataIsValid(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionDtoResponce response = transactionService.createTransaction(validRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getType()).isEqualTo(TransactionType.EXPENCE);
        assertThat(response.getCategory()).isEqualTo(CategoryType.PRODUCTS);
        assertThat(response.getDescription()).isEqualTo("Some description");
        assertThat(response.getSum()).isEqualByComparingTo(BigDecimal.valueOf(1000));
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getUserName()).isEqualTo("SomeName");

        verify(transactionRepository).save(any(Transaction.class));
        verify(userRepository).findById(1L);

    }

    @Test
    public void createTransaction_shouldException_WhenDataIsIncorrect(){
        doThrow(new ValidationTransactionException(List.of()))
                .when(businessValidator).fullValidate(any(TransactionDtoRequest.class));

        List<TransactionDtoRequest> invalidRequest = List.of(
                TransactionDtoRequest.builder().type(null).category(CategoryType.CAFE).description("some descr").sum(BigDecimal.valueOf(1000)).userId(1L).build(),
                TransactionDtoRequest.builder().type(TransactionType.EXPENCE).category(null).description("some descr").sum(BigDecimal.valueOf(1000)).userId(1L).build(),
                TransactionDtoRequest.builder().type(TransactionType.EXPENCE).category(CategoryType.CAFE).description("test".repeat(51)).sum(BigDecimal.valueOf(1000)).userId(1L).build(),
                TransactionDtoRequest.builder().type(TransactionType.EXPENCE).category(CategoryType.CAFE).description("some descr").sum(BigDecimal.valueOf(-1)).userId(1L).build()
        );

        for(TransactionDtoRequest request : invalidRequest){
            assertThatThrownBy(() -> transactionService.createTransaction(request))
                    .isInstanceOf(ValidationTransactionException.class);
        }

        verify(transactionRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    public void findTransactionById_ShouldReturn_WhenIdIsValid(){
        Long transactionId = 1L;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(savedTransaction));

        TransactionDtoResponce transaction = transactionService.getTransactionById(1L);

        assertThat(transaction).isNotNull();
        assertThat(transaction.getId()).isEqualTo(1L);

        verify(transactionRepository).findById(transactionId);
    }

    @Test
    public void findAllUsers_ShouldViewAllUsers_WhenRepositoryNotEmpty(){
        Transaction transaction1 = new Transaction().builder()
                .id(1L)
                .sum(BigDecimal.valueOf(1000))
                .type(TransactionType.EXPENCE)
                .category(CategoryType.CAFE)
                .date(LocalDateTime.now())
                .description("some descr")
                .user(savedUser)
                .build();
        Transaction transaction2 = new Transaction().builder()
                .id(2L)
                .sum(BigDecimal.valueOf(2000))
                .type(TransactionType.EXPENCE)
                .category(CategoryType.RESTOURANT)
                .date(LocalDateTime.now())
                .description("some descr")
                .user(savedUser)
                .build();
        List<Transaction> transactionList = List.of(transaction1, transaction2);

        when(transactionRepository.findAll()).thenReturn(transactionList);

        List<TransactionDtoResponce> responce = transactionService.getAllTransactions();

        assertThat(responce)
                .hasSize(2)
                .extracting(TransactionDtoResponce::getId, TransactionDtoResponce::getSum,
                        TransactionDtoResponce::getType, TransactionDtoResponce::getCategory,
                        TransactionDtoResponce::getDate, TransactionDtoResponce::getDescription,
                        TransactionDtoResponce::getUserId, TransactionDtoResponce::getUserName)
                .containsExactly(
                        tuple(1L, BigDecimal.valueOf(1000), TransactionType.EXPENCE,
                                CategoryType.CAFE, transaction1.getDate(), "some descr", savedUser.getId(), savedUser.getName()),
                        tuple(2L, BigDecimal.valueOf(2000), TransactionType.EXPENCE,
                                CategoryType.RESTOURANT, transaction2.getDate(), "some descr", savedUser.getId(), savedUser.getName())
                );

        verify(transactionRepository).findAll();
    }

    @Test
    public void findAllUsers_ShouldThrowException_WhenRepositoryFails(){
        when(transactionRepository.findAll())
                .thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> transactionService.getAllTransactions())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DB error");

        verify(transactionRepository).findAll();
    }
}
