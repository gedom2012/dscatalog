package com.gedom.dsctalog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gedom.dsctalog.repositories.ProductRepository;
import com.gedom.dsctalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
public class ProductServiceIT {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existindId;
	private Long nonExistindId;
	private Long countTotalProducts;
	
	@BeforeEach
	private void setup() throws Exception {
		existindId = 5L;
		nonExistindId = 999L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void deleteShoudDeleteResourceWhenIdExists() {		
		service.delete(existindId);
		
		assertEquals(countTotalProducts -1, repository.count());
	}
	
	@Test
	public void deleteShouldBeExceptionWhenIdDoesNotExist() {
		assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistindId);
		});
		
	}

}
