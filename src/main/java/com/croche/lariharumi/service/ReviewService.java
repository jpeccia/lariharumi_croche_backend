package com.croche.lariharumi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.croche.lariharumi.models.Product.Product;
import com.croche.lariharumi.models.Review.Review;
import com.croche.lariharumi.models.User.User;
import com.croche.lariharumi.repository.ReviewRepository;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review createReview(Product productId, User userId, String content, Integer rating) {
        Review review = new Review();
        review.setProduct(productId);
        review.setUser(userId);
        review.setComment(content);
        review.setRating(rating);
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        reviewRepository.delete(review);
    }
}
