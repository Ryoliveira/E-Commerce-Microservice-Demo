package com.ryoliveira.ecommerce.productservice.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryoliveira.ecommerce.productservice.dao.ProductRepository;
import com.ryoliveira.ecommerce.productservice.model.Product;

@Service
public class ProductServiceImpl implements ProductService {
	
	Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Autowired
	private ProductRepository prodRepo;

	@Override
	public Product saveProduct(Product product) {
		LOGGER.info("Saving Product: " + product.toString());
		return prodRepo.save(product);
	}

	@Override
	public Product updateProduct(Product updatedProduct) {
		Optional<Product> optional = prodRepo.findById(updatedProduct.getId());
		Product prod = new Product();
		
		try {
			prod = optional.orElseThrow(() -> new NoSuchElementException("No product with id: " + updatedProduct.getId()));
			if(updatedProduct.getName() != null) {
				prod.setName(updatedProduct.getName());
			}
			if(updatedProduct.getDescription() != null) {
				prod.setDescription(updatedProduct.getDescription());
			}
			if(updatedProduct.getPrice() != null) {
				prod.setPrice(updatedProduct.getPrice());
			}
			prod.setCategoryId(updatedProduct.getCategoryId());
			prodRepo.save(prod);
			LOGGER.info("Updated Product: " + prod.toString());
		}catch(NoSuchElementException e) {
			LOGGER.error(e.getMessage());
		}
		return prod;
	}

	@Override
	public void removeProduct(int prodId) {
		Optional<Product> optional = prodRepo.findById(prodId);
		try {
			Product prod = optional.orElseThrow(() -> new NoSuchElementException("No product with id: " + prodId));
			prodRepo.delete(prod);
			LOGGER.info("Deleted Product: " + prod.toString());
		}catch(NoSuchElementException e) {
			LOGGER.error(e.getMessage());
		}

	}

	@Override
	public Product findProductById(int prodId) {
		Optional<Product> optional = prodRepo.findById(prodId);
		Product prod = new Product();
		
		try {
			prod = optional.orElseThrow(() -> new NoSuchElementException("No product with id: " + prodId));
			LOGGER.info("Product Pulled: " + prod.toString());
		}catch(NoSuchElementException e) {
			LOGGER.error(e.getMessage());
		}
		return prod;
	}

	@Override
	public List<Product> findAll() {
		List<Product> prodList = prodRepo.findAll();
		LOGGER.info("Product List Size: " + prodList.size());
		return prodList;
	}

	@Override
	public List<Product> findAllByCategoryId(int categoryId) {
		return prodRepo.findAllByCategoryId(categoryId);
	}

}
