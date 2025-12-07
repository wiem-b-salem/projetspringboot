package org.tp1ex2.projetspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tp1ex2.projetspring.model.Guide;

import java.util.List;

public interface GuideRepository extends JpaRepository<Guide, Long> {
    List<Guide> findByLanguagesContaining(String language);

    @Query("SELECT g FROM Guide g WHERE g.rating >= :minRating")
    List<Guide> findByMinRating(@Param("minRating") Double minRating);
}