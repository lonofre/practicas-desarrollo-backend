package com.product.api.service;

import java.util.List;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Category;

public interface SvcCategory {
    
    List<Category> findAll();
    
    Category findByCategoryId(Integer categoryId);

    ApiResponse createCategory(Category category);

    ApiResponse updateCategory(Category category);

    ApiResponse deleteByCategoryId(Integer categoryId);



}
