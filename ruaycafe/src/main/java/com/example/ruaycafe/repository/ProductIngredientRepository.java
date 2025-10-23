// ProductIngredientRepository.java
package com.example.ruaycafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ruaycafe.entity.ProductIngredient;

public interface ProductIngredientRepository extends JpaRepository<ProductIngredient, Long> {}
