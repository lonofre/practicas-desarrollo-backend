package com.product.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;

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
        return repo.findByCategoryId(categoryId);
    }

    @Override
    public String createCategory(Category category) {
        Category categorySaved = (Category) repo.findByCategoryId(category.getCategoryId());
        if(categorySaved != null){
            if(categorySaved.getStatus() == 0){
                categorySaved.setCategoryId(1);
                repo.save(categorySaved);
                return "category has been activated";
            } else {
                return "category already exists";
            }
        }
        repo.save(category);
        return "category created";
    }

    @Override
    public String updateCategory(Category category) {
        Category categorySaved = (Category) repo.findByCategoryId(category.getCategoryId());
        if(categorySaved != null){
            if(categorySaved.getStatus() == 0){
                
                return "category is not active";
            } else {
                Category c = (Category) repo.findByCategory(category.getCategory());

                if(c != null) {
                    return "category already exists";
                }
                repo.save(category);
                return "category updated";
            }
        }
        return "category does not exists";
    }

    @Override
    public String deleteByCategoryId(Integer categoryId) {
        Category categorySaved = (Category) repo.findByCategoryId(categoryId);
        if(categorySaved == null){
            return "category does not exists";
        }
        repo.deleteByCategoryId(categoryId);
        return "category removed";
    }
    
}
