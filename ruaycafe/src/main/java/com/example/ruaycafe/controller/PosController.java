package com.example.ruaycafe.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ruaycafe.entity.CartItem;
import com.example.ruaycafe.entity.Ingredient;
import com.example.ruaycafe.entity.Product;
import com.example.ruaycafe.entity.ProductIngredient;
import com.example.ruaycafe.service.IngredientService;
import com.example.ruaycafe.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PosController {
    private final ProductService productService;
    private final IngredientService ingredientService;

    public PosController(ProductService productService, IngredientService ingredientService){ this.productService = productService;this.ingredientService = ingredientService; }

    @GetMapping("/")
    public String index(Model model, HttpSession session){
        model.addAttribute("products", productService.findAll());
        model.addAttribute("ingredients", productService.findAllIngredients());
        // cart from session
        List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
        if(cart==null) cart = new ArrayList<>();
        model.addAttribute("cart", cart);
        double sum = cart.stream().mapToDouble(CartItem::getTotal).sum();
        model.addAttribute("sum", sum);
        return "index";
    }
    
    @PostMapping("/cart/add")
    @ResponseBody
    public Map<String,Object> addToCart(@RequestParam Long productId, HttpSession session){
        Product p = productService.findById(productId);
        if(p==null) return Map.of("ok", false, "msg","สินค้าไม่พบ");

        List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
        if(cart==null){ cart = new ArrayList<>(); session.setAttribute("CART", cart); }

        Optional<CartItem> exists = cart.stream().filter(ci->ci.getProductId().equals(productId)).findFirst();
        if(exists.isPresent()){
            exists.get().setQty(exists.get().getQty()+1);
        } else {
            cart.add(new CartItem(productId, p.getName(), p.getPrice(), 1));
        }
        double sum = cart.stream().mapToDouble(CartItem::getTotal).sum();
        return Map.of("ok", true, "sum", sum, "count", cart.size());
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session){
        session.removeAttribute("CART");
        return "redirect:/";
    }

    @PostMapping("/checkout")
    @ResponseBody
    public Map<String,Object> checkout(HttpSession session){
        List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
        if(cart==null || cart.isEmpty()) return Map.of("ok", false, "msg", "ตะกร้าว่าง");
        double total = cart.stream().mapToDouble(CartItem::getTotal).sum();
        // TODO: บันทึกยอดขายลง DB เพิ่มเติมได้
        session.removeAttribute("CART");
        return Map.of("ok", true, "total", total);
    }
    // เพิ่มใน PosController.java
    @PutMapping("/api/products/{id}")
    @ResponseBody
    public Map<String,Object> updateProduct(@PathVariable Long id, @RequestBody Map<String,Object> data) {
        Product p = productService.findById(id);
        if(p == null) {
            return Map.of("ok", false, "msg", "ไม่พบสินค้า");
        }

        p.setName((String) data.get("name"));
        p.setPrice(BigDecimal.valueOf(Double.parseDouble(data.get("price").toString())));
        productService.save(p); // ต้องเพิ่ม method save() ใน ProductService

        return Map.of("ok", true);
    }

    @PostMapping("/api/products")
@ResponseBody
public Map<String,Object> addProduct(@RequestBody Map<String,Object> data){
    try {
        String name = (String) data.get("name");
        Double price = Double.parseDouble(data.get("price").toString());
        String image = (String) data.get("image");

        Product p = new Product();
        p.setName(name);
        p.setPrice(BigDecimal.valueOf(price));
        p.setImage(image);
        productService.save(p);

        // เพิ่มส่วนเชื่อมวัตถุดิบ
        List<Map<String,Object>> ingredients = (List<Map<String,Object>>) data.get("ingredients");
        if(ingredients != null){
            for(Map<String,Object> i : ingredients){
                Long ingredientId = Long.parseLong(i.get("id").toString());
                BigDecimal qty = BigDecimal.valueOf(Double.parseDouble(i.get("quantity").toString()));
                Ingredient ing = productService.findIngredientById(ingredientId); // ต้องเพิ่มใน Service
                if(ing != null){
                    ProductIngredient pi = new ProductIngredient();
                    pi.setProduct(p);
                    pi.setIngredient(ing);
                    pi.setQuantity(qty);
                    productService.saveProductIngredient(pi); // ต้องเพิ่มใน Service
                }
            }
        }

        return Map.of("ok", true, "id", p.getId());
    } catch(Exception e){
        e.printStackTrace();
        return Map.of("ok", false, "msg", e.getMessage());
    }
}

    @DeleteMapping("/api/products/{id}")
    @ResponseBody
    public Map<String,Object> deleteProduct(@PathVariable Long id){
        Product p = productService.findById(id); // ใช้ instance
        if(p == null) return Map.of("ok", false, "msg","ไม่พบสินค้า");
        productService.deleteById(id); // ใช้ instance ไม่ใช่ static
        return Map.of("ok", true);
    }
}
