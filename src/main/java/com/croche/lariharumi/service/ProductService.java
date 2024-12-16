package com.croche.lariharumi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.croche.lariharumi.dto.ProductDTO;
import com.croche.lariharumi.models.Category.Category;
import com.croche.lariharumi.models.Product.Product;
import com.croche.lariharumi.repository.CategoryRepository;
import com.croche.lariharumi.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    // Método para criar um produto
    public Product createProduct(ProductDTO productDTO) {
        // Verifica se a categoria existe no banco de dados
        Category category = categoryRepository.findById(productDTO.categoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));
    
        // Cria um novo produto
        Product product = new Product();
        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setImage(productDTO.image());
        product.setPriceRange(productDTO.price());
        product.setCategory(category); // Associa a categoria ao produto
    
        // Salva e retorna o produto criado
        return productRepository.save(product);
    }
    

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

        // Método para buscar produtos por categoria
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updateProduct(Long id, ProductDTO productDTO) {
        // Busca o produto pelo id
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    
        // Verifica se o categoryId foi fornecido no DTO e associa a categoria
        if (productDTO.categoryId() != null) {
            Category category = categoryRepository.findById(productDTO.categoryId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            product.setCategory(category);  // Associa a categoria ao produto
        }
    
        // Atualiza os dados do produto com as informações do DTO
        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setImage(productDTO.image());
        product.setPriceRange(productDTO.price());
    
        // Salva as mudanças no banco de dados
        return productRepository.save(product);
    }
    

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }
}
