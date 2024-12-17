package com.croche.lariharumi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.croche.lariharumi.dto.ProductDTO;
import com.croche.lariharumi.models.Category.Category;
import com.croche.lariharumi.models.Product.Product;
import com.croche.lariharumi.repository.CategoryRepository;
import com.croche.lariharumi.repository.ProductRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        product.setPriceRange(productDTO.price());
    
        // Salva as mudanças no banco de dados
        return productRepository.save(product);
    }
    
        private static final String IMAGE_DIR = "/path/to/images/"; // Caminho onde as imagens serão salvas

    public Product uploadProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Salva a imagem
        String imageName = productId + "_" + image.getOriginalFilename();
        Path imagePath = Paths.get(IMAGE_DIR + imageName);
        Files.copy(image.getInputStream(), imagePath);

        // Salva o caminho da imagem no banco de dados
        product.setImage(imageName); // Assumindo que a classe Product tem um campo `imagePath`
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }
}
