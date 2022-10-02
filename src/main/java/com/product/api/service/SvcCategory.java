package com.product.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.product.api.entity.Category;

public interface SvcCategory {
    
    List<Category> findAll();
    
    Category findByCategoryId(Integer categoryId);

    String createCategory(Category category);

    String updateCategory(Category category);

    String deleteByCategoryId(Integer categoryId);



}
