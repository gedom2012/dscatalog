package com.gedom.dsctalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gedom.dsctalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
