package com.croche.lariharumi.service;

import com.croche.lariharumi.dto.CategoryDTO;
import com.croche.lariharumi.models.Category.Category;
import com.croche.lariharumi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Criar nova categoria
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
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
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    // Excluir categoria
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
