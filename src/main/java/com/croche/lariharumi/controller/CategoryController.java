package com.croche.lariharumi.controller;

import com.croche.lariharumi.dto.CategoryDTO;
import com.croche.lariharumi.models.Category.Category;
import com.croche.lariharumi.repository.CategoryRepository;
import com.croche.lariharumi.service.CategoryService;
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
@RequestMapping("/categories")
@Tag(name = "Category", description = "Endpoints para gerenciar categorias de produtos")
@Validated
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final String IMAGE_UPLOAD_DIR = "uploads/images";

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

        // Novo endpoint para upload de imagem de categoria
        @PostMapping("/{id}/upload-image")
        @Operation(summary = "Faz o upload de uma imagem para a categoria")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagem carregada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido ou erro no upload"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
        })
        public ResponseEntity<?> uploadCategoryImage(
                @PathVariable Long id,
                @RequestParam("image") MultipartFile file) {
            try {
                // Verifica se a categoria existe
                Optional<Category> existingCategoryOpt = categoryRepository.findById(id);
                if (existingCategoryOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada");
                }
        
                Category existingCategory = existingCategoryOpt.get();
        
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
        
                // Atualiza o campo de imagem da categoria
                existingCategory.setImage(imageUrl); // Supondo que o campo seja "image"
                categoryRepository.save(existingCategory);
        
                // Retorna a URL da imagem
                return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao salvar a imagem: " + e.getMessage());
            }
        }
        

    @GetMapping("/{id}/image")
    @Operation(summary = "Retorna a imagem de uma categoria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Imagem retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada ou imagem não encontrada")
    })
    public ResponseEntity<?> getCategoryImage(@PathVariable Long id) {
        try {
            // Verifica se a categoria existe
            Optional<Category> existingCategoryOpt = categoryRepository.findById(id);
            if (existingCategoryOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada");
            }

            Category existingCategory = existingCategoryOpt.get();

            // Verifica se a categoria tem uma imagem associada
            String imageUrl = existingCategory.getImage();
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
                        .contentType(MediaType.IMAGE_PNG) // Ou outro tipo de imagem conforme o arquivo
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagem não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar a imagem: " + e.getMessage());
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
