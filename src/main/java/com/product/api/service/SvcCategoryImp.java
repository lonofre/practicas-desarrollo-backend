package com.product.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;
import com.product.exception.ApiException;

@Service
public class SvcCategoryImp implements SvcCategory {

    @Autowired
    RepoCategory repo;

    @Override
    public List<Category> findAll() {
        return repo.findAll();
    }

    @Override
    public Category findByCategoryId(Integer categoryId) {
        Category category = repo.findByCategoryId(categoryId);
        if(category == null){
            throw new ApiException(HttpStatus.NOT_FOUND, "category does not exists");
        }
        return category;
    }

    @Override
    public ApiResponse createCategory(Category category) {
        Category categorySaved = (Category) repo.findByCategoryId(category.getCategoryId());
        if(categorySaved != null){
            if(categorySaved.getStatus() == 0){
                categorySaved.setCategoryId(1);
                repo.save(categorySaved);
                return new ApiResponse("category has been activated");
            } else {
                throw new ApiException(HttpStatus.BAD_REQUEST, "category already exists");
            }
        }
        repo.save(category);
        return new ApiResponse("category created");
    }

    @Override
    public ApiResponse updateCategory(Category category) {
        Category categorySaved = (Category) repo.findByCategoryId(category.getCategoryId());
        if(categorySaved != null){
            if(categorySaved.getStatus() == 0){
                throw new ApiException(HttpStatus.BAD_REQUEST, "category is not active");
            } else {
                Category c = (Category) repo.findByCategory(category.getCategory());

                if(c != null) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "category already exists");
                }
                repo.save(category);
                return new ApiResponse("category updated");
            }
        }
        throw new ApiException(HttpStatus.NOT_FOUND, "category does not exists");
    }

    @Override
    public ApiResponse deleteByCategoryId(Integer categoryId) {
        Category categorySaved = (Category) repo.findByCategoryId(categoryId);
        if(categorySaved == null){
            throw new ApiException(HttpStatus.NOT_FOUND, "category does not exists");
        }
        repo.deleteByCategoryId(categoryId);
        return new ApiResponse("category removed");
    }
    
}
