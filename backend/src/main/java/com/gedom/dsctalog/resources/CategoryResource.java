package com.gedom.dsctalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gedom.dsctalog.entities.Category;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@GetMapping
	public ResponseEntity<List<Category>> FindAll() {
		List<Category> list = new ArrayList<>();
		list.add(new Category(1L, "Category"));
		list.add(new Category(2L, "Category"));
		return ResponseEntity.ok().body(list);

	}

}
