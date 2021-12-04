package com.gedom.dsctalog.tests.factories;

import java.time.Instant;

import com.gedom.dsctalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		return new Product(0, "Iphone", "Iphone 12, 128Gb", 5000.0, "http://xpto", Instant.now());
	}

}
