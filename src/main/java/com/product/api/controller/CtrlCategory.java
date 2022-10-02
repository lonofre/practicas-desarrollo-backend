package com.product.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.api.entity.Category;
import com.product.api.service.SvcCategory;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CtrlCategory {
    
    @Autowired
    private SvcCategory svc;

    // Valores por defecto al iniciar el programa
    private ArrayList<Category> categories = new ArrayList<>(){
        { 
            add(new Category(1, "Abarrotes", 1));
            add(new Category(2, "Electrónica", 1));
        }
    };

    @GetMapping()
    public ResponseEntity<List<Category>> getCategories(){
        return new ResponseEntity<>(svc.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(
        @PathVariable(value = "categoryId") Integer categoryId){
        return new ResponseEntity<>(svc.findByCategoryId(categoryId), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> createCategory(
        @Valid @RequestBody Category category,
        BindingResult bindingResult
    ){
        String message = "";
        if(bindingResult.hasErrors()){
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(svc.createCategory(category), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<String> updateCategory(
        @Valid @RequestBody Category category,
        BindingResult bindingResult 
    ){
        String message = "";
        if(bindingResult.hasErrors()){
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(svc.updateCategory(category), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(
        @PathVariable(value = "categoryId") Integer categoryId
    ){
        return new ResponseEntity<>(svc.deleteByCategoryId(categoryId), HttpStatus.OK);
    }


}
