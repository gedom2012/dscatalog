package com.gedom.dsctalog.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gedom.dsctalog.DTO.ProductDTO;
import com.gedom.dsctalog.entities.Category;
import com.gedom.dsctalog.entities.Product;
import com.gedom.dsctalog.repositories.CategoryRepository;
import com.gedom.dsctalog.repositories.ProductRepository;
import com.gedom.dsctalog.services.exceptions.DataBaseException;
import com.gedom.dsctalog.services.exceptions.ResourceNotFoundException;
import com.gedom.dsctalog.tests.factories.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Mock
	private CategoryRepository catRepository;

	private Long existingId, nonExistingId, dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	ProductDTO productDto;

	@BeforeEach
	public void setup() {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		category = Factory.createCategory();
		product = Factory.createProduct();
		productDto = Factory.createProductDto();
		page = new PageImpl<>(List.of(product));

		// settings mock to methods with return values
		when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		when(repository.save(product)).thenReturn(product);
		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		when(repository.getOne(existingId)).thenReturn(product);
		when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

		// setting mocks in category
		when(catRepository.getOne(existingId)).thenReturn(category);
		when(catRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

		// settings mock to void methods
		doNothing().when(repository).deleteById(existingId);
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		verify(repository, times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

		verify(repository, times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldThrowDataBaseExceptionWhenIdisDependent() {
		assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
		});

		verify(repository, times(1)).deleteById(dependentId);
	}

	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<ProductDTO> result = service.findAllPaged(pageable);

		assertNotNull(result);
		verify(repository, times(1)).findAll(pageable);
	}

	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		ProductDTO product = service.findById(existingId);

		assertNotNull(product);
		verify(repository, times(1)).findById(existingId);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
		assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		verify(repository, times(1)).findById(nonExistingId);
	}

	@Test
	public void updateShouldSuccessWhenIdExist() {
		ProductDTO result = service.update(existingId, productDto);

		assertNotNull(result);
		verify(repository, times(1)).getOne(existingId);
		verify(catRepository, times(1)).getOne(existingId);
	}

	@Test
	public void updateResourceShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDto);
		});
		verify(repository, times(1)).getOne(nonExistingId);
	}

}
