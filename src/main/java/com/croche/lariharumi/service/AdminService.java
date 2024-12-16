package com.croche.lariharumi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.croche.lariharumi.models.Category.Category;
import com.croche.lariharumi.models.Product.Product;
import com.croche.lariharumi.repository.ProductRepository;

@Service
public class AdminService {

    @Autowired
    private ProductRepository productRepository;

    // Adicionar um novo produto
    @Transactional
    public Product addProduct(String name, String description, String price, Category category) {
        Product product = new Product(null, name, description, price, price, category);
        return productRepository.save(product);
    }

    // Atualizar um produto existente
    @Transactional
    public Product updateProduct(Long id, String name, String description, String price, Category category) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(name);
        product.setDescription(description);
        product.setPriceRange(price);
        product.setCategory(category);
        return productRepository.save(product);
    }

    // Remover um produto
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }
}
