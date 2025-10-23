package com.example.ruaycafe.entity;

import java.math.BigDecimal;

public class CartItem {
    private Long productId;
    private String name;
    private Double price;
    private int qty;

    public CartItem(Long productId, String name, BigDecimal price, int qty){}
    public CartItem(Long productId, String name, Double price, int qty){
        this.productId=productId; this.name=name; this.price=price; this.qty=qty;
    }
    public Long getProductId(){return productId;}
    public void setProductId(Long p){this.productId=p;}
    public String getName(){return name;}
    public void setName(String n){this.name=n;}
    public Double getPrice(){return price;}
    public void setPrice(Double p){this.price=p;}
    public int getQty(){return qty;}
    public void setQty(int q){this.qty=q;}
    public double getTotal(){ return price * qty; }
}
