package com.gedom.dsctalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gedom.dsctalog.entities.Category;
import com.gedom.dsctalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true) // improvement performance to transactions of read
	public List<Category> findAll() {
		return repository.findAll();
	}

}
