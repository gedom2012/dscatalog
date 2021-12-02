package com.gedom.dsctalog.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.gedom.dsctalog.entities.Product;

@DataJpaTest
public class ProductRepositoyTests {

	@Autowired
	private ProductRepository repository;

	private Long existingId, nonExistingId;

	@BeforeEach
	public void setup() {
		existingId = 1L;
		nonExistingId = 99999L;
	}

	@Test
	public void deleteShouldDeleteObjectWhenExists() {
		repository.deleteById(existingId);

		Optional<Product> result = repository.findById(existingId);
		assertFalse(result.isPresent());

	}

	@Test
	public void deleteShouldEmptyResultDataAccessExceptionWhenDoesNotExists() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});

	}

	@Test
	public void returnsProductWhenObjectExists() {
		Optional<Product> result = repository.findById(existingId);
		assertNotNull(result);
	}

	@Test
	public void returnsEmptyWhenObjectDoNotExists() {
		Optional<Product> result = repository.findById(nonExistingId);
		assertTrue(result.isEmpty());
	}

}
