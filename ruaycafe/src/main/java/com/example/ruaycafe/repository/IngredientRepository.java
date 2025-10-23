// IngredientRepository.java
package com.example.ruaycafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ruaycafe.entity.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {}
