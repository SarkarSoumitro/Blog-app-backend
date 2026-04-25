package com.example.The.blog.app.controller;

import com.example.The.blog.app.domain.dtos.CategoryDto;
import com.example.The.blog.app.domain.dtos.CreateCategoryRequest;
import com.example.The.blog.app.domain.entities.Category;
import com.example.The.blog.app.mappers.CategoryMapper;
import com.example.The.blog.app.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private  final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories(){
        List<CategoryDto> categories = categoryService.listCategories()
                .stream().map(category -> categoryMapper.toDto(category)).toList();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public  ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest){
        Category categorytoCreate = categoryMapper.toEntity(createCategoryRequest);
       Category savedCategory = categoryService.createCategory(categorytoCreate);
          return new ResponseEntity<>(
                  categoryMapper.toDto(savedCategory),
                  HttpStatus.CREATED
          );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id){
      categoryService.deleteCategory(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
