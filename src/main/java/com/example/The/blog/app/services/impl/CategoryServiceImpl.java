package com.example.The.blog.app.services.impl;

import com.example.The.blog.app.domain.entities.Category;
import com.example.The.blog.app.repositories.CategoryRepository;
import com.example.The.blog.app.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        String categoryName = category.getName();
        if(categoryRepository.existsByNameIgnoreCase(categoryName)){
            throw new IllegalArgumentException("Category already exist with name: "+categoryName );
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
       Optional<Category> category= categoryRepository.findById(id);
       if(category.isEmpty()){
           throw new NoSuchElementException("Category not found with id: " + id);
       }

       if(!category.get().getPosts().isEmpty()){
           throw new IllegalStateException("Category has post associated with it");
       }

       categoryRepository.deleteById(id);
    }

    @Override
    public Category getcategoryById(UUID id) {
     return    categoryRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Category not found with id "+ id));

    }


}
