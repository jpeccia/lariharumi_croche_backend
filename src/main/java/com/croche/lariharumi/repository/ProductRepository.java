package com.croche.lariharumi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.croche.lariharumi.models.Category.Category;
import com.croche.lariharumi.models.Product.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByCategoryId(Long categoryId);

}
