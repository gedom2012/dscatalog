package com.gedom.dsctalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gedom.dsctalog.DTO.CategoryDTO;
import com.gedom.dsctalog.DTO.ProductDTO;
import com.gedom.dsctalog.entities.Category;
import com.gedom.dsctalog.entities.Product;
import com.gedom.dsctalog.repositories.CategoryRepository;
import com.gedom.dsctalog.repositories.ProductRepository;
import com.gedom.dsctalog.services.exceptions.DataBaseException;
import com.gedom.dsctalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = repository.findAll(pageable);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) throws Exception {
		try {
			Optional<Product> obj = repository.findById(id);
			Product entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
			return new ProductDTO(entity, entity.getCategories());
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	@Transactional
	public ProductDTO insert(ProductDTO productDTO) {
		Product entity = new Product();
		copyDtoToEntity(productDTO, entity);		
		entity = repository.save(entity);
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id); // getOne == return a entity without access the database.
			copyDtoToEntity(dto, entity);
			repository.save(entity);
			return new ProductDTO(entity, entity.getCategories());
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}

	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation");
		}
	}

	private void copyDtoToEntity(ProductDTO productDTO, Product entity) {
		entity.setName(productDTO.getName());
		entity.setDescription(productDTO.getDescription());
		entity.setDate(productDTO.getDate());
		entity.setImgUrl(productDTO.getImgUrl());
		entity.setPrice(productDTO.getPrice());

		entity.getCategories().clear();
		for (CategoryDTO catDto : productDTO.getCategories()) { // pegando todas as categorias que retornaram no DTO e copiando para minha entidade
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}

	}

}
