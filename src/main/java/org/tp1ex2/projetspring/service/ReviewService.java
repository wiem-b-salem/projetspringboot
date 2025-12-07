package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Review;
import java.util.List;

public interface ReviewService {
    Review create(Review review);
    Review createReview(Review review);
    List<Review> getAll();
    List<Review> getAllReviews();
    Review getById(Long id);
    Review getReviewById(Long id);
    Review update(Review review);
    Review updateReview(Review review);
    void delete(Long id);
    void deleteReview(Long id);
}
