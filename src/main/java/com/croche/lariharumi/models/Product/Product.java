package com.croche.lariharumi.models.Product;

import java.util.ArrayList;
import java.util.List;

import com.croche.lariharumi.models.Category.Category;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private List<String> imageUrls = new ArrayList<>(); // Lista de URLs de imagens
    private String priceRange;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
}
