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
    
    @Query(value = "SELECT * FROM category WHERE status = 1", nativeQuery = true)
    List<Category> findAll();
    
    @Query(value = "SELECT * FROM category WHERE category_id = :category_id", nativeQuery = true)
    Category findByCategoryId(@Param("category_id") Integer categoryId);

    @Transactional
    Integer deleteByCategoryId(Integer categoryId);

    @Transactional
    @Query(value = "UPDATE category SEET status = 0 WHERE category_id = :category_id", nativeQuery = true)
    Integer deactivateCategory(@Param("category_id") Integer categoryId);

    @Query(value = "SELECT * FROM category WHERE category = :category", nativeQuery = true)
    Category findByCategory(@Param("category") String category);

}
