package com.example.ruaycafe.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private String image; // path relative to /static

    // constructors, getters, setters
    public Product() {}
    public Product(String name, Double price, String image) {
        this.name=name; this.price=price; this.image=image;
    }
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public Double getPrice(){return price;}
    public void setPrice(Double price){this.price=price;}
    public String getImage(){return image;}
    public void setImage(String image){this.image=image;}
}
