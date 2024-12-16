package com.croche.lariharumi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.croche.lariharumi.models.Product.Product;
import com.croche.lariharumi.models.Review.Review;
import com.croche.lariharumi.models.User.User;
import com.croche.lariharumi.repository.ProductRepository;
import com.croche.lariharumi.repository.ReviewRepository;
import com.croche.lariharumi.repository.UserRepository;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository; // Assume that you have a Product entity

    public Review createReview(Long userId, Long productId, int rating, String comment) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    public List<Review> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
