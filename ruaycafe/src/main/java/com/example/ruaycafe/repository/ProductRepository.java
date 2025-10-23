package com.example.ruaycafe.repository;

import com.example.ruaycafe.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository มี method deleteById(Long id) ให้เรียบร้อย
}
