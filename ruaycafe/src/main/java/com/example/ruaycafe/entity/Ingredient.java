package com.example.ruaycafe.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // ชื่อวัตถุดิบ เช่น "เมล็ดกาแฟอาราบิก้า"
    private String unit; // หน่วย เช่น "กรัม", "มล.", "ชิ้น"
    private BigDecimal stockQuantity; // ปริมาณคงเหลือ
    private BigDecimal costPerUnit; // ต้นทุนต่อหน่วย
    private BigDecimal minStock; // สต็อกขั้นต่ำที่ต้องแจ้งเตือน

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(BigDecimal stockQuantity) { this.stockQuantity = stockQuantity; }
    public BigDecimal getCostPerUnit() { return costPerUnit; }
    public void setCostPerUnit(BigDecimal costPerUnit) { this.costPerUnit = costPerUnit; }
    public BigDecimal getMinStock() { return minStock; }
    public void setMinStock(BigDecimal minStock) { this.minStock = minStock; }
}