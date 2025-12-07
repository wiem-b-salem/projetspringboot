package org.tp1ex2.projetspring.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tp1ex2.projetspring.model.Review;
import org.tp1ex2.projetspring.repository.ReviewRepository;
import org.tp1ex2.projetspring.service.ReviewService;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    @Override
    public Review updateReview(Review review) {
        return reviewRepository.saveAndFlush(review);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public Review create(Review review) {
        return createReview(review);
    }

    @Override
    public List<Review> getAll() {
        return getAllReviews();
    }

    @Override
    public Review getById(Long id) {
        return getReviewById(id);
    }

    @Override
    public Review update(Review review) {
        return updateReview(review);
    }

    @Override
    public void delete(Long id) {
        deleteReview(id);
    }
}
