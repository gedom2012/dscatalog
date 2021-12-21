package com.gedom.dsctalog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.gedom.dsctalog.DTO.ProductDTO;
import com.gedom.dsctalog.repositories.ProductRepository;
import com.gedom.dsctalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional //roll back on database
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
	
	@Test
	public void findAllPagedShouldReturnPageWhenPage0Size10() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		assertFalse(result.isEmpty());
		assertEquals(0, result.getNumber());
		assertEquals(10, result.getSize());
		assertEquals(countTotalProducts, result.getTotalElements());		
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyWhenPageDoesNotExist() {
		PageRequest pageRequest = PageRequest.of(50, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		assertTrue(result.isEmpty());	
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		assertFalse(result.isEmpty());	
		assertEquals("Macbook Pro", result.getContent().get(0).getName());
		assertEquals("PC Gamer", result.getContent().get(1).getName());
		assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}

}
