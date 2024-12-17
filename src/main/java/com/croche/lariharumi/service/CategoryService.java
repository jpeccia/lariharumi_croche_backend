package com.croche.lariharumi.service;

import com.croche.lariharumi.dto.CategoryDTO;
import com.croche.lariharumi.models.Category.Category;
import com.croche.lariharumi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Criar nova categoria
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());
        category.setDescription(categoryDTO.description());
        return categoryRepository.save(category);
    }

    // Obter todas as categorias
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Obter categoria por ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    // Atualizar categoria existente
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryDTO.name());
        category.setDescription(categoryDTO.description());
        return categoryRepository.save(category);
    }

        private static final String IMAGE_DIR = "/path/to/images/"; // Caminho onde as imagens serão salvas

        public Category uploadCategoryImage(Long categoryId, MultipartFile image) throws IOException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Salva a imagem
        String imageName = categoryId + "_" + image.getOriginalFilename();
        Path imagePath = Paths.get(IMAGE_DIR + imageName);
        Files.copy(image.getInputStream(), imagePath);

        // Salva o caminho da imagem no banco de dados
        category.setImage(imageName); // Assumindo que a classe Category tem um campo `imagePath`
        return categoryRepository.save(category);
    }

    // Excluir categoria
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
