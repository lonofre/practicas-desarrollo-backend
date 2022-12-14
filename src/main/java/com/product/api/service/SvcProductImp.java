package com.product.api.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Category;
import com.product.api.entity.Product;
import com.product.api.entity.ProductBasicData;
import com.product.api.repository.RepoCategory;
import com.product.api.repository.RepoProduct;
import com.product.exception.ApiException;

@Service
public class SvcProductImp implements SvcProduct {

	@Autowired
	RepoProduct repo;

	@Autowired
	RepoCategory repoCategory;

	@Override
	public Product getProduct(String gtin) {
		Product product = repo.findByGtinAndStatus1(gtin);
		if (product != null) {
			product.setCategory(repoCategory.findByCategoryId(product.getCategory_id()));
			return product;
		} else
			throw new ApiException(HttpStatus.NOT_FOUND, "product does not exist");
	}


	@Override
	public ApiResponse createProduct(Product in) {

		Category category = repoCategory.findByCategoryId(in.getCategory_id());
		if (category == null) {
			throw new ApiException(HttpStatus.NOT_FOUND, "category not found");
		}

		Product productGtin = repo.findByGtin(in.getGtin());
		if (productGtin != null && productGtin.getStatus() == 0) {
			productGtin.setStatus(1);
			updateProduct(in, productGtin.getProduct_id());
			return new ApiResponse("product activated");
		} else if (productGtin != null && productGtin.getStatus() == 1) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "product gtin already exist");
		}

		Product product = repo.findByProduct(in.getProduct());
		if (product != null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "product name already exist");
		}

		repo.save(in);
		return new ApiResponse("product created");

	}

	@Override
	public ApiResponse updateProduct(Product in, Integer id) {
		Integer updated = 0;
		try {
			updated = repo.updateProduct(id, in.getGtin(), in.getProduct(), in.getDescription(), in.getPrice(),
					in.getStock(), in.getCategory_id());
		} catch (DataIntegrityViolationException e) {
			if (e.getLocalizedMessage().contains("gtin"))
				throw new ApiException(HttpStatus.BAD_REQUEST, "product gtin already exist");
			if (e.getLocalizedMessage().contains("product"))
				throw new ApiException(HttpStatus.BAD_REQUEST, "product name already exist");
			if (e.contains(SQLIntegrityConstraintViolationException.class))
				throw new ApiException(HttpStatus.BAD_REQUEST, "category not found");
		}
		if (updated == 0)
			throw new ApiException(HttpStatus.BAD_REQUEST, "product cannot be updated");
		else
			return new ApiResponse("product updated");
	}

	@Override
	public ApiResponse deleteProduct(Integer id) {
		if (repo.deleteProduct(id) > 0)
			return new ApiResponse("product removed");
		else
			throw new ApiException(HttpStatus.BAD_REQUEST, "product cannot be deleted");
	}

	@Override
	public ApiResponse updateProductStock(String gtin, Integer stock) {
		Product product = getProduct(gtin);
		if (stock > product.getStock())
			throw new ApiException(HttpStatus.BAD_REQUEST, "stock to update is invalid");

		repo.updateProductStock(gtin, product.getStock() - stock);
		return new ApiResponse("product stock updated");
	}


	@Override
	public List<ProductBasicData> findAll(Integer categoryId) {
		return repo.findAllByCategoryId(categoryId);
	}


	@Override
	public ApiResponse updateProductCategory(String gtin, Integer categoryId) {
		Category category = repoCategory.findByCategoryId(categoryId);
		Product product = repo.findByGtin(gtin);
		if (category == null){
			throw new ApiException(HttpStatus.BAD_REQUEST, "category not found");
		} else if (product == null) {
			throw new ApiException(HttpStatus.NOT_FOUND, "product does not exist");
		}
		repo.updateProductCategory(categoryId, gtin);
		return new ApiResponse("product category updated");
	}
}
