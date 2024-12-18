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
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final String IMAGE_UPLOAD_DIR = "uploads/images";

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

            // Atualiza o campo de imagem do produto
            existingProduct.setImage(imageUrl); // Supondo que o campo seja "image"
            productRepository.save(existingProduct);

            // Retorna a URL da imagem
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar a imagem: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/image")
    @Operation(summary = "Retorna a imagem de um produto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Imagem retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado ou imagem não encontrada")
    })
    public ResponseEntity<?> getProductImage(@PathVariable Long id) {
        try {
            // Verifica se o produto existe
            Optional<Product> existingProductOpt = productRepository.findById(id);
            if (existingProductOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
            }

            Product existingProduct = existingProductOpt.get();

            // Verifica se o produto tem uma imagem associada
            String imageUrl = existingProduct.getImage();
            if (imageUrl == null || imageUrl.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagem não encontrada");
            }

            // Resolva o caminho completo da imagem com base no diretório de upload
            Path imagePath = Paths.get(IMAGE_UPLOAD_DIR).resolve(imageUrl.replace("/uploads/images/", ""));

            // Verifica se o arquivo da imagem existe
            if (!Files.exists(imagePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagem não encontrada");
            }

            // Cria um recurso de arquivo para a imagem
            FileSystemResource resource = new FileSystemResource(imagePath);
            if (resource.exists()) {
                // Retorna a imagem com o tipo de mídia adequado
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Ou outro tipo de imagem conforme o arquivo
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagem não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar a imagem: " + e.getMessage());
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
