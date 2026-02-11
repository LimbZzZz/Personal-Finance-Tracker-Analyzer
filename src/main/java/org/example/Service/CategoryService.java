package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.Entity.Category;
import org.example.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category){
        if(categoryRepository.existsByName(category.getName())){
         throw new IllegalArgumentException("Категория уже существует");
        }
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryByName(String name){
        return categoryRepository.findByName(name);
    }
}
