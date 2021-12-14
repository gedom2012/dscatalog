package com.gedom.dsctalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gedom.dsctalog.DTO.ProductDTO;
import com.gedom.dsctalog.services.ProductService;
import com.gedom.dsctalog.services.exceptions.ResourceNotFoundException;
import com.gedom.dsctalog.tests.factories.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductService service;

	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;
	private Long existingId;
	private Long nonExistingId;
	private String jsonBody;

	@BeforeEach
	private void setup() throws Exception {
		productDTO = Factory.createProductDto();
		page = new PageImpl<>(List.of(productDTO));
		existingId = 1L;
		nonExistingId = 2L;

		when(service.findAllPaged(any())).thenReturn(page);
		when(service.findById(existingId)).thenReturn(productDTO);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		when(service.update(existingId, eq(any()))).thenReturn(productDTO);
		when(service.update(nonExistingId, eq(any()))).thenThrow(ResourceNotFoundException.class);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
	}

	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());

	}

	@Test
	public void findByIdShouldThrowResouceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void updateShouldSuccessWhenIdExists() throws Exception {

		jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
						.content(jsonBody)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());

	}

	@Test
	public void updateShouldThrowResouceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
		jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody));

		result.andExpect(status().isNotFound());
	}

}
