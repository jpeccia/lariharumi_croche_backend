package com.croche.lariharumi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.croche.lariharumi.models.Review.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
}
