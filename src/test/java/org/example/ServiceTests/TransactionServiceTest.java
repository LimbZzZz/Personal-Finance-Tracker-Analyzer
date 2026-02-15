/*
package org.example.ServiceTests;

import org.example.CustomException.ValidationTransactionException;
import org.example.DTO.Request.TransactionDtoRequest;
import org.example.DTO.Response.TransactionDtoResponse;
import org.example.Entity.Category;
import org.example.Entity.Transaction;
import org.example.Entity.User;
import org.example.Enum.TransactionType;
import org.example.Repository.CategoryRepository;
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
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionService transactionService;
    private User savedUser;
    private Category savedCategory;
    private TransactionDtoRequest validRequest;
    private Transaction savedTransaction;

    @BeforeEach
    void initialize(){
        savedUser = User.builder()
                .id(1L)
                .username("SomeName")
                .email("SomeEmail@mail.ru")
                .password("SomePassword")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        savedCategory = Category.builder()
                .id(1L)
                .name("Развлечения")
                .color("961de0")
                .build();

        validRequest = TransactionDtoRequest.builder()
                .sum(BigDecimal.valueOf(1000))
                .type(TransactionType.EXPENCE)
                .categoryId(1L)
                .description("Some description")
                .userId(1L)
                .build();

        savedTransaction = Transaction.builder()
                .id(1L)
                .sum(BigDecimal.valueOf(1000))
                .type(TransactionType.EXPENCE)
                .category(savedCategory)
                .date(LocalDateTime.now())
                .description("Some description")
                .user(savedUser)
                .build();
    }

    @Test
    public void createTransaction_shouldSaveTransaction_WhenDataIsValid(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(savedCategory));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionDtoResponse response = transactionService.createTransaction(validRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getType()).isEqualTo(TransactionType.EXPENCE);
        assertThat(response.getCategoryName()).isEqualTo("Развлечения");
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
                TransactionDtoRequest.builder().type(null).categoryId(1L).description("some descr").sum(BigDecimal.valueOf(1000)).userId(1L).build(),
                TransactionDtoRequest.builder().type(TransactionType.EXPENCE).categoryId(1L).description("some descr").sum(BigDecimal.valueOf(1000)).userId(1L).build(),
                TransactionDtoRequest.builder().type(TransactionType.EXPENCE).categoryId(1L).description("test".repeat(51)).sum(BigDecimal.valueOf(1000)).userId(1L).build(),
                TransactionDtoRequest.builder().type(TransactionType.EXPENCE).categoryId(1L).description("some descr").sum(BigDecimal.valueOf(-1)).userId(1L).build()
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

        TransactionDtoResponse transaction = transactionService.getTransactionById(1L);

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
                .category(savedCategory)
                .date(LocalDateTime.now())
                .description("some descr")
                .user(savedUser)
                .build();
        Transaction transaction2 = new Transaction().builder()
                .id(2L)
                .sum(BigDecimal.valueOf(2000))
                .type(TransactionType.EXPENCE)
                .category(savedCategory)
                .date(LocalDateTime.now())
                .description("some descr")
                .user(savedUser)
                .build();
        List<Transaction> transactionList = List.of(transaction1, transaction2);

        when(transactionRepository.findAll()).thenReturn(transactionList);

        List<TransactionDtoResponse> responce = transactionService.getAllTransactions();

        assertThat(responce)
                .hasSize(2)
                .extracting(TransactionDtoResponse::getId, TransactionDtoResponse::getSum,
                        TransactionDtoResponse::getType, TransactionDtoResponse::getCategoryName,
                        TransactionDtoResponse::getDate, TransactionDtoResponse::getDescription,
                        TransactionDtoResponse::getUserId, TransactionDtoResponse::getUserName)
                .containsExactly(
                        tuple(1L, BigDecimal.valueOf(1000), TransactionType.EXPENCE,
                                savedCategory.getName(), transaction1.getDate(), "some descr", savedUser.getId(), savedUser.getUsername()),
                        tuple(2L, BigDecimal.valueOf(2000), TransactionType.EXPENCE,
                                savedCategory.getName(), transaction2.getDate(), "some descr", savedUser.getId(), savedUser.getUsername())
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
*/
