package com.product.api.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.product.api.entity.Category;

@Repository
public interface RepoCategory extends JpaRepository<Category, Integer>  {
    
    List<Category> findAll();
    
    Category findByCategoryId(Integer categoryId);

    @Transactional
    Integer deleteByCategoryId(Integer categoryId);

    @Query(value = "SELECT * FROM category where category = :category", nativeQuery = true)
    Category findByCategory(@Param("category") String category);

}
