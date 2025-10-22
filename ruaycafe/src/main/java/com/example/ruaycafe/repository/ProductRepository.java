package com.example.ruaycafe.repository;

import com.example.ruaycafe.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
