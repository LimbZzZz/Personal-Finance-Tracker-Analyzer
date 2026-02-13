package org.example.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFound(UserNotFoundException exception,
                                                      HttpServletRequest request){
        log.warn("Пользователь, введенный в запросе, не найден {}: {}",
                request.getRequestURI(),
                exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Пользователь не найден")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> transactionNotFound(TransactionNotFoundException exception,
                                                             HttpServletRequest request){
        log.warn("Транзакция, введенная в запросе, не найдена {}: {}",
                request.getRequestURI(),
                exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Транзакция не найдена")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationTransactionException.class)
    public ResponseEntity<ErrorResponse> validationTransaction(ValidationTransactionException exception,
                                                               HttpServletRequest request){
        log.warn("Ошибка валидации транзакции при запросе {}: {}",
                request.getRequestURI(),
                exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Ошибка валидации транзакции")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryNotFound(CategoryNotFoundException exception,
                                                          HttpServletRequest request){
        log.warn("Категория, введенная в запросе, не найдена {}: {}",
                request.getRequestURI(),
                exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Категория не найдена")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<ErrorResponse> companyNotFound(CompanyNotFoundException exception,
                                                         HttpServletRequest request){
        log.warn("Компания, введенная в запросе, не найдена {}: {}",
                request.getRequestURI(),
                exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Компания не найдена")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> emailAlreadyExist(EmailAlreadyExistException exception,
                                                           HttpServletRequest request){
        log.warn("Email введенный в запросе уже существует {}: {}",
                request.getRequestURI(),
                exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Такой EMAIL уже существует")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccountAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> accountAlreadyExist(AccountAlreadyExistException exception,
                                                             HttpServletRequest request){
        log.warn("Карта, введенная в запросе, уже существует {}: {}",
                request.getRequestURI(),
                exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Эта карта уже существует")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> insufficientFunds(InsufficientFundsException exception,
                                                             HttpServletRequest request){
        log.warn("Недостаточно средств {}: {}",
                request.getRequestURI(),
                exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Недостаточно средств")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> validationEx(ValidationException exception,
                                                      HttpServletRequest request){
        log.warn("Ошибка валидации при запросе {}: {}",
                request.getRequestURI(),
                exception.getMessage());

        ErrorResponse errors = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Ошибка валидации")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception exception,
                                                   HttpServletRequest request){
        log.error("Неожиданная ошибка при запросе {}: {}",
                request.getRequestURI(),
                exception.getMessage(),
                exception);

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Ошибка сервера")
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgument(IllegalArgumentException exception,
                                                         HttpServletRequest request){
        log.warn("Неверное число в запросе {}: {}",
                request.getRequestURI(),
                exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Вы ввели неверное число")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
