package com.croche.lariharumi.dto;

import com.croche.lariharumi.models.Category.Category;

public record ProductDTO(String name, String description, String price, Category category) {
    
}
