package org.example.Validator.TransactionValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.FieldError;
import org.example.CustomException.ValidationTransactionException;
import org.example.DTO.Request.TransactionDtoRequest;
import org.example.Enum.TransactionType;
import org.example.Repository.CategoryRepository;
import org.example.Repository.CompanyRepository;
import org.example.Repository.UserRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionBusinessValidator {

    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;

    public void fullValidate(TransactionDtoRequest request) {
        List<FieldError> errors = new ArrayList<>();

        validateCategoryExists(request, errors);

        if(request.getCompanyId() != null){
            validateCompanyExists(request, errors);
        }

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

    public void validateCategoryExists(TransactionDtoRequest request, List<FieldError> errors){
        if(!categoryRepository.existsById(request.getCategoryId())){
            errors.add(new FieldError("Category ID",
                    "Категории с этим ID не существует",
                    String.valueOf(request.getCategoryId())));
        }
    }

    public void validateCompanyExists(TransactionDtoRequest request, List<FieldError> errors){
        if(!companyRepository.existsById(request.getCategoryId())){
            errors.add(new FieldError("Company ID",
                    "Компании с этим ID не существует",
                    String.valueOf(request.getCompanyId())));
        }
    }

}