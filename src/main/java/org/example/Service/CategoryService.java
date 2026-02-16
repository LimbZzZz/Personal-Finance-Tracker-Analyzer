package org.example.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.CategoryAlreadyExist;
import org.example.CustomException.CategoryNotFoundException;
import org.example.DTO.Response.CategoryDtoResponse;
import org.example.Entity.Category;
import org.example.Entity.Transaction;
import org.example.Repository.CategoryRepository;
import org.example.Service.AOP.LogExecutionTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService{
    private final CategoryRepository categoryRepository;


    @LogExecutionTime
    public List<CategoryDtoResponse> findAllCategories(){
        List<Category> category = categoryRepository.findAll();
        return category.stream()
                .map(this::mapToDtoCategory)
                .toList();
    }

    private CategoryDtoResponse mapToDtoCategory(Category category){
        return CategoryDtoResponse.builder()
                .name(category.getName())
                .color(category.getColor())
                .transactionMap(category.getTransactions().stream()
                        .collect(Collectors.toMap(
                                Transaction::getId,
                                Transaction::getDate,
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        )))
                .build();
    }

    @LogExecutionTime
    public Optional<Category> getCategoryByName(String name){
        log.info("Операция получения категории по Имени: {} началась", name);
        long currentTime = System.currentTimeMillis();
        Optional<Category> category = categoryRepository.findByName(name);
        if(category.isEmpty()){
            log.warn("Категория: {}. Категория с таким именем не найдена", name);
            throw new CategoryNotFoundException(-1L);
        }
        log.info("Операция получения категории по имени: {}, выполнена успешно" +
                "\nВремя выполения операции: {}мс", name, (System.currentTimeMillis() - currentTime));
        return category;
    }
}
