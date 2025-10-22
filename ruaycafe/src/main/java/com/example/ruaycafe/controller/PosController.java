package com.example.ruaycafe.controller;

import com.example.ruaycafe.model.CartItem;
import com.example.ruaycafe.model.Product;
import com.example.ruaycafe.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.*;

@Controller
public class PosController {
    private final ProductService productService;
    public PosController(ProductService productService){ this.productService = productService; }

    @GetMapping("/")
    public String index(Model model, HttpSession session){
        model.addAttribute("products", productService.findAll());
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
        p.setPrice(Double.parseDouble(data.get("price").toString()));
        productService.save(p); // ต้องเพิ่ม method save() ใน ProductService

        return Map.of("ok", true);
    }
}
