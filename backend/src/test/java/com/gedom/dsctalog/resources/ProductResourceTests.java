package com.gedom.dsctalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.gedom.dsctalog.DTO.ProductDTO;
import com.gedom.dsctalog.services.ProductService;
import com.gedom.dsctalog.tests.factories.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService service;

	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;

	@BeforeEach
	private void setup() {
		productDTO = Factory.createProductDto();
		page = new PageImpl<>(List.of(productDTO));

		when(service.findAllPaged(any())).thenReturn(page);
	}

	@Test
	private void findAllShouldReturnPage() throws Exception {
		ResultActions result = mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
	}

}
