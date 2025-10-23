package com.example.ruaycafe.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal unitCost; // ต้นทุนต่อหน่วย

    // Customization
    private Integer sweetLevel; // 0-100 (ระดับความหวาน)
    private Integer iceLevel; // 0-100 (ระดับน้ำแข็ง)
    private String size; // small, medium, large
    private String note; // หมายเหตุพิเศษ

    @ManyToMany
    @JoinTable(
            name = "order_item_addon",
            joinColumns = @JoinColumn(name = "order_item_id"),
            inverseJoinColumns = @JoinColumn(name = "addon_id")
    )
    private List<AddOn> addOns = new ArrayList<>();

    // คำนวณยอดรวมของรายการนี้
    public BigDecimal getItemTotal() {
        BigDecimal addOnTotal = addOns.stream()
                .map(AddOn::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return unitPrice.add(addOnTotal).multiply(new BigDecimal(quantity));
    }

    // คำนวณต้นทุนรวมของรายการนี้
    public BigDecimal getItemCost() {
        BigDecimal addOnCost = addOns.stream()
                .map(AddOn::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return unitCost.add(addOnCost).multiply(new BigDecimal(quantity));
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    public Integer getSweetLevel() { return sweetLevel; }
    public void setSweetLevel(Integer sweetLevel) { this.sweetLevel = sweetLevel; }
    public Integer getIceLevel() { return iceLevel; }
    public void setIceLevel(Integer iceLevel) { this.iceLevel = iceLevel; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public List<AddOn> getAddOns() { return addOns; }
    public void setAddOns(List<AddOn> addOns) { this.addOns = addOns; }
}