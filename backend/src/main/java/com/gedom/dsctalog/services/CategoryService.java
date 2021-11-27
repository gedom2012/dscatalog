package com.gedom.dsctalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gedom.dsctalog.DTO.CategoryDTO;
import com.gedom.dsctalog.entities.Category;
import com.gedom.dsctalog.repositories.CategoryRepository;
import com.gedom.dsctalog.services.exceptions.DataBaseException;
import com.gedom.dsctalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true) // improvement performance to transactions of read
	public Page<CategoryDTO> findAllPaged(PageRequest page) {
		Page<Category> list = repository.findAll(page);
		return list.map(x -> new CategoryDTO(x));
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) throws Exception {
		try {
			Optional<Category> obj = repository.findById(id);
			Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
			return new CategoryDTO(entity);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}

	}

	@Transactional
	public CategoryDTO insert(CategoryDTO categoryDto) {
		Category entity = new Category();
		entity.setName(categoryDto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
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

}
