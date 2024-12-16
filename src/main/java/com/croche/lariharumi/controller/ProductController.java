package com.croche.lariharumi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.croche.lariharumi.models.Category.Category;
import com.croche.lariharumi.models.Product.Product;
import com.croche.lariharumi.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Endpoint para criar um produto
    @PostMapping
    public Product createProduct(@RequestParam String name, @RequestParam String description, @RequestParam String price, @RequestParam Category category) {
        return productService.createProduct(name, description, price, category);
    }

    // Endpoint para obter todos os produtos
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Endpoint para obter um produto por ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Endpoint para atualizar um produto
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestParam String name, @RequestParam String description, @RequestParam String price, @RequestParam Category category) {
        return productService.updateProduct(id, name, description, price, category);
    }

    // Endpoint para excluir um produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
