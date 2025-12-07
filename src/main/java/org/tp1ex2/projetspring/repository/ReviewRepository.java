package org.tp1ex2.projetspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tp1ex2.projetspring.model.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserId(Long userId);
    List<Review> findByTourId(Long tourId);
    List<Review> findByStars(Integer stars);

    @Query("SELECT r FROM Review r WHERE r.stars >= :minStars")
    List<Review> findByMinStars(@Param("minStars") Integer minStars);
}