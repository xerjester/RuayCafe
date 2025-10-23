package com.example.ruaycafe.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ruaycafe.entity.Ingredient;
import com.example.ruaycafe.entity.Product;
import com.example.ruaycafe.entity.ProductIngredient;
import com.example.ruaycafe.repository.IngredientRepository;
import com.example.ruaycafe.repository.ProductIngredientRepository;
import com.example.ruaycafe.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepo;
    private final IngredientRepository ingredientRepo;
    private final ProductIngredientRepository piRepo;

    public ProductService(ProductRepository productRepo, IngredientRepository ingredientRepo, ProductIngredientRepository piRepo){
        this.productRepo = productRepo;
        this.ingredientRepo = ingredientRepo;
        this.piRepo = piRepo;
    }

    public List<Product> findAll(){ return productRepo.findAll(); }
    public Product findById(Long id){ return productRepo.findById(id).orElse(null); }
    public void save(Product p){ productRepo.save(p); }
    public void deleteById(Long id){ productRepo.deleteById(id); }

    // Ingredient
    public List<Ingredient> findAllIngredients(){ return ingredientRepo.findAll(); }
    public Ingredient findIngredientById(Long id){ return ingredientRepo.findById(id).orElse(null); }

    // ProductIngredient
    public void saveProductIngredient(ProductIngredient pi){ piRepo.save(pi); }
}
