package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Review;
import java.util.List;

public interface ReviewService {
    Review createReview(Review review);
    List<Review> getAllReviews();
    Review getReviewById(Long id);
    Review updateReview(Review review);
    void deleteReview(Long id);
}
