package com.croche.lariharumi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.croche.lariharumi.models.Category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
