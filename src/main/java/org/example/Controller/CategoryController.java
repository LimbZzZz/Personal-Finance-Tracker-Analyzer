package org.example.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.DTO.Response.CategoryDtoResponse;
import org.example.Service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/show")
    public ResponseEntity<List<CategoryDtoResponse>> showAllCategories(){
        log.info("Запрос на получение списка категорий получен");
        return ResponseEntity.ok(categoryService.findAllCategories());
    }
}
