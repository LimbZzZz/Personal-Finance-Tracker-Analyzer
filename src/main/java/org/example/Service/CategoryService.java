package org.example.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.CategoryAlreadyExist;
import org.example.CustomException.CategoryNotFoundException;
import org.example.Entity.Category;
import org.example.Repository.CategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryService extends BaseService<Category, Long>{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

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
