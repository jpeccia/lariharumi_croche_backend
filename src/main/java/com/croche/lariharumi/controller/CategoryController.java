package com.croche.lariharumi.controller;

import com.croche.lariharumi.dto.CategoryDTO;
import com.croche.lariharumi.models.Category.Category;
import com.croche.lariharumi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Category", description = "Endpoints para gerenciar categorias de produtos")
@Validated
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Endpoint para criar uma nova categoria
    @PostMapping
    @Operation(summary = "Cria uma nova categoria", description = "Endpoint utilizado para criar uma nova categoria de produto.")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    public ResponseEntity<Category> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        Category category = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);  // Retorna status 201
    }

    // Endpoint para obter todas as categorias
    @GetMapping
    @Operation(summary = "Obtém todas as categorias", description = "Endpoint utilizado para listar todas as categorias de produtos.")
    @ApiResponse(responseCode = "200", description = "Lista de categorias obtida com sucesso")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Endpoint para obter uma categoria por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtém uma categoria pelo ID", description = "Endpoint utilizado para obter uma categoria específica usando seu ID.")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Status 404 caso não encontre a categoria
        }
    }

    // Endpoint para atualizar uma categoria
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma categoria existente", description = "Endpoint utilizado para atualizar as informações de uma categoria existente.")
    @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDTO categoryDTO) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Status 404 caso não encontre a categoria
        }
    }

    // Endpoint para excluir uma categoria
    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma categoria", description = "Endpoint utilizado para excluir uma categoria existente.")
    @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();  // Status 204 caso a exclusão seja bem-sucedida
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Status 404 caso não encontre a categoria
        }
    }
}
