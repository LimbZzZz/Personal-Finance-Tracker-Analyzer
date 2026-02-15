package org.example.Validator.TransactionValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.FieldError;
import org.example.CustomException.ValidationTransactionException;
import org.example.DTO.Request.TransactionDtoRequest;
import org.example.Enum.TransactionType;
import org.example.Repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionBusinessValidator {
    private static final List<TransactionType> VALID_TYPES = List.of(
            TransactionType.EXPENSE, TransactionType.INCOME
    );

    private final CategoryRepository categoryRepository;

    public void fullValidate(TransactionDtoRequest request) {
        List<FieldError> errors = new ArrayList<>();

        typeValidate(request, errors);
        descriptionValidate(request, errors);
        sumValidate(request, errors);
        userIdValidate(request, errors);
        categoryIdValidate(request, errors);
        companyIdValidate(request, errors);

        if (!errors.isEmpty()) {
            log.error("Ошибка создания транзакции. Ошибки: {}", errors);
            throw new ValidationTransactionException(errors);
        } else {
            log.info("Транзакция успешно создана: Type: {}, CategoryId: {}, CompanyId: {}, Sum: {}",
                    request.getType(),
                    request.getCategoryId(),
                    request.getCompanyId(),
                    request.getSum());
        }
    }

    public void typeValidate(TransactionDtoRequest request, List<FieldError> errors) {
        if (request.getType() == null) {
            errors.add(new FieldError("type",
                    "Тип транзакции обязателен",
                    "null"));
        } else {
            boolean isValidType = VALID_TYPES.contains(request.getType());
            if (!isValidType) {
                errors.add(new FieldError("type",
                        "Неверный тип транзакции. Допустимые значения: " + VALID_TYPES,
                        request.getType().getDisplayName()));
            }
        }
    }

    public void descriptionValidate(TransactionDtoRequest request, List<FieldError> errors) {
        String description = request.getDescription();
        if (description != null && description.length() > 50) {
            errors.add(new FieldError("description",
                    "Описание не должно превышать 50 символов",
                    "Текущая длина: " + description.length()));
        }
    }

    public void sumValidate(TransactionDtoRequest request, List<FieldError> errors) {
        if (request.getSum() == null) {
            errors.add(new FieldError("sum",
                    "Сумма обязательна",
                    "null"));
        } else if (request.getSum().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(new FieldError("sum",
                    "Сумма должна быть положительным числом",
                    request.getSum().toString()));
        }
    }

    public void userIdValidate(TransactionDtoRequest request, List<FieldError> errors) {
        Long userId = request.getUserId();
        if (userId == null) {
            errors.add(new FieldError("userId",
                    "ID пользователя обязателен",
                    "null"));
        } else if (userId <= 0) {
            errors.add(new FieldError("userId",
                    "ID пользователя должен быть положительным",
                    userId.toString()));
        }
    }

    public void categoryIdValidate(TransactionDtoRequest request, List<FieldError> errors) {
        Long categoryId = request.getCategoryId();
        if (categoryId == null) {
            errors.add(new FieldError("categoryId",
                    "ID категории обязателен",
                    "null"));
        } else if (categoryId <= 0) {
            errors.add(new FieldError("categoryId",
                    "ID категории должен быть положительным",
                    categoryId.toString()));
        } else if (!categoryRepository.existsById(categoryId)) {
            errors.add(new FieldError("categoryId",
                    "Категория с таким ID не существует",
                    categoryId.toString()));
        }
    }

    public void companyIdValidate(TransactionDtoRequest request, List<FieldError> errors) {
        Long companyId = request.getCompanyId();
        if (companyId != null && companyId <= 0) {
            errors.add(new FieldError("companyId",
                    "ID компании должен быть положительным",
                    companyId.toString()));
        }
    }
}