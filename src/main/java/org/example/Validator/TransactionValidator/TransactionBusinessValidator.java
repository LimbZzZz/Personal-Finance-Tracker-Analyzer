package org.example.Validator.TransactionValidator;

import org.example.CustomException.TransactionExceptions.FieldError;
import org.example.CustomException.TransactionExceptions.TransactionNotFoundException;
import org.example.CustomException.TransactionExceptions.ValidationTransactionException;
import org.example.DTO.Transaction.TransactionDtoRequest;
import org.example.Enum.CategoryType;
import org.example.Enum.TransactionType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TransactionBusinessValidator {
    private static final List<TransactionType> VALID_TYPES = List.of(
            TransactionType.EXPENCE, TransactionType.INCOME
    );

    private static final List<CategoryType> VALID_CATEGORY = List.of(
            CategoryType.ENTERTAINMENT, CategoryType.SHOPPING,
            CategoryType.CAFE, CategoryType.PRODUCTS,
            CategoryType.OTHER
    );

    public void fullValidate(TransactionDtoRequest request) {
        List<FieldError> errors = new ArrayList<>();
        //Ошибка в типе транзакции
        typeValidate(request, errors);
        //Ошибка категории транзакции
        //categoryValidate(request, errors);
        //Ошибка описания транзакции
        descriptionValidate(request, errors);
        //Ошибка суммы транзакции
        sumValidate(request, errors);
        //Ошибка принадлежности транзакции
        userIdValidate(request, errors);

        if(errors.size() > 0){
            throw new ValidationTransactionException(errors);
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

/*    public void categoryValidate(TransactionDtoRequest request, List<FieldError> errors){
        if(request.getCategory() == null){
            errors.add(new FieldError("Категория транзакции",
                    "Неверный тип категории",
                    "Category равно null"));
        }else{
            boolean isValidCategoryType = VALID_CATEGORY.stream()
                    .anyMatch(type -> type.equals(request.getCategory()));
            if(!isValidCategoryType){
                errors.add(new FieldError("Категория транзакции",
                        "Неверный тип категории",
                        request.getCategory().getCategoryType()));
            }
        }
    }*/

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
