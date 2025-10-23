package com.example.ruaycafe.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "addon")
public class AddOn {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // เช่น "ช็อตเพิ่ม", "วิปครีม", "ไข่มุก"
    private BigDecimal price; // ราคาเพิ่ม
    private BigDecimal cost; // ต้นทุนเพิ่ม

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient; // วัตถุดิบที่ใช้

    private BigDecimal quantityUsed; // ปริมาณที่ใช้

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }
    public BigDecimal getQuantityUsed() { return quantityUsed; }
    public void setQuantityUsed(BigDecimal quantityUsed) { this.quantityUsed = quantityUsed; }
}