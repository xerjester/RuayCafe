package com.example.ruaycafe.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ruaycafe.entity.Ingredient;
import com.example.ruaycafe.repository.IngredientRepository;

@Service
public class IngredientService {
    private final IngredientRepository repo;

    public IngredientService(IngredientRepository repo){
        this.repo = repo;
    }

    public List<Ingredient> findAll(){
        return repo.findAll();
    }

    public Optional<Ingredient> findById(Long id){
        return repo.findById(id);
    }
}
