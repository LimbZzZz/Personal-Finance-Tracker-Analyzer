package org.example.Validator.TransactionValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.FieldError;
import org.example.CustomException.ValidationTransactionException;
import org.example.DTO.Transaction.TransactionDtoRequest;
import org.example.Entity.Category;
import org.example.Enum.TransactionType;
import org.example.Repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionBusinessValidator {
    private static final List<TransactionType> VALID_TYPES = List.of(
            TransactionType.EXPENSE, TransactionType.INCOME
    );

    private final CategoryRepository categoryRepository;

    private static final Map<String, TransactionType> VALID_PARE_CATEGORY_TRANSACTION = Map.of(
            "Развлечения", TransactionType.EXPENSE,
            "Ресторан", TransactionType.INCOME,
            "Кафе", TransactionType.INCOME,
            "Кино", TransactionType.EXPENSE
    );

    public void fullValidate(TransactionDtoRequest request) {

        List<FieldError> errors = new ArrayList<>();
        typeValidate(request, errors);
        categoryValidate(request, errors);
        descriptionValidate(request, errors);
        sumValidate(request, errors);
        userIdValidate(request, errors);

        if(!errors.isEmpty()){
            log.error("Ошибка создания транзакции: Type: {}, Category: {}, Company: {}, Sum: {}" +
                            "\nОшибки: {}",
                    request.getType(),
                    request.getCategoryId(),
                    request.getCompanyId(),
                    request.getSum(),
                    errors);
            throw new ValidationTransactionException(errors);
        }else{
            log.error("Транзакция успешно создана: Type: {}, Category: {}, Company: {}, Sum: {}",
                    request.getType(),
                    request.getCategoryId(),
                    request.getCompanyId(),
                    request.getSum());
        }
    }

    public void typeValidate(TransactionDtoRequest request, List<FieldError> errors){
        if(request.getType() == null){
            errors.add(new FieldError("Тип транзакции",
                    "Неверный тип транзакции",
                    "Тип равен null"));
        }else{
            boolean isValidType = VALID_TYPES.stream()
                    .anyMatch(type -> type.equals(request.getType()));
            if(!isValidType){
                errors.add(new FieldError("Тип транзакции",
                        "Неверный тип транзакции",
                        request.getType().getDisplayName()));
            }
        }
    }

    public void categoryValidate(TransactionDtoRequest request, List<FieldError> errors){
        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);

        if(category == null){
            errors.add(new FieldError("category",
                    "Категория не найдена", "id"));
        }else{
            String categoryName = category.getName();
            TransactionType allowedType = VALID_PARE_CATEGORY_TRANSACTION.get(categoryName);

            if (!allowedType.equals(request.getType())){
                errors.add(new FieldError("Тип транзакции",
                        "Категория " + categoryName + " разрешена только для типа: " + allowedType,
                        allowedType.getDisplayName()));
            }
        }
    }

    public void descriptionValidate(TransactionDtoRequest request, List<FieldError> errors){
        if(request.getDescription().length() > 50){
            errors.add(new FieldError("Описание транзакции",
                    "Описание не должно превышать 50 символов",
                    "Текущий размер описания " + String.valueOf(request.getDescription().length())));
        }
    }

    public void sumValidate(TransactionDtoRequest request, List<FieldError> errors){
        if(request.getSum() == null || request.getSum().signum() <= 0){
            errors.add(new FieldError("Сумма",
                    "Неверный формат суммы",
                    request.getSum().toString()));
        }
    }

    public void userIdValidate(TransactionDtoRequest request, List<FieldError> errors){
        if(request.getUserId() < 0){
            errors.add(new FieldError("Идентификатор пользователя",
                    "Идентификатор пользователя не может быть отрицательным",
                    String.valueOf(request.getUserId())));
        }
    }
}
