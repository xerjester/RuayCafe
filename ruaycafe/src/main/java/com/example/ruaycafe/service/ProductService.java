package com.example.ruaycafe.service;

import com.example.ruaycafe.model.Product;
import com.example.ruaycafe.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo){this.repo=repo;}
    public List<Product> findAll(){ return repo.findAll(); }
    public Product findById(Long id){ return repo.findById(id).orElse(null); }
    public Product save(Product product) {
        return repo.save(product);
    }
}
