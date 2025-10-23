package com.example.ruaycafe.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private String image;
    private String category; // hot, cold, dessert

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductIngredient> ingredients = new ArrayList<>();

    // คำนวณต้นทุนจากวัตถุดิบทั้งหมด
    public BigDecimal calculateCost() {
        return ingredients.stream()
                .map(pi -> pi.getIngredient().getCostPerUnit().multiply(pi.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // คำนวณกำไร
    public BigDecimal calculateProfit() {
        return price.subtract(calculateCost());
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<ProductIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<ProductIngredient> ingredients) { this.ingredients = ingredients; }
}
