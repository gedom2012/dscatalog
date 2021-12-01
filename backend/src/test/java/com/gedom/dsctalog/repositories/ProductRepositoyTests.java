package com.gedom.dsctalog.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.gedom.dsctalog.entities.Product;

@DataJpaTest
public class ProductRepositoyTests {

	@Autowired
	private ProductRepository repository;

	@Test
	public void deleteShouldDeleteObjectWhenExists() {
		Long existingId = 1L;

		repository.deleteById(existingId);

		Optional<Product> result = repository.findById(existingId);
		assertFalse(result.isPresent());

	}

	@Test
	public void deleteShouldEmptyResultDataAccessExceptionWhenDoesNotExists() {
		Long nonExistingId = 99999L;

		assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});

	}

}
