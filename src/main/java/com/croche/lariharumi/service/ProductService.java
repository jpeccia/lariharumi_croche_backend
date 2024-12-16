package com.croche.lariharumi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.croche.lariharumi.models.Category.Category;
import com.croche.lariharumi.models.Product.Product;
import com.croche.lariharumi.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(String name, String description, String price, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPriceRange(price);
        product.setCategory(category);  // Example, adjust as per your model
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updateProduct(Long id, String name, String description, String price, Category category) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(name);
        product.setDescription(description);
        product.setPriceRange(price);
        product.setCategory(category);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    public Product createProduct(String name, String description, Double price, String category) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createProduct'");
    }
}
