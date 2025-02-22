package com.croche.lariharumi.controller;

import com.croche.lariharumi.dto.ProductDTO;
import com.croche.lariharumi.models.Product.Product;
import com.croche.lariharumi.repository.ProductRepository;
import com.croche.lariharumi.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@Tag(name = "Product", description = "Endpoints para gerenciamento de produtos")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    ProductRepository productRepository;

    private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/uploads/images";

    @PostMapping
    @Operation(summary = "Cria um novo produto", description = "Endpoint para adicionar um novo produto ao sistema.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        Product createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct); // 201 Created
    }

    @GetMapping
    @Operation(summary = "Obtém todos os produtos", description = "Retorna uma lista de todos os produtos disponíveis.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de produtos obtida com sucesso")
    })
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products); // 200 OK
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Obtém produtos por categoria", description = "Retorna uma lista de produtos de uma categoria específica.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produtos da categoria obtidos com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products); // 200 OK
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtém um produto por ID", description = "Busca e retorna um produto específico pelo seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product); // 200 OK
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um produto", description = "Atualiza os dados de um produto existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,  // ID do produto a ser atualizado
            @RequestBody ProductDTO productDTO) {  // Dados do produto para atualização

        // Chama o serviço para atualizar o produto
        Product updatedProduct = productService.updateProduct(id, productDTO);
        
        // Retorna o produto atualizado com status HTTP 200 OK
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/{id}/upload-image")
    @Operation(summary = "Faz o upload de uma imagem para o produto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Imagem carregada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Arquivo inválido ou erro no upload"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<?> uploadProductImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile file) {
        try {
            // Verifica se o produto existe
            Optional<Product> existingProductOpt = productRepository.findById(id);
            if (existingProductOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
            }

            Product existingProduct = existingProductOpt.get();

            // Inicializa a lista de imagens se for null
            if (existingProduct.getImageUrls() == null) {
                existingProduct.setImageUrls(new ArrayList<>());
            }

            // Valida se o arquivo é uma imagem
            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O arquivo enviado não é uma imagem");
            }

            // Cria o diretório de upload se não existir
            Path dirPath = Paths.get(IMAGE_UPLOAD_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Salva o arquivo
            String filename = id + "_" + file.getOriginalFilename();
            Path filePath = dirPath.resolve(filename);
            Files.write(filePath, file.getBytes());

            // Gera a URL de acesso à imagem
            String imageUrl = "/uploads/images/" + filename;

            // Adiciona a URL da imagem à lista de imagens do produto
            existingProduct.getImageUrls().add(imageUrl);

            // Atualiza o produto no banco
            productRepository.save(existingProduct);

            // Retorna a URL da imagem
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar a imagem: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/images")
    @Operation(summary = "Retorna as imagens de um produto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Imagens retornadas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado ou imagens não encontradas")
    })
    public ResponseEntity<?> getProductImages(@PathVariable Long id) {
        try {
            // Verifica se o produto existe
            Optional<Product> existingProductOpt = productRepository.findById(id);
            if (existingProductOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
            }
    
            Product existingProduct = existingProductOpt.get();
    
            // Verifica se o produto tem imagens associadas
            List<String> imageUrls = existingProduct.getImageUrls();
            if (imageUrls == null || imageUrls.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagens não encontradas");
            }
    
            // Retorna a lista de URLs das imagens
            return ResponseEntity.ok(Map.of("imageUrls", imageUrls));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar as imagens: " + e.getMessage());
        }
    }
    
    
    @DeleteMapping("/{id}/images/{imageIndex}")
    @Operation(summary = "Remove uma imagem do produto", description = "Remove uma imagem específica de um produto.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Imagem removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto ou imagem não encontrada"),
        @ApiResponse(responseCode = "400", description = "Índice de imagem inválido")
    })
    public ResponseEntity<?> removeProductImage(
            @PathVariable Long id, 
            @PathVariable int imageIndex) {
        try {
            // Verifica se o produto existe
            Optional<Product> existingProductOpt = productRepository.findById(id);
            if (existingProductOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
            }
    
            Product existingProduct = existingProductOpt.get();
    
            // Verifica se o índice da imagem é válido
            List<String> imageUrls = existingProduct.getImageUrls();
            if (imageUrls == null || imageUrls.isEmpty() || imageIndex < 0 || imageIndex >= imageUrls.size()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Índice de imagem inválido");
            }
    
            // Remove a imagem da lista
            imageUrls.remove(imageIndex);
    
            // Atualiza o produto no banco
            productRepository.save(existingProduct);
    
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok("Imagem removida com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover a imagem: " + e.getMessage());
        }
    }
    


    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um produto", description = "Remove um produto do sistema pelo seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
