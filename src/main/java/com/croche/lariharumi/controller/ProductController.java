package com.croche.lariharumi.controller;

import com.croche.lariharumi.dto.ProductDTO;
import com.croche.lariharumi.models.Product.Product;
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

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Product", description = "Endpoints para gerenciamento de produtos")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

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
