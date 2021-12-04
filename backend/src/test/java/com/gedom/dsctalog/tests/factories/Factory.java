package com.gedom.dsctalog.tests.factories;

import java.time.Instant;

import com.gedom.dsctalog.DTO.ProductDTO;
import com.gedom.dsctalog.entities.Category;
import com.gedom.dsctalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(0, "Iphone", "Iphone 12, 128Gb", 5000.0, "http://xpto", Instant.now());
		product.getCategories().add(createCategory());
		return product;
	}

	public static ProductDTO createProductDto() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}

	public static Category createCategory() {
		return new Category(1L, "Smartphone");
	}

}
