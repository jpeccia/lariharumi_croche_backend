package com.croche.lariharumi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.croche.lariharumi.models.Category.Category;
import com.croche.lariharumi.models.Product.Product;
import com.croche.lariharumi.service.AdminService;

@RestController
@RequestMapping("/admin/products")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Adicionar um novo produto ao catálogo
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String price,
            @RequestParam Category category) {
        Product product = adminService.addProduct(name, description, price, category);
        return ResponseEntity.ok(product);
    }

    // Atualizar um produto existente
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String price,
            @RequestParam Category category) {
        Product product = adminService.updateProduct(id, name, description, price, category);
        return ResponseEntity.ok(product);
    }

    // Remover um produto do catálogo
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
